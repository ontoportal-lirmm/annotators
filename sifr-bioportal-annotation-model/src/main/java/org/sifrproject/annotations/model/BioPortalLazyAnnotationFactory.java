package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.model.retrieval.UMLSPropertyRetriever;
import org.sifrproject.annotations.umls.UMLSGroupIndex;

import java.util.List;

/**
 * Default lazy dereference implementation of BioPortalLazyAnnotationFactory. Use this factory to instantiate all
 * lazy implementations of the annotation model.
 */
public class BioPortalLazyAnnotationFactory implements AnnotationFactory {


    @Override
    public Annotation createAnnotation(final AnnotatedClass annotatedClass, final AnnotationTokens annotations, final Hierarchy hierarchy, final Mappings mappings, final JsonObject jsonObject) {
        return new BioPortalLazyAnnotation(annotatedClass, hierarchy, annotations, mappings, jsonObject);
    }

    @Override
    public AnnotatedClass createAnnotatedClass(final JsonObject jsonObject, final Links links, final UMLSGroupIndex groupIndex) {
        return new BioPortalLazyAnnotatedClass(jsonObject, links, groupIndex);
    }

    @Override
    public AnnotatedClass createAnnotatedClass(final JsonObject jsonObject, final Links links) {
        return new BioPortalLazyAnnotatedClass(jsonObject, links);
    }

    @Override
    public AnnotationToken createAnnotationToken(final JsonObject jsonObject) {
        return new BioPortalLazyAnnotationToken(jsonObject);
    }

    @Override
    public AnnotationTokens createAnnotationTokens(final List<AnnotationToken> annotationTokens, final JsonArray jsonObject) {
        return new BioPortalLazyAnnotationTokens(annotationTokens, jsonObject);
    }

    @Override
    public HierarchyElement createHierarchyElement(final AnnotatedClass annotatedClass, final JsonObject jsonObject) {
        return new BioPortalLazyHierarchyElement(annotatedClass, jsonObject);
    }

    @Override
    public HierarchyElement createHierarchyElement(final AnnotatedClass annotatedClass, final int distance) {
        return null;
    }

    @Override
    public Links createLinks(final LinkMetadata linksMetadata, final LinkMetadata linksContextMetadata, final JsonObject jsonObject) {
        return new BioPortalLazyLinks(linksMetadata, linksContextMetadata, jsonObject);
    }

    @Override
    public LinkMetadata createLinkMetadata(final JsonObject jsonObject) {
        return new BioportalLazyLinkContext(jsonObject);
    }

    @Override
    public Mapping createMapping(final AnnotatedClass annotatedClass, final JsonObject jsonObject) {
        return new BioPortalLazyMapping(annotatedClass, jsonObject);
    }

    @Override
    public Mappings createMappings(final List<Mapping> mappings, final JsonArray jsonObject) {
        return new BioportalLazyMappings(mappings, jsonObject);
    }

    @Override
    public Annotation createErrorAnnotation(final String message) {
        return new BioportalErrorAnnotation(message);
    }
}
