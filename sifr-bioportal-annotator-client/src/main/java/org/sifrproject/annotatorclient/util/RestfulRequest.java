package org.sifrproject.annotatorclient.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;

public final class RestfulRequest {
    private static final Logger logger = LoggerFactory.getLogger(RestfulRequest.class);

    public static String queryAnnotator(final RequestGenerator requestGenerator) throws IOException {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        final HttpURLConnection httpURLConnection = requestGenerator.createRequest();
        logger.debug("Request to NCBO Annotator: {}",httpURLConnection.getURL());
        final int code = httpURLConnection.getResponseCode();
        final String message = httpURLConnection.getResponseMessage();
        logger.debug("{} - {}", code, message);
        final String response;
        if(code==400){
            response = streamAsString(httpURLConnection.getErrorStream());
        } else {
            response = streamAsString(httpURLConnection.getInputStream());
        }
        logger.debug(response);
        return response;
    }

    private static String streamAsString(final InputStream stream){
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream,"UTF-8"))) {
            final StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty()) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } catch (final IOException e) {
            return e.getLocalizedMessage();
        }
    }

}
