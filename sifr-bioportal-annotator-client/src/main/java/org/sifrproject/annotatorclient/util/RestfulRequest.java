package org.sifrproject.annotatorclient.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public final class RestfulRequest {
    private static final Logger logger = LoggerFactory.getLogger(RestfulRequest.class);

    public static String queryAnnotator(RequestGenerator requestGenerator) throws IOException {

        HttpURLConnection httpURLConnection = requestGenerator.createRequest();
        logger.info("Request to NCBO Annotator: {}",httpURLConnection.getURL());
        int code = httpURLConnection.getResponseCode();
        String message = httpURLConnection.getResponseMessage();
        logger.info("{} - {}", code, message);
        String response;
        if(code==400){
            response = streamAsString(httpURLConnection.getErrorStream());
        } else {
            response = streamAsString(httpURLConnection.getInputStream());
        }
        logger.info(response);
        return response;
    }

    private static String streamAsString(InputStream stream){
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream,"UTF-8"))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty()) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }
    }

}
