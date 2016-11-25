package org.sifrproject.annotations.model;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sifrproject.annotations.api.BioPortalAnnotation;
import org.sifrproject.annotations.api.BioPortalAnnotationFactory;
import org.sifrproject.annotations.api.BioPortalAnnotationParser;
import org.sifrproject.annotations.api.umls.PropertyRetriever;
import org.sifrproject.annotations.umls.groups.UMLSGroup;
import org.sifrproject.annotations.umls.groups.UMLSGroupIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LIRMMBioportalAnnotationParser implements BioPortalAnnotationParser {

    private final static Logger logger = LoggerFactory.getLogger(LIRMMBioportalAnnotationParser.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private final List<BioPortalAnnotation> annotations;
    private final BioPortalAnnotationFactory annotationFactory;
    private final PropertyRetriever cuiRetrieval;
    private final PropertyRetriever UMLSTypeRetrieval;
    private final UMLSGroupIndex groupIndex;

    public LIRMMBioportalAnnotationParser(BioPortalAnnotationFactory annotationFactory, PropertyRetriever cuiRetrieval, PropertyRetriever UMLSTypeRetrieval, UMLSGroupIndex groupIndex) {
        annotations = new ArrayList<>();
        this.annotationFactory = annotationFactory;
        this.cuiRetrieval = cuiRetrieval;
        this.groupIndex = groupIndex;
        this.UMLSTypeRetrieval = UMLSTypeRetrieval;
    }

    @Override
    public void parseAnnotations(String queryResponse) throws IOException {
        JsonNode rootNode = mapper.readTree(queryResponse);
        if (rootNode != null) {
            for (JsonNode child : rootNode) {
                JsonNode annotatedClass = child.get("annotatedClass");
                String classId = annotatedClass.get("@id").asText();
                String ontology = annotatedClass.get("links").get("ontology").asText();
                JsonNode annotationNode = child.get("annotations");
                double score = 0d;
                if (child.has("score")) {
                    score = child.get("score").asDouble();
                }

                Set<String> mappings = Collections.emptySet();
                if (child.has("mappings")) {
                    mappings = handleMappings(child.get("mappings"));
                }

                handleAnnotations(annotationNode, classId, ontology, score, mappings);
            }
        } else {
            logger.error("Output empty");
        }
    }

    private String cuiString(Iterable<String> cuis) {
        StringBuilder builder = new StringBuilder();

        for (String cui : cuis) {
            builder.append(cui).append(" ");
        }
        return builder.toString().trim();
    }

    private Set<String> handleMappings(JsonNode mappingsNode) {
        Set<String> mappings = new TreeSet<>();
        mappingsNode.forEach(mapping -> mappings.add(mapping.get("annotatedClass").get("@id").asText()));
        return mappings;
    }

    private void handleAnnotations(JsonNode annotationNode, String classId, String ontology, double score, Set<String> mappings) {
        for (JsonNode annotation : annotationNode) {
            int end = annotation.findValue("to").asInt();
            int begin = annotation.findValue("from").asInt() - 1;
            String text = annotation.findValue("text").asText();
            Set<String> cuiset = new HashSet<>();
            cuiset.addAll(cuiRetrieval.retrievePropertyValues(classId));
            mappings.forEach(mapping -> cuiset.addAll(cuiRetrieval.retrievePropertyValues(mapping)));
            String cuis = cuiString(cuiset);

            List<String> types = UMLSTypeRetrieval.retrievePropertyValues(classId);
            Set<UMLSGroup> groups = types.stream().map(groupIndex::getGroupByType).collect(Collectors.toSet());

            if (!groups.isEmpty()) {
                List<BioPortalAnnotation> filtered = annotations.stream().filter(
                        a -> a.getEnd() == end && a.getBegin() == begin).collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    addAnnotations(groups, text, classId, ontology, cuis, score, begin, end);
                } else {
                    List<BioPortalAnnotation> cuifiltered = filtered.stream().filter(a -> !a.getCuis().isEmpty()).collect(Collectors.toList());
                    if (cuifiltered.isEmpty()) {
                        for (BioPortalAnnotation fAnnot : filtered) {
                            annotations.remove(fAnnot);
                        }
                        addAnnotations(groups, text, classId, ontology, cuis, score, begin, end);
                    }
                }
            }
        }

    }

    private void addAnnotations(Set<UMLSGroup> groups, String text, String classId, String ontology, String cuis, double score, int begin, int end) {
        for (UMLSGroup group : groups) {
            annotations.add(annotationFactory.createAnnotation(text, classId, ontology, group.name(), cuis, score, begin, end));
        }
    }

    @Override
    public List<BioPortalAnnotation> annotations() {
        return annotations;
    }
}
