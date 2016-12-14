package org.sifrproject.annotations.input;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.model.AnnotationFactory;
import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.exceptions.InvalidFormatException;
import org.sifrproject.annotations.model.BioPortalLazyAnnotationTokens;
import org.sifrproject.annotations.model.BioportalLazyHierarchy;
import org.sifrproject.annotations.model.BioportalLazyMappings;
import org.sifrproject.annotations.umls.UMLSGroupIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BioPortalJSONAnnotationParser implements AnnotationParser {

    private final static Logger logger = LoggerFactory.getLogger(BioPortalJSONAnnotationParser.class);

    private final JSONParser parser = new JSONParser();

    private final AnnotationFactory annotationFactory;
    private final PropertyRetriever cuiRetrieval;
    private final PropertyRetriever umlsTypeRetrieval;
    private final UMLSGroupIndex groupIndex;


    public BioPortalJSONAnnotationParser(AnnotationFactory annotationFactory, PropertyRetriever cuiRetrieval,
                                         PropertyRetriever umlsTypeRetrieval, UMLSGroupIndex groupIndex) {
        this.annotationFactory = annotationFactory;
        this.cuiRetrieval = cuiRetrieval;
        this.groupIndex = groupIndex;
        this.umlsTypeRetrieval = umlsTypeRetrieval;
    }

    public BioPortalJSONAnnotationParser(AnnotationFactory annotationFactory) {
        this(annotationFactory, null, null, null);
    }

    @Override
    public List<Annotation> parseAnnotations(String queryResponse) throws ParseException {

        List<Annotation> annotations = new ArrayList<>();
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


                            annotations.add(annotationFactory.createAnnotation(
                                    parseAnnotatedClass(annotatedClassNode),
                                    parseAnnotations(annotationsNode),
                                    parseHierarchy(hierarchyNode), parseMappings(mappingsNode), child));

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
        return annotations;
    }

    private AnnotatedClass parseAnnotatedClass(JSONObject annotatedClassNode) throws InvalidFormatException {
        if (annotatedClassNode != null) {
            Links links = parseLinks((JSONObject) annotatedClassNode.get("links"));

            return annotationFactory.createAnnotatedClass(annotatedClassNode, links, cuiRetrieval, umlsTypeRetrieval, groupIndex);
        } else {
            return null;
        }
    }

    private Links parseLinks(JSONObject linksNode) {
        if (linksNode != null) {
            return annotationFactory.createLinks(
                    parseLinkMetadata(linksNode),
                    parseLinkMetadata(linksNode), linksNode);
        } else {
            return null;
        }
    }

    private LinkMetadata parseLinkMetadata(JSONObject metadataNode) {
        return annotationFactory.createLinkMetadata(metadataNode);
    }

    private AnnotationTokens parseAnnotations(JSONArray annotationsNode) {

        List<AnnotationToken> tokens = new ArrayList<>();
        for (Object tokenNodeObject : annotationsNode) {
            JSONObject tokenNode = (JSONObject) tokenNodeObject;
            tokens.add(annotationFactory.createAnnotationToken(tokenNode));
        }
        return new BioPortalLazyAnnotationTokens(tokens, annotationsNode);
    }

    private Hierarchy parseHierarchy(JSONArray hierarchyNode) throws InvalidFormatException {
        if (hierarchyNode != null) {
            List<HierarchyElement> hierarchyElements = new ArrayList<>();
            for (Object hierarchyElementNodeObject : hierarchyNode) {

                JSONObject hierarchyElementNode = (JSONObject) hierarchyElementNodeObject;

                AnnotatedClass annotatedClass = parseAnnotatedClass((JSONObject) hierarchyElementNode.get("annotatedClass"));
                hierarchyElements.add(annotationFactory.createHierarchyElement(annotatedClass, hierarchyElementNode));
            }
            return new BioportalLazyHierarchy(hierarchyElements, hierarchyNode);
        } else {
            return new BioportalLazyHierarchy(Collections.emptyList(), null);
        }
    }

    private Mappings parseMappings(JSONArray mappingsNode) throws InvalidFormatException {
        List<Mapping> mappings = new ArrayList<>();
        for (Object mappingNodeObject : mappingsNode) {
            JSONObject mappingNode = (JSONObject) mappingNodeObject;
            AnnotatedClass annotatedClass = parseAnnotatedClass((JSONObject) mappingNode.get("annotatedClass"));

            mappings.add(annotationFactory.createMapping(annotatedClass, mappingNode));
        }
        return new BioportalLazyMappings(mappings, mappingsNode);
    }

}
