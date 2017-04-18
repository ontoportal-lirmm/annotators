package org.sifrproject;

import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationFactory;
import org.sifrproject.annotations.api.model.ScoreableElement;
import org.sifrproject.annotations.exceptions.InvalidFormatException;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;
import org.sifrproject.annotations.input.BioPortalJSONAnnotationParser;
import org.sifrproject.annotations.model.BioPortalLazyAnnotationFactory;
import org.sifrproject.scoring.CValueScore;
import org.sifrproject.scoring.OldScore;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TestScore {


    private final String annotationsText = "[\n" +
            "  {\n" +
            "    \"annotatedClass\": {\n" +
            "      \"@id\": \"http://purl.obolibrary.org/obo/DOID_1909\",\n" +
            "      \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "      \"links\": {\n" +
            "        \"self\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909\",\n" +
            "        \"ontology\": \"http://data.bioontology.org/ontologies/DOID\",\n" +
            "        \"children\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/children\",\n" +
            "        \"parents\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/parents\",\n" +
            "        \"descendants\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/descendants\",\n" +
            "        \"ancestors\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/ancestors\",\n" +
            "        \"tree\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/tree\",\n" +
            "        \"notes\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/notes\",\n" +
            "        \"mappings\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/mappings\",\n" +
            "        \"ui\": \"http://bioportal.bioontology.org/ontologies/DOID?p=classes&conceptid=http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909\",\n" +
            "        \"instances\": \"http://bioportal.bioontology.org/ontologies/DOID?p=classes&conceptid=http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909\",\n" +
            "        \"@context\": {\n" +
            "          \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
            "          \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
            "          \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
            "          \"ui\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"instances\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
            "        }\n" +
            "      }\n" +
            "    },\n" +
            "    \"@context\": {\n" +
            "      \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
            "    },\n" +
            "    \"hierarchy\": [],\n" +
            "    \"annotations\": [\n" +
            "      {\n" +
            "        \"from\": 1,\n" +
            "        \"to\": 8,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"MELANOMA\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"from\": 10,\n" +
            "        \"to\": 17,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"MELANOMA\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"from\": 24,\n" +
            "        \"to\": 31,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"MELANOMA\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"from\": 38,\n" +
            "        \"to\": 45,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"MELANOMA\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"from\": 52,\n" +
            "        \"to\": 59,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"MELANOMA\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"mappings\": []\n" +
            "  },\n" +
            "  {\n" +
            "    \"annotatedClass\": {\n" +
            "      \"@id\": \"http://purl.obolibrary.org/obo/DOID_8923\",\n" +
            "      \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "      \"links\": {\n" +
            "        \"self\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923\",\n" +
            "        \"ontology\": \"http://data.bioontology.org/ontologies/DOID\",\n" +
            "        \"instances\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "        \"children\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/children\",\n" +
            "        \"parents\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/parents\",\n" +
            "        \"descendants\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/descendants\",\n" +
            "        \"ancestors\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/ancestors\",\n" +
            "        \"tree\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/tree\",\n" +
            "        \"notes\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/notes\",\n" +
            "        \"mappings\": \"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/mappings\",\n" +
            "        \"ui\": \"http://bioportal.bioontology.org/ontologies/DOID?p=classes&conceptid=http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923\",\n" +
            "        \"@context\": {\n" +
            "          \"instances:\": \"http://stuff\",\n" +
            "          \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
            "          \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
            "          \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
            "          \"ui\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
            "        }\n" +
            "      }\n" +
            "    },\n" +
            "    \"@context\": {\n" +
            "      \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
            "    },\n" +
            "    \"hierarchy\": [],\n" +
            "    \"annotations\": [\n" +
            "      {\n" +
            "        \"from\": 19,\n" +
            "        \"to\": 31,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"SKIN MELANOMA\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"from\": 33,\n" +
            "        \"to\": 45,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"SKIN MELANOMA\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"from\": 47,\n" +
            "        \"to\": 59,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"SKIN MELANOMA\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"mappings\": []\n" +
            "  }\n" +
            "]";
    private final String id1 = "http://purl.obolibrary.org/obo/DOID_1909";
    private final String id2 = "http://purl.obolibrary.org/obo/DOID_8923";
    private final AnnotationFactory factory = new BioPortalLazyAnnotationFactory();
    private final AnnotationParser annotationParser = new BioPortalJSONAnnotationParser(factory);

    @Test
    public void testOldScore() throws ParseException, NCBOAnnotatorErrorException, InvalidFormatException {
        List<Annotation> annotations = annotationParser.parseAnnotations(annotationsText);
        Map<String, ScoreableElement> scores = new OldScore().compute(annotations);
        assertTrue("missing class: " + id1, scores.containsKey(id1));
        assertTrue("missing class: " + id2, scores.containsKey(id2));
        assert 50. == scores.get(id1).getScore();
        assert 30. == scores.get(id2).getScore();
    }

    @Test
    public void testCValueScore() throws ParseException, NCBOAnnotatorErrorException, InvalidFormatException {
        annotationParser.parseAnnotations(annotationsText);
        List<Annotation> annotations = annotationParser.parseAnnotations(annotationsText);
        Map<String, ScoreableElement> scores = new CValueScore(true).compute(annotations);
        assertTrue("missing class: " + id1, scores.containsKey(id1));
        assertTrue("missing class: " + id2, scores.containsKey(id2));
        assert equalWithin(5.643856189774724, scores.get(id1).getScore(), 0.001);
        assert equalWithin(14.720671786825555, scores.get(id2).getScore(), 0.001);
    }


    @SuppressWarnings("all")
    private boolean equalWithin(double a, double b, double delta) {
        return a - b < delta;
    }
}
