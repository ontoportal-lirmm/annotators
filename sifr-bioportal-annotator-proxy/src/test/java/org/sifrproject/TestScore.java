package org.sifrproject;

import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationFactory;
import org.sifrproject.annotations.api.model.ScoreableElement;
import org.sifrproject.annotations.input.BioPortalJSONAnnotationParser;
import org.sifrproject.annotations.model.BioPortalLazyAnnotationFactory;
import org.sifrproject.scoring.CValueScore;
import org.sifrproject.scoring.OldScore;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TestScore {


    private final String annotationsText = "[{\"annotatedClass\":{\"@id\":\"http://purl.obolibrary.org/obo/DOID_1909\",\"@type\":\"http://www.w3.org/2002/07/owl#Class\",\"links\":{\"self\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909\",\"ontology\":\"http://data.bioontology.org/ontologies/DOID\",\"children\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/children\",\"parents\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/parents\",\"descendants\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/descendants\",\"ancestors\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/ancestors\",\"tree\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/tree\",\"notes\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/notes\",\"mappings\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909/mappings\",\"ui\":\"http://bioportal.bioontology.org/ontologies/DOID?p=classes&conceptid=http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_1909\",\"@context\":{\"self\":\"http://www.w3.org/2002/07/owl#Class\",\"ontology\":\"http://data.bioontology.org/metadata/Ontology\",\"children\":\"http://www.w3.org/2002/07/owl#Class\",\"parents\":\"http://www.w3.org/2002/07/owl#Class\",\"descendants\":\"http://www.w3.org/2002/07/owl#Class\",\"ancestors\":\"http://www.w3.org/2002/07/owl#Class\",\"tree\":\"http://www.w3.org/2002/07/owl#Class\",\"notes\":\"http://data.bioontology.org/metadata/Note\",\"mappings\":\"http://data.bioontology.org/metadata/Mapping\",\"ui\":\"http://www.w3.org/2002/07/owl#Class\"}},\"@context\":{\"@vocab\":\"http://data.bioontology.org/metadata/\"}},\"hierarchy\":[],\"annotations\":[{\"from\":1,\"to\":8,\"matchType\":\"PREF\",\"text\":\"MELANOMA\"},{\"from\":10,\"to\":17,\"matchType\":\"PREF\",\"text\":\"MELANOMA\"},{\"from\":24,\"to\":31,\"matchType\":\"PREF\",\"text\":\"MELANOMA\"},{\"from\":38,\"to\":45,\"matchType\":\"PREF\",\"text\":\"MELANOMA\"},{\"from\":52,\"to\":59,\"matchType\":\"PREF\",\"text\":\"MELANOMA\"}],\"mappings\":[]},{\"annotatedClass\":{\"@id\":\"http://purl.obolibrary.org/obo/DOID_8923\",\"@type\":\"http://www.w3.org/2002/07/owl#Class\",\"links\":{\"self\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923\",\"ontology\":\"http://data.bioontology.org/ontologies/DOID\",\"children\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/children\",\"parents\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/parents\",\"descendants\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/descendants\",\"ancestors\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/ancestors\",\"tree\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/tree\",\"notes\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/notes\",\"mappings\":\"http://data.bioontology.org/ontologies/DOID/classes/http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923/mappings\",\"ui\":\"http://bioportal.bioontology.org/ontologies/DOID?p=classes&conceptid=http%3A%2F%2Fpurl.obolibrary.org%2Fobo%2FDOID_8923\",\"@context\":{\"self\":\"http://www.w3.org/2002/07/owl#Class\",\"ontology\":\"http://data.bioontology.org/metadata/Ontology\",\"children\":\"http://www.w3.org/2002/07/owl#Class\",\"parents\":\"http://www.w3.org/2002/07/owl#Class\",\"descendants\":\"http://www.w3.org/2002/07/owl#Class\",\"ancestors\":\"http://www.w3.org/2002/07/owl#Class\",\"tree\":\"http://www.w3.org/2002/07/owl#Class\",\"notes\":\"http://data.bioontology.org/metadata/Note\",\"mappings\":\"http://data.bioontology.org/metadata/Mapping\",\"ui\":\"http://www.w3.org/2002/07/owl#Class\"}},\"@context\":{\"@vocab\":\"http://data.bioontology.org/metadata/\"}},\"hierarchy\":[],\"annotations\":[{\"from\":19,\"to\":31,\"matchType\":\"PREF\",\"text\":\"SKIN MELANOMA\"},{\"from\":33,\"to\":45,\"matchType\":\"PREF\",\"text\":\"SKIN MELANOMA\"},{\"from\":47,\"to\":59,\"matchType\":\"PREF\",\"text\":\"SKIN MELANOMA\"}],\"mappings\":[]}]";
    private final String id1 = "http://purl.obolibrary.org/obo/DOID_1909";
    private final String id2 = "http://purl.obolibrary.org/obo/DOID_8923";
    private final AnnotationFactory factory = new BioPortalLazyAnnotationFactory();
    private final AnnotationParser annotationParser = new BioPortalJSONAnnotationParser(factory);

    @Test
    public void testOldScore() throws ParseException {
        List<Annotation> annotations = annotationParser.parseAnnotations(annotationsText);
        Map<String, ScoreableElement> scores = new OldScore().compute(annotations);
        assertTrue("missing class: " + id1, scores.containsKey(id1));
        assertTrue("missing class: " + id2, scores.containsKey(id2));
        assert 50. == scores.get(id1).getScore();
        assert 30. == scores.get(id2).getScore();
    }

    @Test
    public void testCValueScore() throws ParseException {
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
