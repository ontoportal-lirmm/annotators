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
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED_CHARSET_UTF_8 = "application/x-www-form-urlencoded; charset=UTF-8";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String ACCEPT_HEADER = "Accept";
    public static final String ACCEPTED_MIMES = "text/xml, application/json, text/html, text/plain";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USER_AGENT_HEADER = "User-agent";
    private final String baseURI;
    private final Map<String, String> headers;
    private final HttpServletRequest httpServletRequest;


    public RequestGenerator(final HttpServletRequest request, final String annotatorURI) throws UnsupportedEncodingException {
        this.httpServletRequest = request;
        baseURI = annotatorURI;
        final Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String paramName = parameterNames.nextElement();
            final String paramValue;
            if(paramName.equals("text") && !request.getParameter(paramName).isEmpty()) {
                final String encoding = (httpServletRequest.getMethod().equals("GET")) ? "8859_1" : httpServletRequest.getCharacterEncoding();
                 paramValue = new String(request.getParameter(paramName).getBytes(encoding), "utf-8");
            } else {
                paramValue = request.getParameter(paramName);
            }
            this.put(paramName, paramValue);
        }
        headers = new HashMap<>();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            final String value = request.getHeader(headerName).toLowerCase();
            headers.put(headerName, value);
        }
    }

    @SuppressWarnings("HardcodedFileSeparator")
    HttpURLConnection createRequest() throws IOException {
        String uri = baseURI;
        final String parameterString = createParameterString();
        if (!uri.endsWith("/")) {
            uri += "/";
        }
        if (httpServletRequest.getMethod().equals("GET")) {
            uri += "?" + parameterString;
        } else if (uri.endsWith("?")) {
            uri = uri.substring(0, uri.length() - 1) + "/";
        }

        final URL url = new URL(uri);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(httpServletRequest.getMethod());
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setRequestProperty(ACCEPT_HEADER, ACCEPTED_MIMES);
        transferHeaders(connection);

        if (httpServletRequest.getMethod().equals("POST")) {
            connection.setDoOutput(true);
            connection.setRequestProperty(CONTENT_TYPE,
                    APPLICATION_X_WWW_FORM_URLENCODED_CHARSET_UTF_8);
            connection.setRequestProperty(CONTENT_LENGTH,
                    Integer.toString(parameterString.getBytes().length));
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(parameterString);
                wr.flush();
                wr.close();
            }
        } else {
            connection.setRequestProperty(CONTENT_LENGTH,
                    Integer.toString(0));
        }
        return connection;
    }

    private String createParameterString() throws UnsupportedEncodingException {
        String parameterString = "";
        boolean first = true;
        for (final String paramName : keySet()) {
            if (!first) {
                parameterString += "&";
            }
            first = false;
            parameterString += paramName + "=" + URLEncoder.encode(get(paramName), "UTF-8");
        }
        return parameterString;
    }

    private void transferHeaders(final HttpURLConnection connection) {
        if (headers.containsKey(AUTHORIZATION_HEADER.toLowerCase())) {
            connection.setRequestProperty(AUTHORIZATION_HEADER, headers.get(AUTHORIZATION_HEADER.toLowerCase()));
        }
        if (headers.containsKey(USER_AGENT_HEADER.toLowerCase())) {
            connection.setRequestProperty(USER_AGENT_HEADER, headers.get(USER_AGENT_HEADER.toLowerCase()));
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
