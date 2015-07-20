package org.sifrproject.annotators;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.sifrproject.scoring.CValueScore;
import org.sifrproject.scoring.OldScore;
import org.sifrproject.scoring.Scorer;
import org.sifrproject.util.JSON;
import org.sifrproject.util.JSONType;
import org.sifrproject.util.Debug;
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
public class AnnotatorServlet extends HttpServlet {
    private static final long serialVersionUID = -7313493486599524614L;

    protected String annotatorURI;
    
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
        Debug.clear();


        annotatorURI = "http://data.bioontology.org/annotator?";
        // to query the NCBO annotator

        
            // test for call to not implemented functionalities
        if(!(score.equals("false") || score.equals("old") || score.equals("cvalue") || score.equals("cvalueh"))){
            annotations = new JSON(new UnsupportedOperationException("unknow score:"+score));
            
        }else if(!score.equals("false") && !(format.equals("json") || format.equals("rdf") || format.equals("debug"))){
            annotations = new JSON(new UnsupportedOperationException("score parameter cannot be used if format is not json or rdf"));
            
        }else{
            // query annotator
            annotations = queryAnnotator(parameters);
        
            // additional functionalities
            if(annotations.getType()==JSONType.ARRAY){
                
                Scorer scorer = null;
                switch(score){
                    case "old":     scorer = new OldScore(annotations);           break;
                    case "cvalue":  scorer = new CValueScore(annotations, true);  break;
                    case "cvalueh": scorer = new CValueScore(annotations, false); break;
                }
                if(scorer!=null){
                    Map<String, Double> scores = scorer.compute();
                    annotations = scorer.getScoredAnnotations(scores);
                }
                
                if(format.equals("rdf"))
                    annotationsRdfOutput = JsonToRdf.convert(annotations, annotatorURI);
                else if(format.equals("debug"))
                    annotations = Debug.makeDebugAnnotations(annotations);
            }
        }
        
        // process response
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        if (format.equals("rdf")) {
        	response.setContentType("application/rdf+xml;charset=UTF-8");
	        out.println(annotationsRdfOutput);
	        out.flush();
        } else {
            response.setContentType("application/json; charset=UTF-8");
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
        String url = parameters.makeUrl(annotatorURI);
                
        // query annotator
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpResponse httpResponse = null;
        try{
            URI uri = new URI(url);
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
