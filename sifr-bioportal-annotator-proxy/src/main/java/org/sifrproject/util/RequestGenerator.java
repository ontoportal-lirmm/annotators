package org.sifrproject.util;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides static methods to process url used by servlet servlets
 * 
 * @author Julien Diener
 */
public class UrlParameters extends LinkedHashMap<String,String[]>{
    private static final long serialVersionUID = 4351112172500760834L;
    private final String baseURI;
    private final Map<String,String[]> headers;

    
    public UrlParameters(HttpServletRequest request){
        baseURI = request.getRequestURI();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            this.put(paramName, paramValues);
        }
        headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String headerName = "";
        while (headerNames.hasMoreElements()){
            headerName = headerNames.nextElement();
            String[] values = request.getParameterValues(headerName);
            headers.put(headerName,values);
        }
    }
    
    String makeGETUrl() {
        String url = baseURI;
        boolean first = true;
        for(String paramName : this.keySet()) {
            if (!first) {
                url += "&";
            }
            first = false;

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
        }
        return url;
    }

    Map<String,String[]> getHeaders(){
        return headers;
    }

    String makePOSTUrl() {
        return baseURI;
    }

    /**
     * Retrieve the first parameter value from the {@code values} array if any, otherwise returns the {@code defaultValue} supplied
     *
     * @param name         The value of the parameter
     * @param defaultValue The default value to return is the first argument is null or empty
     * @return The parameter or the default value
     */
    public String getFirst(String name, String defaultValue) {
        String[] values = get(name);
        String value = defaultValue;
        if (values != null && values.length > 0)
            value = values[0];
        return value;
    }
}
