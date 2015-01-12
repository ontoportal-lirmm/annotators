/**
 * 
 */
package org.sifrproject.annotators;

import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides static methods to process url used by annotator servlets 
 * 
 * @author Julien Diener
 */
public class UrlUtils {

    /**
     * @param {@link HttpServletRequest} request
     * @return Ordered list of URL parameters made from given request parameters
     */
    protected static LinkedHashMap<String,String[]> getParameters(HttpServletRequest request) {
        LinkedHashMap<String,String[]> parameters = new LinkedHashMap<>();
        
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            parameters.put(paramName, paramValues);
        }
        
        return parameters;
    }

    protected static void appendParameters(String baseUrl, LinkedHashMap<String,String[]> parameters) {
        for(String paramName : parameters.keySet()) {
            String[] paramValues = parameters.get(paramName);

            baseUrl += paramName + "=" + paramValues[0];
            for(int i=1; i<paramValues.length; i++)
                baseUrl += "," + paramValues[i];
            baseUrl += "&";
        }
    }
}
