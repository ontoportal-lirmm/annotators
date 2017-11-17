package org.sifrproject.annotations.model.retrieval;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.sifrproject.annotations.api.model.retrieval.UMLSProperties;
import org.sifrproject.annotations.api.model.retrieval.UMLSPropertyRetriever;
import org.sifrproject.util.RestfulRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class APIUMLSPropertyRetriever implements UMLSPropertyRetriever {
    private static final Logger logger = LoggerFactory.getLogger(APIUMLSPropertyRetriever.class);

    private final String apikey;

    public APIUMLSPropertyRetriever(final String apikey) throws IOException {
        this.apikey = apikey;
    }

    @Override
    public UMLSProperties retrievePropertyValues(final String URI) {
        final List<String> tuis = new ArrayList<>();
        final List<String> cuis = new ArrayList<>();

        try {
            final String output = RestfulRequest.queryClass(String.format("%s?apikey=%s",URI,apikey));
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
