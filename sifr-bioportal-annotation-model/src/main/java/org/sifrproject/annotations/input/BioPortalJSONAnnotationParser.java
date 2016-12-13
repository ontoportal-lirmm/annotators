package org.sifrproject.annotations.input;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.umls.PropertyRetriever;
import org.sifrproject.annotations.exceptions.InvalidFormatException;
import org.sifrproject.annotations.umls.groups.UMLSGroup;
import org.sifrproject.annotations.umls.groups.UMLSGroupIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class BioPortalJSONAnnotationParser implements AnnotationParser {

    private final static Logger logger = LoggerFactory.getLogger(BioPortalJSONAnnotationParser.class);

    private final JSONParser parser = new JSONParser();

    private final List<Annotation> annotations;
    private final AnnotationFactory annotationFactory;
    private final PropertyRetriever cuiRetrieval;
    private final PropertyRetriever UMLSTypeRetrieval;
    private final UMLSGroupIndex groupIndex;


    public BioPortalJSONAnnotationParser(AnnotationFactory annotationFactory, PropertyRetriever cuiRetrieval,
                                         PropertyRetriever UMLSTypeRetrieval, UMLSGroupIndex groupIndex) {
        annotations = new ArrayList<>();
        this.annotationFactory = annotationFactory;
        this.cuiRetrieval = cuiRetrieval;
        this.groupIndex = groupIndex;
        this.UMLSTypeRetrieval = UMLSTypeRetrieval;
    }

    public BioPortalJSONAnnotationParser(AnnotationFactory annotationFactory) {
        this(annotationFactory, null, null, null);
    }

    @Override
    public void parseAnnotations(String queryResponse) throws ParseException {

        try {
            JSONArray rootNode = (JSONArray) parser.parse(queryResponse);

            if (rootNode != null) {
                try {
                    for (Object childObject : rootNode) {
                        JSONObject child = (JSONObject) childObject;
                        if (child.containsKey("annotatedClass") && child.containsKey("annotations") && child.containsKey("mappings")) {
                            JSONObject annotatedClassNode = (JSONObject) child.get("annotatedClass");
                            JSONArray annotationsNode = (JSONArray) child.get("annotations");
                            JSONArray hierarchyNode = (JSONArray) child.get("hierarchy");
                            JSONArray mappingsNode = (JSONArray) child.get("mappings");

                            double score = -1;
                            if (child.containsKey("score")) {
                                score = Double.valueOf((String) child.get("score"));
                            }

                            this.annotations.add(annotationFactory.createAnnotation(
                                    parseAnnotatedClass(annotatedClassNode),
                                    parseAnnotations(annotationsNode),
                                    parseHierarchy(hierarchyNode), parseMappings(mappingsNode), score));

                        } else {
                            throw new InvalidFormatException("Invalid annotation structure, one of annotatedClass, annotations, mappings, missing");
                        }

                    }
                } catch (InvalidFormatException e) {
                    logger.error(e.getLocalizedMessage());
                }
            } else {
                logger.error("Output empty!");
            }
        } catch (ParseException e) {
            logger.error("Invalid JSON syntax:{}", e.getLocalizedMessage());
        }
    }


    private AnnotatedClass parseAnnotatedClass(JSONObject annotatedClassNode) throws InvalidFormatException {
        if (annotatedClassNode != null && annotatedClassNode.containsKey("@id") && annotatedClassNode.containsKey("@type")) {
            String classId = (String) annotatedClassNode.get("@id");
            String type = (String) annotatedClassNode.get("@type");
            if (annotatedClassNode.containsKey("@context") &&
                    ((JSONObject) annotatedClassNode.get("@context")).containsKey("@vocab")) {
                String contextVocab = (String) ((JSONObject) annotatedClassNode.get("@context")).get("@vocab");
                Links links = parseLinks((JSONObject) annotatedClassNode.get("links"));

                if (links != null) {

                    Set<String> cuiset = new HashSet<>();
                    Set<UMLSGroup> groups = new HashSet<>();

                    cuiset.addAll(cuiRetrieval.retrievePropertyValues(classId));
                    List<String> types = UMLSTypeRetrieval.retrievePropertyValues(classId);
                    groups = types.stream().map(groupIndex::getGroupByType).collect(Collectors.toSet());

                    return annotationFactory.createAnnotatedClass(classId, type, links, contextVocab, cuiset, groups);
                } else {
                    throw new InvalidFormatException("links property of annotatedClass is invalid");
                }
            } else {
                throw new InvalidFormatException("context property of annotatedClass is invalid");
            }
        } else {
            throw new InvalidFormatException("annotatedClass property cannot be empty");
        }
    }

    private Links parseLinks(JSONObject linksNode) {
        if (linksNode != null && linksNode.containsKey("@context")) {
            return annotationFactory.createLinks(
                    parseLinkMetadata(linksNode),
                    parseLinkMetadata((JSONObject) linksNode.get("@context")));
        } else {
            return null;
        }
    }

    private LinkMetadata parseLinkMetadata(JSONObject metadataNode) {
        String self = (String) metadataNode.get("self");
        String ontology = (String) metadataNode.get("ontology");
        String children = (String) metadataNode.get("children");
        String parents = (String) metadataNode.get("parents");
        String descendants = (String) metadataNode.get("descendants");
        String ancestors = (String) metadataNode.get("ancestors");
        String instances = (String) metadataNode.get("instances");
        String tree = (String) metadataNode.get("tree");
        String notes = (String) metadataNode.get("descendants");
        String mappings = (String) metadataNode.get("mappings");
        String ui = (String) metadataNode.get("descendants");

        return annotationFactory.createLinkMetadata(self, ontology, children, parents, descendants, ancestors,
                instances, tree, notes, mappings, ui);
    }

    private List<AnnotationToken> parseAnnotations(JSONArray annotationsNode) {
        List<AnnotationToken> tokens = new ArrayList<>();
        for (Object tokenNodeObject : annotationsNode) {
            JSONObject tokenNode = (JSONObject) tokenNodeObject;
            int end = ((Long) tokenNode.get("to")).intValue();
            int begin = ((Long) tokenNode.get("from")).intValue() - 1;
            String text = (String) tokenNode.get("text");
            String matchType = (String) tokenNode.get("matchType");
            tokens.add(annotationFactory.createAnnotationToken(begin, end, matchType, text));
        }
        return tokens;
    }

    private List<HierarchyElement> parseHierarchy(JSONArray hierarchyNode) throws InvalidFormatException {
        if (hierarchyNode != null) {
            List<HierarchyElement> hierarchyElements = new ArrayList<>();
            for (Object hierarchyElementNodeObject : hierarchyNode) {

                JSONObject hierarchyElementNode = (JSONObject) hierarchyElementNodeObject;

                AnnotatedClass annotatedClass = parseAnnotatedClass((JSONObject) hierarchyElementNode.get("annotatedClass"));
                if (!hierarchyElementNode.containsKey("distance")) {
                    throw new InvalidFormatException("Distance attribute missing from hierarchy");
                }
                int distance = ((Long) hierarchyElementNode.get("distance")).intValue();
                double score = -1;
                if (hierarchyElementNode.containsKey("score")) {
                    score = Double.valueOf((String) hierarchyElementNode.get("score"));
                }
                hierarchyElements.add(annotationFactory.createHierarchyElement(annotatedClass, distance, score));
            }
            return hierarchyElements;
        } else {
            return Collections.emptyList();
        }
    }

    private List<Mapping> parseMappings(JSONArray mappingsNode) throws InvalidFormatException {
        List<Mapping> mappings = new ArrayList<>();
        for (Object mappingNodeObject : mappingsNode) {
            JSONObject mappingNode = (JSONObject) mappingNodeObject;
            AnnotatedClass annotatedClass = parseAnnotatedClass((JSONObject) mappingNode.get("annotatedClass"));
            double score = -1;
            if (mappingNode.containsKey("score")) {
                score = Double.valueOf((String) mappingNode.get("score"));
            }
            mappings.add(annotationFactory.createMapping(annotatedClass, score));
        }
        return mappings;
    }


    /*private String cuiString(Iterable<String> cuis) {
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
    }*/

    /*private void handleAnnotations(JsonNode annotationNode, String classId, String ontology, double score, Set<String> mappings) {
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
                List<Annotation> filtered = annotations.stream().filter(
                        a -> a.getEnd() == end && a.getBegin() == begin).collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    addAnnotations(groups, text, classId, ontology, cuis, score, begin, end);
                } else {
                    List<Annotation> cuifiltered = filtered.stream().filter(a -> !a.getCuis().isEmpty()).collect(Collectors.toList());
                    if (cuifiltered.isEmpty()) {
                        for (Annotation fAnnot : filtered) {
                            annotations.remove(fAnnot);
                        }
                        addAnnotations(groups, text, classId, ontology, cuis, score, begin, end);
                    }
                }
            }
        }
    }*/

    @Override
    public List<Annotation> annotations() {
        List<Annotation> outputList = new ArrayList<>(annotations.size());
        outputList.addAll(annotations);
        return outputList;
    }

    @Override
    public void clear() {
        annotations.clear();
    }
}
