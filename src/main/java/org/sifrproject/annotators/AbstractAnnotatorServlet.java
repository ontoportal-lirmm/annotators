package org.sifrproject.annotators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Implements the core functionalities of the AnnotatorPlus web services:
 *   - It queries the suitable bioportal annotation server (w.r.t implementing subclasses)
 *   - add a "score" functionality that sort output following a scoring method
 *   - add a "format=rbf" to the current possible output formats of bioportal (json, xml, ...)
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
        LinkedHashMap<String, String[]> parameters = UrlUtils.getParameters(request);
        
        String score  = getFirst(parameters.remove("score"), "false").toLowerCase();
        String format = getFirst(parameters.get("format"),   "json" ).toLowerCase();
        
        if(format=="rbf") 
            parameters.put("format", new String[]{"json"});
        
        // query annotator
        List<String> annotations = queryAnnotator(parameters);
        
        // TODO: score
        
        // TODO: format RBF
        
        // process response
        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        for(String line : annotations)
            out.println(line);
        out.flush();
        
    }

    private static String getFirst(String[] values, String defaultValue){
        String value = defaultValue;
        if(values!=null && values.length>0)
            value = values[0];
        return value;
    }
    
    private List<String> queryAnnotator(LinkedHashMap<String, String[]> parameters){
        // make query URL
        String url = getAnnotatorBaseURL();
        UrlUtils.appendParameters(url, parameters);
                
        // query annotator
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpResponse httpResponse = null;
        try{  httpResponse = client.execute(new HttpGet(url));    }
        catch (ClientProtocolException e){  e.printStackTrace();  }
        catch (IOException e){              e.printStackTrace();  }

        
        // process response
        ArrayList<String> response = new ArrayList<>();
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String line;
            while ((line = reader.readLine()) != null)
                response.add(line);
        } 
        catch (IllegalStateException e){    e.printStackTrace();  }
        catch (IOException e){              e.printStackTrace();  }
        
        
        // close http client
        try{                   client.close();        }
        catch(IOException e1){ e1.printStackTrace();  }

        return response;
    }
}
