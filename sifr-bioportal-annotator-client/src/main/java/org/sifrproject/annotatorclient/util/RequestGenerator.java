package org.sifrproject.annotatorclient.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
    private final String method;


    public RequestGenerator(final String annotatorURI, final String method, final Map<String, String> headers) throws UnsupportedEncodingException {
        baseURI = annotatorURI;
        this.headers = headers;
        this.method = method;
    }

    HttpURLConnection createRequest() throws IOException {
        String uri = baseURI;
        String parameterString = createParameterString();
        if (method.equals("GET")) {
            uri += "?" + parameterString;
        } else if (uri.endsWith("?")) {
            uri = uri.substring(0, uri.length() - 1) + "/";
        } else if (!uri.endsWith("/")){
            uri += "/";
        }

        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setRequestProperty("Accept", "text/xml, application/json, text/html, text/plain");
        transferHeaders(connection);

        if (method.equals("POST")) {
            connection.setDoOutput(true);
            connection.setRequestProperty("ContentType",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Content-Length",
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
        for (final String paramName : this.keySet()) {
            if (!first) {
                parameterString += "&";
            }
            first = false;
            parameterString += paramName + "=" + URLEncoder.encode(get(paramName), "UTF-8");
        }
        return parameterString;
    }

    private void transferHeaders(final HttpURLConnection connection) {
        if (headers.containsKey("authorization")) {
            connection.setRequestProperty("Authorization", headers.get("authorization"));
        }
        if (headers.containsKey("user-agent")) {
            connection.setRequestProperty("User-Agent", headers.get("user-agent"));
        }
    }
}
