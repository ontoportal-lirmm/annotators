package org.sifrproject.servlet;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.output.OutputGeneratorDispatcher;
import org.sifrproject.annotations.output.LIRMMOutputGeneratorDispatcher;
import org.sifrproject.parameters.semanticgroups.GroupParameterHandler;
import org.sifrproject.scoring.CValueScore;
import org.sifrproject.scoring.OldScore;
import org.sifrproject.util.UrlParameters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implements the core features of the AnnotatorPlus web services:
 * - It queries the suitable bioportal annotation server (w.r.t implementing subclasses)
 * - add a "score" functionality that sort output following a scoring method
 * - add a "format=rdf" to the current possible output formats of bioportal (json, xml, ...)
 *
 * @authors Julien Diener, Emmanuel Castanier
 */
public class AnnotatorServlet extends HttpServlet {
    private static final long serialVersionUID = -7313493486599524614L;
    private static final String sparqlServer = "http://sparql.bioportal.lirmm.fr/sparql/";

    private String annotatorURI = null;

    // redirect GET to POST
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // parse parameters
        UrlParameters parameters = new UrlParameters(request);


        /*
         * Loading configuration properties
         */
        InputStream proxyPropertiesStream = AnnotatorServlet.class.getResourceAsStream("/annotatorProxy.properties");
        Properties proxyProperties = new Properties();
        proxyProperties.load(proxyPropertiesStream);

        /*
         * Initializing the annotator URI, from the properties if present
         * Otherwise the default behaviour is adopted, assuming the proxy runs on the same machine as the ncbo annotator
         * but on different ports (by default 80 for the proxy and 8080 for the ncbo annotator).
         */
        if (proxyProperties.containsKey("annotatorURI")) {
            //To debug locally using a remote bioportal, comment matcher condition above and uncomment the line below
            //annotatorURI = "http://services.bioportal.lirmm.fr:8080/servlet?";
            annotatorURI = proxyProperties.getProperty("annotatorURI");
        } else {
            // Extract the base url of the tomcat server and generate the servlet URL from it (the servlet have to be
            // deployed on the same server as the servlet used)
            Pattern pattern = Pattern.compile("^((?:https?://)?[^:]+)");
            Matcher matcher = pattern.matcher(request.getRequestURL().toString());
            if (matcher.find()) {
                annotatorURI = matcher.group(1) + ":8080/servlet?";
            }
        }

        /*
         * Parameter handling section, simple values that require no processing retrieved directly, more complex parameter
         * handling must be implemented as a ParameterHandler and registered below.
         * Parameter handlers should register appropriate options post-annotators that correspond to the parameter themselves.
         */

        //Retrieving format parameter, json is the default output format if the format parameter is absent
        String format = getFirst(parameters.get("format"), "json").toLowerCase();


        /*
         *
         */


        OutputGeneratorDispatcher outputGeneratorDispatcher = new LIRMMOutputGeneratorDispatcher(annotatorURI);
        //Response writer
        response.setCharacterEncoding("UTF-8");
        PrintWriter servletResponseWriter = response.getWriter();
        servletResponseWriter.write(outputGeneratorDispatcher.generate(format, annotations));


        ////////////////////////////////////////////////////
        String score = getFirst(parameters.remove("score"), "false").toLowerCase();

        String semanticGroups = getFirst(parameters.get("semantic_groups"), "");
        String initialSemanticTypes = getFirst(parameters.get("semantic_types"), "");

        if (!format.equals("json")) {
            parameters.remove("format");
            parameters.put("format", new String[]{"json"});
        }

        String unknownGroups = "";
        if (!semanticGroups.isEmpty()) {
            unknownGroups = GroupParameterHandler.processGroupParameter(parameters, initialSemanticTypes, semanticGroups);
        }

        // process query
        JSON annotations;
        Debug.clear();




        // test whether the query contains unimplemented features
        if (!(score.equals("false") || score.equals("old") || score.equals("cvalue") || score.equals("cvalueh"))) {
            annotations = new JSON(new UnsupportedOperationException(MessageFormat.format("unknown score:{0}", score)));
            outputContent("application/json", annotations.toString(), response, servletResponseWriter);
        } else if (!score.equals("false") && !(format.equals("json") || format.equals("rdf") || format.equals("brat") || format.equals("debug"))) {
            annotations = new JSON(new UnsupportedOperationException("The score feature can only be invoked if the output format is json or rdf"));
            outputContent("application/json", annotations.toString(), response, servletResponseWriter);
        } else if (!unknownGroups.isEmpty()) {
            annotations = new JSON(new UnsupportedOperationException(MessageFormat.format("The specified UMLS groups do not exist: {0}", unknownGroups)));
            outputContent("application/json", annotations.toString(), response, servletResponseWriter);
        } else {
            // query servlet
            String queryOutput = queryAnnotator(parameters);


            if (format.equals("brat")) {
                try {
                    outputContent("application/brat", BRAT.convertAnnotationsToBRAT(queryOutput, sparqlServer), response, servletResponseWriter);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                annotations = new JSON(queryOutput);
                // additional features
                if (annotations.getType() == JSONType.ARRAY) {

                    Scorer scorer = null;
                    switch (score) {
                        case "old":
                            scorer = new OldScore(annotations);
                            break;
                        case "cvalue":
                            scorer = new CValueScore(annotations, true);
                            break;
                        case "cvalueh":
                            scorer = new CValueScore(annotations, false);
                            break;
                    }
                    if (scorer != null) {
                        Map<String, Double> scores = scorer.compute();
                        annotations = scorer.getScoredAnnotations(scores);
                    }

                    switch (format) {
                        case "rdf":
                            outputContent("application/rdf+xml", JsonToRdf.convert(annotations, annotatorURI), response, servletResponseWriter);
                            break;
                        case "debug":
                            outputContent("application/json", Debug.makeDebugAnnotations(annotations).toString(), response, servletResponseWriter);
                            break;
                        default:
                            outputContent("application/json", annotations.toString(), response, servletResponseWriter);
                    }
                }
            }
        }
    }

    private void outputContent(String type, String content, HttpServletResponse response, PrintWriter output) {
        response.setContentType(String.format("%s; charset=UTF-8", type));
        output.println(content);
        output.flush();
    }

    /**
     * Retrieve the first parameter value from the {@code values} array if any, otherwise returns the {@code defaultValue} supplied
     *
     * @param values       The array potentially containing the value of the parameter
     * @param defaultValue The default value to return is the first argument is null or empty
     * @return The parameter or the default value
     */
    private String getFirst(String[] values, String defaultValue) {
        String value = defaultValue;
        if (values != null && values.length > 0)
            value = values[0];
        return value;
    }

    private String queryAnnotator(UrlParameters parameters) {
        // make query URL
        String url = parameters.makeUrl(annotatorURI);

        // query servlet
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

            HttpResponse httpResponse;
            try {
                URI uri = new URI(url);
                httpResponse = client.execute(new HttpGet(uri));

            } catch (URISyntaxException | IOException e) {
                return e.getLocalizedMessage();

            }


            // process response

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {
                Optional<String> fileContents = bufferedReader.lines().reduce(String::concat);
                return fileContents.orElse("Impossible to read response");
            } catch (IOException e) {
                return e.getLocalizedMessage();
            }
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }

    }
}
