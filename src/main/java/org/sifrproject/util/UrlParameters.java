/**
 * 
 */
package org.sifrproject.util;

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

            url += paramName + "=" + paramValues[0];
            for(int i=1; i<paramValues.length; i++)
                url += "," + paramValues[i];
            url += "&";
        }
        return url;
    }
}
