package org.sifrproject.util;

import javax.servlet.http.HttpServletRequest;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Provides static methods to process url used by servlet servlets
 *
 * @author Julien Diener
 */
@SuppressWarnings("HardcodedFileSeparator")
public class POSTRequestGenerator extends LinkedHashMap<String, String> implements RequestGenerator {
    private static final long serialVersionUID = 4351112172500760834L;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED_CHARSET_UTF_8 = "application/x-www-form-urlencoded; charset=UTF-8";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String ACCEPTED_MIMES = "text/xml, application/json, text/html, text/plain";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USER_AGENT_HEADER = "User-agent";
    private static final Pattern NORMALIZE_ENCODING = Pattern.compile("-");
    private final String baseURI;
    private final Map<String, String> headers;
    private final HttpServletRequest httpServletRequest;


    public POSTRequestGenerator(final HttpServletRequest request, final String annotatorURI, final String serverEncoding) throws UnsupportedEncodingException {
        httpServletRequest = request;
        baseURI = annotatorURI;
        final Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String paramName = parameterNames.nextElement();

            /*if(NORMALIZE_ENCODING.matcher(serverEncoding).replaceAll("").toLowerCase().equals("utf8")) {
//                final String encoding = (httpServletRequest.getMethod().equals("GET")) ? "8859_1" : httpServletRequest.getCharacterEncoding();

            } else {
                paramValue = request.getParameter(paramName);
            }*/
            final String paramValue = new String(request
                    .getParameter(paramName)
                    .getBytes(Charset.forName(serverEncoding.toUpperCase())), "utf-8");
            put(paramName, paramValue);
        }
        put("display","cui,semanticType");
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
        final StringBuilder parameterString = new StringBuilder();
        boolean first = true;
        for (final String paramName : keySet()) {
            if (!first) {
                parameterString.append("&");
            }
            first = false;
            parameterString.append(paramName).append("=").append(URLEncoder.encode(get(paramName), "UTF-8"));
        }
        return parameterString.toString();
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
    @Override
    public String getFirst(final String name, final String defaultValue) {
        String value = get(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    @SuppressWarnings("all")
    public POSTRequestGenerator clone() {
        return (POSTRequestGenerator) super.clone();
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
