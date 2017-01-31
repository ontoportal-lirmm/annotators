package org.sifrproject.util;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public final class RestfulQuery {
    private static final Logger logger = LoggerFactory.getLogger(RestfulQuery.class);
    public static String queryAnnotator(UrlParameters parameters, String annotatorURI) throws IOException {
        // make query URL
        String url = parameters.makeUrl(annotatorURI).trim();

        // query servlet
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

            HttpResponse httpResponse;
            try {
                URI uri = new URI(url);
                httpResponse = client.execute(new HttpGet(uri));

            } catch (URISyntaxException | IOException e) {
                return e.getLocalizedMessage();

            }

            // process response

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while(line!=null && !line.isEmpty()){
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                return e.getLocalizedMessage();
            }
        }
    }
}
