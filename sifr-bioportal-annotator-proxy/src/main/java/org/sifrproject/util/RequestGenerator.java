package org.sifrproject.util;

import javax.servlet.http.HttpServletRequest;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class RequestGenerator extends LinkedHashMap<String, String> {
    private static final long serialVersionUID = 4351112172500760834L;
    private final String baseURI;
    private final Map<String, String> headers;
    private final HttpServletRequest httpServletRequest;


    public RequestGenerator(HttpServletRequest request, String annotatorURI) throws UnsupportedEncodingException {
        this.httpServletRequest = request;
        baseURI = annotatorURI;
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue;
//            String encoding = (httpServletRequest.getMethod().equals("GET")) ? "8859_1" : httpServletRequest.getCharacterEncoding();
//            paramValue = new String(request.getParameter(paramName).getBytes(encoding), "utf-8");
            paramValue = request.getParameter(paramName);
            this.put(paramName, paramValue);
        }
        headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String headerName;
        while (headerNames.hasMoreElements()) {
            headerName = headerNames.nextElement();
            String value = request.getHeader(headerName).toLowerCase();
            headers.put(headerName, value);
        }
    }

    HttpURLConnection createRequest() throws IOException {
        String uri = baseURI;
        String parameterString = createParameterString();
        if (!uri.endsWith("/")) {
            uri += "/";
        }
        if (httpServletRequest.getMethod().equals("GET")) {
            uri += "?" + parameterString;
        } else if (uri.endsWith("?")) {
            uri = uri.substring(0, uri.length() - 1) + "/";
        }

        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(httpServletRequest.getMethod());
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "text/xml, application/json, text/html, text/plain");
        transferHeaders(connection);

        if (httpServletRequest.getMethod().equals("POST")) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(parameterString.getBytes().length));
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parameterString);
            wr.flush();
            wr.close();
        } else {
            connection.setRequestProperty("Content-Length",
                    Integer.toString(0));
        }
        return connection;
    }

    private String createParameterString() throws UnsupportedEncodingException {
        String parameterString = "";
        boolean first = true;
        for (String paramName : this.keySet()) {
            if (!first) {
                parameterString += "&";
            }
            first = false;
            parameterString += paramName + "=" + URLEncoder.encode(get(paramName), "UTF-8");
        }
        return parameterString;
    }

    private void transferHeaders(HttpURLConnection connection) {
        if (headers.containsKey("authorization")) {
            connection.setRequestProperty("Authorization", headers.get("authorization"));
        }
        if (headers.containsKey("user-agent")) {
            connection.setRequestProperty("User-agent", headers.get("user-agent"));
        }
    }

    /**
     * Retrieve the first parameter value from the {@code values} array if any, otherwise returns the {@code defaultValue} supplied
     *
     * @param name         The value of the parameter
     * @param defaultValue The default value to return is the first argument is null or empty
     * @return The parameter or the default value
     */
    public String getFirst(String name, String defaultValue) {
        String value = get(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }
}
