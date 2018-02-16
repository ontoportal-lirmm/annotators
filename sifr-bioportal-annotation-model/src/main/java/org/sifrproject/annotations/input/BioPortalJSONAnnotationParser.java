package org.sifrproject.annotations.input;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.model.retrieval.UMLSPropertyRetriever;
import org.sifrproject.annotations.exceptions.InvalidFormatException;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;
import org.sifrproject.annotations.model.BioPortalLazyAnnotationTokens;
import org.sifrproject.annotations.model.BioportalLazyHierarchy;
import org.sifrproject.annotations.model.BioportalLazyMappings;
import org.sifrproject.annotations.umls.UMLSGroupIndex;
import org.sifrproject.annotations.umls.UMLSSemanticGroupsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The default BioportalAnnotationParser, creates a lazy annotation model with underlying JSON objects and access-time
 * retrieval.
 */
public class BioPortalJSONAnnotationParser implements AnnotationParser {

    private final static Logger logger = LoggerFactory.getLogger(BioPortalJSONAnnotationParser.class);


    private final AnnotationFactory annotationFactory;
    private final UMLSPropertyRetriever umlsTypeRetrieval;
    private final UMLSGroupIndex groupIndex;


    /**
     * Create an annotation parser
     * @param annotationFactory The factory for annotation elements

     * @param umlsTypeRetrieval The property retriever of UMLS semantic groups (may be null, see second constructor)
     * @param groupIndex The UMLS group index that maps UMLS groups to their semantic types and vice versa
     */
    public BioPortalJSONAnnotationParser(final AnnotationFactory annotationFactory,
                                         final UMLSPropertyRetriever umlsTypeRetrieval, final UMLSGroupIndex groupIndex) {
        this.annotationFactory = annotationFactory;
        this.groupIndex = groupIndex;
        this.umlsTypeRetrieval = umlsTypeRetrieval;
    }

    /**
     * Create an annotation parser
     * @param annotationFactory The factory for annotation elements
     */
    public BioPortalJSONAnnotationParser(final AnnotationFactory annotationFactory) {
        this(annotationFactory, null, UMLSSemanticGroupsLoader.load());
    }

    @Override
    public List<Annotation> parseAnnotations(final String queryResponse) throws ParseException, NCBOAnnotatorErrorException, InvalidFormatException {

        final List<Annotation> annotations = new ArrayList<>();
//        try {
            final JsonValue rootNode = Json.parse(queryResponse);
            if (rootNode != null) {
                    if(rootNode.isObject()){
                        throw new NCBOAnnotatorErrorException(String.format("%s", queryResponse));
                    }
                    for (final JsonValue childObject : rootNode.asArray()) {
                        final JsonObject child = childObject.asObject();
                        final JsonValue annotatedClassNode = child.get("annotatedClass");
                        final JsonValue annotationsNode = child.get("annotations");
                        final JsonValue hierarchyNode = child.get("hierarchy");
                        final JsonValue mappingsNode = child.get("mappings");
                        if ((annotatedClassNode != null) && (annotationsNode != null) && (mappingsNode != null)) {



                            annotations.add(annotationFactory.createAnnotation(
                                    parseAnnotatedClass(annotatedClassNode),
                                    parseAnnotations(annotationsNode.asArray()),
                                    parseHierarchy(hierarchyNode.asArray()), parseMappings(mappingsNode.asArray()), child));

                        } else {
                            throw new InvalidFormatException("Invalid annotation structure, one of annotatedClass, annotations, mappings, missing");
                        }

                    }

            } else {
                logger.error("Output empty!");
            }
        /*} catch (RuntimeException e) {
            logger.error("Invalid JSON syntax:{}", e.getLocalizedMessage());
            throw new NCBOAnnotatorErrorException(String.format("%s", queryResponse));
        }*/
        return annotations;
    }

    private AnnotatedClass parseAnnotatedClass(final JsonValue annotatedClassNode) throws InvalidFormatException {
        if (annotatedClassNode != null) {
            final Links links = parseLinks(annotatedClassNode.asObject().get("links"));

            return annotationFactory.createAnnotatedClass(annotatedClassNode.asObject(), links, umlsTypeRetrieval, groupIndex);
        } else {
            return null;
        }
    }

    private Links parseLinks(final JsonValue linksNode) {
        if (linksNode != null) {
            return annotationFactory.createLinks(
                    parseLinkMetadata(linksNode),
                    parseLinkMetadata(linksNode), linksNode.asObject());
        } else {
            return null;
        }
    }

    private LinkMetadata parseLinkMetadata(final JsonValue metadataNode) {
        return annotationFactory.createLinkMetadata(metadataNode.asObject());
    }

    private AnnotationTokens parseAnnotations(final JsonArray annotationsNode) {

        final List<AnnotationToken> tokens = new ArrayList<>();
        for (final JsonValue tokenNodeObject : annotationsNode) {
            tokens.add(annotationFactory.createAnnotationToken(tokenNodeObject.asObject()));
        }
        return new BioPortalLazyAnnotationTokens(tokens, annotationsNode);
    }

    private Hierarchy parseHierarchy(final JsonArray hierarchyNode) throws InvalidFormatException {
        if (hierarchyNode != null) {
            final List<HierarchyElement> hierarchyElements = new ArrayList<>();
            for (final JsonValue hierarchyElementNodeObject : hierarchyNode) {

                final JsonObject hierarchyElementNode = hierarchyElementNodeObject.asObject();

                final AnnotatedClass annotatedClass = parseAnnotatedClass((hierarchyElementNode.get("annotatedClass")));
                hierarchyElements.add(annotationFactory.createHierarchyElement(annotatedClass, hierarchyElementNode));
            }
            return new BioportalLazyHierarchy(hierarchyElements, hierarchyNode);
        } else {
            return new BioportalLazyHierarchy(Collections.<HierarchyElement>emptyList(), null);
        }
    }

    private Mappings parseMappings(final JsonArray mappingsNode) throws InvalidFormatException {
        final List<Mapping> mappings = new ArrayList<>();
        for (final JsonValue mappingNodeObject : mappingsNode) {
            final JsonObject mappingNode = mappingNodeObject.asObject();
            final AnnotatedClass annotatedClass = parseAnnotatedClass(mappingNode.get("annotatedClass"));

            mappings.add(annotationFactory.createMapping(annotatedClass, mappingNode));
        }
        return new BioportalLazyMappings(mappings, mappingsNode);
    }

}
