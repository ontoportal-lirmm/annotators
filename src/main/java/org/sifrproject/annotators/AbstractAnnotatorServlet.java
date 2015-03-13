package org.sifrproject.annotators;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.sifrproject.scoring.OldScore;
import org.sifrproject.util.JSON;
import org.sifrproject.util.JSONType;
import org.sifrproject.util.UrlParameters;
import org.sifrproject.format.JsonToRdf;


/**
 * Implements the core functionalities of the AnnotatorPlus web services:
 *   - It queries the suitable bioportal annotation server (w.r.t implementing subclasses)
 *   - add a "score" functionality that sort output following a scoring method
 *   - add a "format=rdf" to the current possible output formats of bioportal (json, xml, ...)
 * 
 * All Implementations of AnnotatorPlus services should inherit from this class 
 * and implements the  abstract method {@link getAnnotatorBaseUrl}
 * 
 * @authors Julien Diener, Emmanuel Castanier
 */
public abstract class AbstractAnnotatorServlet extends HttpServlet {
    private static final long serialVersionUID = -7313493486599524614L;

    
    protected abstract String getAnnotatorBaseURL();

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
        
        String score  = getFirst(parameters.remove("score"), "false").toLowerCase();
        String format = getFirst(parameters.get("format"),   "json" ).toLowerCase();
        
        if(format=="rdf") 
        	parameters.remove("format");
            parameters.put("format", new String[]{"json"});
        
        // process query
        JSON annotations;
        String annotationsRdfOutput = "";
        
            // test for call to not implemented functionalities
        if(!score.equals("false") && !score.equals("old")){
            annotations = new JSON(new UnsupportedOperationException("score="+score+" is not implemented"));
            
        }else if(!score.equals("false") && !(format.equals("json") || format.equals("rdf"))){
            annotations = new JSON(new UnsupportedOperationException("score parameter cannot be used if format is not json or rdf"));
            
        }else{
            // query annotator
            annotations = queryAnnotator(parameters);
        
            // additional functionalities
            if(annotations.getType()==JSONType.ARRAY){
                
                if(score.equals("old")){
                        OldScore scorer = new OldScore(annotations);
                        Map<String, Double> scores = scorer.compute();
                        annotations = scorer.getScoredAnnotations(scores);
                }
                // TODO: score=cvalue & cvalueh
                
                // TODO: format RDF
                if(format.equals("rdf"))
                    annotationsRdfOutput = JsonToRdf.convert(annotations);
            }
        }
        
        // process response
        PrintWriter out = response.getWriter();
        
        if (format.equals("rdf")) {
        	response.setContentType("application/rdf+xml;charset=UTF-8");
	        out.println(annotationsRdfOutput);
	        out.flush();
        } else {
	        response.setContentType("application/json;charset=UTF-8");
	        out.println(annotations.toString());
	        out.flush();
        }
    }

    private static String getFirst(String[] values, String defaultValue){
        String value = defaultValue;
        if(values!=null && values.length>0)
            value = values[0];
        return value;
    }
    
    private JSON queryAnnotator(UrlParameters parameters){
        // make query URL
        String url = parameters.makeUrl(getAnnotatorBaseURL());
                
        // query annotator
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpResponse httpResponse = null;
        try{
            URI uri = new URI(url.replace(" ", "%20"));  
            httpResponse = client.execute(new HttpGet(uri));
            
        } catch (URISyntaxException e) {
            return new JSON(e);
            
        }catch(IOException e){
            return new JSON(e);
        }

        
        // process response
        JSON annotations;
        try{
            annotations = new JSON(httpResponse.getEntity().getContent());
        }catch (IOException e){
            annotations = new JSON(e);  
        }

        
        // close http client
        try{
            client.close();        
        }catch(IOException e1){
            e1.printStackTrace();  
        }

        return annotations;
    }
}
