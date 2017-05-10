package org.sifrproject.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;

public final class RestfulRequest {
    private static final Logger logger = LoggerFactory.getLogger(RestfulRequest.class);

    @SuppressWarnings("MethodParameterOfConcreteClass")
    public static String queryAnnotator(final RequestGenerator requestGenerator) throws IOException {

        final HttpURLConnection httpURLConnection = requestGenerator.createRequest();
        logger.debug("Request to Annotator: {}", httpURLConnection.getURL());
        final int code = httpURLConnection.getResponseCode();
        final String message = httpURLConnection.getResponseMessage();
        logger.info("{} - {} {}", code, message, httpURLConnection.getURL().toString());
        final String response = streamAsString((code == 200) ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream());
        logger.debug(response);
        return response;
    }

    @SuppressWarnings("HardcodedLineSeparator")
    private static String streamAsString(final InputStream stream) {
        final StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
            String line = bufferedReader.readLine();
            while ((line != null) && !line.isEmpty()) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }

        } catch (final IOException e) {
            stringBuilder.append(e.getLocalizedMessage());
            return e.getLocalizedMessage();
        }
        return stringBuilder.toString();
    }

}
