package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.umls.UMLSGroupIndex;

import java.util.List;

/**
 * Default lazy dereference implementation of BioPortalLazyAnnotationFactory. Use this factory to instantiate all
 * lazy implementations of the annotation model.
 */
public class BioPortalLazyAnnotationFactory implements AnnotationFactory {


    @Override
    public Annotation createAnnotation(AnnotatedClass annotatedClass, AnnotationTokens annotations, Hierarchy hierarchy, Mappings mappings, JsonObject jsonObject) {
        return new BioPortalLazyAnnotation(annotatedClass, hierarchy, annotations, mappings, jsonObject);
    }

    @Override
    public AnnotatedClass createAnnotatedClass(JsonObject jsonObject, Links links, PropertyRetriever cuiPropertyRetriever, PropertyRetriever semanticGroupPropertyRetriever, UMLSGroupIndex groupIndex) {
        return new BioPortalLazyAnnotatedClass(jsonObject, links, cuiPropertyRetriever, semanticGroupPropertyRetriever, groupIndex);
    }

    @Override
    public AnnotatedClass createAnnotatedClass(JsonObject jsonObject, Links links) {
        return new BioPortalLazyAnnotatedClass(jsonObject, links);
    }

    @Override
    public AnnotationToken createAnnotationToken(JsonObject jsonObject) {
        return new BioPortalLazyAnnotationToken(jsonObject);
    }

    @Override
    public AnnotationTokens createAnnotationTokens(List<AnnotationToken> annotationTokens, JsonArray jsonObject) {
        return new BioPortalLazyAnnotationTokens(annotationTokens, jsonObject);
    }

    @Override
    public HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, JsonObject jsonObject) {
        return new BioPortalLazyHierarchyElement(annotatedClass, jsonObject);
    }

    @Override
    public HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, int distance) {
        return null;
    }

    @Override
    public Links createLinks(LinkMetadata linksMetadata, LinkMetadata linksContextMetadata, JsonObject jsonObject) {
        return new BioPortalLazyLinks(linksMetadata, linksContextMetadata, jsonObject);
    }

    @Override
    public LinkMetadata createLinkMetadata(JsonObject jsonObject) {
        return new BioportalLazyLinkContext(jsonObject);
    }

    @Override
    public Mapping createMapping(AnnotatedClass annotatedClass, JsonObject jsonObject) {
        return new BioPortalLazyMapping(annotatedClass, jsonObject);
    }

    @Override
    public Mappings createMappings(List<Mapping> mappings, JsonArray jsonObject) {
        return new BioportalLazyMappings(mappings, jsonObject);
    }

    @Override
    public Annotation createErrorAnnotation(String message) {
        return new BioportalErrorAnnotation(message);
    }
}
