/**
 * 
 */
package org.sifrproject.util;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides static methods to process url used by annotator servlets 
 * 
 * @author Julien Diener
 */
public class UrlParameters extends LinkedHashMap<String,String[]>{
    private static final long serialVersionUID = 4351112172500760834L;

    
    public UrlParameters(HttpServletRequest request){
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            this.put(paramName, paramValues);
        }
    }
    
    public String makeUrl(String baseUrl) {
        String url = baseUrl;
        for(String paramName : this.keySet()) {
            String[] paramValues = this.get(paramName);

            if (paramName.equals("text")) {
                // Encode the text to be annotated (avoid error with % and others)
                try {
                    url += paramName + "=" + URLEncoder.encode(paramValues[0], "UTF-8");
                    for(int i=1; i<paramValues.length; i++)
                        url += "," + URLEncoder.encode(paramValues[i], "UTF-8");
                } catch (Exception e){
                    url += paramName + "=" + paramValues[0];
                    for(int i=1; i<paramValues.length; i++)
                        url += "," + paramValues[i];
                }
            } else {
                url += paramName + "=" + paramValues[0];
                for(int i=1; i<paramValues.length; i++)
                    url += "," + paramValues[i];
            }
            url += "&";

        }
        return url;
    }
}
