package org.sifrproject.servlet;

import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationFactory;
import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGeneratorDispatcher;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;
import org.sifrproject.annotations.input.BioPortalJSONAnnotationParser;
import org.sifrproject.annotations.model.BioPortalLazyAnnotationFactory;
import org.sifrproject.annotations.model.BioportalErrorAnnotation;
import org.sifrproject.annotations.model.retrieval.CUIPropertyRetriever;
import org.sifrproject.annotations.model.retrieval.SemanticTypePropertyRetriever;
import org.sifrproject.annotations.output.LIRMMOutputGeneratorDispatcher;
import org.sifrproject.annotations.umls.UMLSGroupIndex;
import org.sifrproject.annotations.umls.UMLSSemanticGroupsLoader;
import org.sifrproject.parameters.ContextParameterHandler;
import org.sifrproject.parameters.LIRMMProxyParameterRegistry;
import org.sifrproject.parameters.ScoreParameterHandler;
import org.sifrproject.parameters.SemanticGroupParameterHandler;
import org.sifrproject.parameters.api.ParameterRegistry;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.LIRMMPostAnnotationRegistry;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RestfulQuery;
import org.sifrproject.util.UrlParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparqy.graph.storage.JenaRemoteSPARQLStore;
import org.sparqy.graph.storage.StoreHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implements the core features of the AnnotatorPlus web services:
 * - It queries the suitable bioportal annotation server (w.r.t implementing subclasses)
 * - adds several new features
 *
 * @authors Julien Diener, Emmanuel Castanier
 */
public class AnnotatorServlet extends HttpServlet {
    private static final long serialVersionUID = -7313493486599524614L;
    private static final String sparqlServer = "http://sparql.bioportal.lirmm.fr/sparql/";
    private static final Logger logger = LoggerFactory.getLogger(AnnotatorServlet.class);

    private String annotatorURI = null;

    private AnnotationParser parser;

    private PostAnnotationRegistry postAnnotationRegistry;
    private ParameterRegistry parameterRegistry;

    private Properties proxyProperties;

    private OutputGeneratorDispatcher outputGeneratorDispatcher;

    public AnnotatorServlet() {
        try {
            /*
             * Instantiating annotation parser and dependencies
             */
            StoreHandler.registerStoreInstance(new JenaRemoteSPARQLStore(sparqlServer));
            PropertyRetriever cuiRetrieval = new CUIPropertyRetriever();
            PropertyRetriever typeRetrieval = new SemanticTypePropertyRetriever();
            UMLSGroupIndex umlsGroupIndex = UMLSSemanticGroupsLoader.load();
            AnnotationFactory annotationFactory = new BioPortalLazyAnnotationFactory();
            parser = new BioPortalJSONAnnotationParser(annotationFactory, cuiRetrieval, typeRetrieval, umlsGroupIndex);

            /*
            * Loading configuration properties
            */
            InputStream proxyPropertiesStream = AnnotatorServlet.class.getResourceAsStream("/annotatorProxy.properties");
            proxyProperties = new Properties();
            proxyProperties.load(proxyPropertiesStream);

            /*
             * Instantiating parameter and post-annotation registries
             */

            //The registry is used to register post-annotation components that will add new annotations
            //to the annotations produced by the BioPortal annotator
            //This needs to be initialized here, as parameter handler will need and instance of the registry
            //in order to directly register post-annotation components depending on the values of the parameters
            postAnnotationRegistry = new LIRMMPostAnnotationRegistry();
            parameterRegistry = new LIRMMProxyParameterRegistry(postAnnotationRegistry);

            /*
             * Registering parameter handlers
             */

            parameterRegistry.registerParameterHandler("semantic_groups", new SemanticGroupParameterHandler(), true);
            parameterRegistry.registerParameterHandler("score", new ScoreParameterHandler(), true);
            parameterRegistry.registerParameterHandler("negation|experiencer|temporality", new ContextParameterHandler(), true);


            /*
             * Initializing the output generator dispatcher that allows to generate the output that corresponds to a
             * particular format from the annotation model automatically
             */
            outputGeneratorDispatcher = new LIRMMOutputGeneratorDispatcher();
        } catch (IOException e) {
            logger.error("Cannot instantiate servlet: {}", e.getLocalizedMessage());
        }
    }

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

        //Retrieving format parameter, json is the default output format if the format parameter is absent
        String format = parameters.getFirst("format", "json").toLowerCase();

        //Retrieving the source text parameter
        String text = parameters.getFirst("text", "").toLowerCase();

        //Create annotation list to hold the annotation model
        List<Annotation> annotations;
        try {

            /*
             * Running parameter handlers
             */
            parameterRegistry.processParameters(parameters);

            /*
            * Querying the bioportal annotator and building the model
            */
            String queryOutput = RestfulQuery.queryAnnotator(parameters, annotatorURI);
            System.out.println(queryOutput);
            annotations = parser.parseAnnotations(queryOutput);

        } catch (InvalidParameterException | ParseException | IOException | NCBOAnnotatorErrorException e) {
            /*
             * Handling exceptions by activating the 'error' output format and creating a single error annotation that
             * will contain the error message. This requires that the OutputGeneratorDispatcher registers the appropriate
             * error output generator, which is the case with the current LIRMMOutputGeneratorDispatcher
             */
            format = "error";
            annotations = new ArrayList<>();
            annotations.add(new BioportalErrorAnnotation(e.getMessage()));
        }

        /*
        *Applying post-processors
        */
        postAnnotationRegistry.apply(annotations, text);
        postAnnotationRegistry.clear();

        /*
         * Output section, generates the output from the model with the dispatcher and sends the response to the servlet
         */
        //Response writer
        response.setCharacterEncoding("UTF-8");
        PrintWriter servletResponseWriter = response.getWriter();
        outputContent(outputGeneratorDispatcher.generate(format, annotations, annotatorURI), response, servletResponseWriter);

    }

    private void outputContent(AnnotatorOutput annotatorOutput, HttpServletResponse response, PrintWriter output) {
        response.setContentType(String.format("%s; charset=UTF-8", annotatorOutput.getMimeType()));
        output.println(annotatorOutput.getContent());
        output.flush();
    }

}
