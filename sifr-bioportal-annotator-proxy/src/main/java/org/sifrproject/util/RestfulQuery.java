package org.sifrproject.util;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public final class RestfulQuery {
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
                Optional<String> fileContents = bufferedReader.lines().reduce(String::concat);
                return fileContents.orElse("Cannot read response");
            } catch (IOException e) {
                return e.getLocalizedMessage();
            }
        }

    }
}
