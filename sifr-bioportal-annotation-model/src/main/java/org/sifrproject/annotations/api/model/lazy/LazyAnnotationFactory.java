package org.sifrproject.annotations.api.model.lazy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.umls.PropertyRetriever;
import org.sifrproject.annotations.umls.groups.UMLSGroupIndex;

import java.util.List;

/**
 * An abstract factory interface to create {@code BioportalAnnotation} instances
 */
public interface LazyAnnotationFactory {
    /**
     * Abstract factory method to create a {@code BioportalAnnotation} instance
     *
     * @param annotatedClass An instance of AnnotatedClass containing the annotation meta information
     * @param annotations    The list of {@code TokenAnnotation} instances containing the delimitation of matching tokens, their type type and the text covered
     * @param hierarchy      The list of classes belonging to the hierarchy of the annotated class
     * @param jsonObject     The underlying JSON model object
     * @return An appropriate instance of {@code Annotation}
     */
    Annotation createAnnotation(AnnotatedClass annotatedClass, AnnotationTokens annotations, Hierarchy hierarchy, Mappings mappings, JSONObject jsonObject);


    AnnotatedClass createAnnotatedClass(JSONObject jsonObject, Links links, PropertyRetriever cuiRetriever, PropertyRetriever semanticGroupRetriever, UMLSGroupIndex umlsGroupIndex);

    AnnotatedClass createAnnotatedClass(JSONObject jsonObject, Links links);

    /**
     * Creates an instance of {@code {@link AnnotationToken}}
     *
     * @param jsonObject The underlying JSON model object
     * @return the {@code {@link AnnotationToken}} instance
     */
    AnnotationToken createAnnotationToken(JSONObject jsonObject);

    /**
     * Creates an instance of {@code {@link AnnotationTokens}}
     *
     * @param jsonObject The underlying JSON model object
     * @return the {@code {@link AnnotationToken}} instance
     */
    AnnotationTokens createAnnotationTokens(List<AnnotationToken> annotationTokens, JSONArray jsonObject);


    /**
     * Creates an instance of {@code {@link HierarchyElement}}
     *
     * @param annotatedClass The annotated class describing the hierarchy element
     * @param jsonObject     The underlying JSON model object
     * @return The instance of {@code {@link HierarchyElement}}
     */
    HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, JSONObject jsonObject);

    /**
     * Creates an instance of {@code {@link HierarchyElement}}
     *
     * @param annotatedClass The annotated class describing the hierarchy element
     * @param distance       The distance in the hierarchy from the annotated concept
     * @return The instance of {@code {@link HierarchyElement}}
     */
    HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, int distance);


    Links createLinks(LinkMetadata linksMetadata, LinkMetadata linksContextMetadata, JSONObject jsonObject);

    LinkMetadata createLinkMetadata(JSONObject jsonObject);


    /**
     * Adds a mapping to the referenced annotated class
     *
     * @param annotatedClass The annotated class corresponding to the destination of the mapping.
     * @param jsonObject     The underlying JSON model object
     * @return The {@code {@link Mapping}} instance
     */
    Mapping createMapping(AnnotatedClass annotatedClass, JSONObject jsonObject);

    /**
     * Adds a mapping to the referenced annotated class
     *
     * @param jsonObject The underlying JSON model object
     * @return The {@code {@link Mapping}} instance
     */
    Mappings createMappings(List<Mapping> mappings, JSONArray jsonObject);

}
