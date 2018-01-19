package org.sifrproject.annotations.model.retrieval;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.sifrproject.annotations.api.model.retrieval.UMLSProperties;
import org.sifrproject.annotations.api.model.retrieval.UMLSPropertyRetriever;
import org.sifrproject.util.RestfulRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("HardcodedFileSeparator")
public final class APIUMLSPropertyRetriever implements UMLSPropertyRetriever {
    private static final Logger logger = LoggerFactory.getLogger(APIUMLSPropertyRetriever.class);

    @SuppressWarnings("HardcodedFileSeparator")
    private static final char URI_PATH_SEPARATOR = '/';


    private final String apikey;
    private final String ontologiesApiURI;

    public APIUMLSPropertyRetriever(final String ontologiesApiURI,final String apikey) throws IOException {
        this.apikey = apikey;
        this.ontologiesApiURI = ontologiesApiURI;
    }

    @Override
    public UMLSProperties retrievePropertyValues(final String URI) {
        final List<String> tuis = new ArrayList<>();
        final List<String> cuis = new ArrayList<>();

        try {

            final String uriPath = new URL(URI).getPath();

            final String QS = ((apikey == null) || apikey.isEmpty()) ?
                    String.format("%s%s", ontologiesApiURI, uriPath) :
                    String.format("%s%s?apikey=%s", ontologiesApiURI, uriPath, apikey);
            final String output = RestfulRequest.queryClass(QS);
            final JsonValue rootNode = Json.parse(output);


            final JsonValue tuiArray = rootNode
                    .asObject()
                    .get("semanticType");
            if (tuiArray != null) {
                for (final JsonValue value : tuiArray
                        .asArray()) {
                    tuis.add(value.asString());
                }
            }

            final JsonValue cuiArray = rootNode
                    .asObject()
                    .get("cui");
            if (cuiArray != null) {
                for (final JsonValue value : cuiArray
                        .asArray()) {
                    cuis.add(value.asString());
                }
            }

        } catch (final MalformedURLException e) {
            logger.error("Invalid URI {}", e.getLocalizedMessage());
        } catch (final IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        return new MyUMLSProperties(tuis, cuis);
    }

    private static final class MyUMLSProperties implements UMLSProperties {
        private final List<String> tuis;
        private final List<String> cuis;

        private MyUMLSProperties(final List<String> tuis, final List<String> cuis) {
            this.tuis = tuis;
            this.cuis = cuis;
        }

        @Override
        public List<String> getCUIs() {
            return Collections.unmodifiableList(cuis);
        }

        @Override
        public List<String> getTUIs() {
            return Collections.unmodifiableList(tuis);
        }
    }
}
