package org.sifrproject.annotations.model.lazy;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.model.lazy.LazyAnnotationFactory;
import org.sifrproject.annotations.api.umls.PropertyRetriever;
import org.sifrproject.annotations.umls.groups.UMLSGroupIndex;

import java.util.List;

public class BioPortalLazyAnnotationFactory implements LazyAnnotationFactory {


    @Override
    public Annotation createAnnotation(AnnotatedClass annotatedClass, AnnotationTokens annotations, Hierarchy hierarchy, Mappings mappings, JSONObject jsonObject) {
        return new BioPortalLazyAnnotation(annotatedClass, hierarchy, annotations, mappings, jsonObject);
    }

    @Override
    public AnnotatedClass createAnnotatedClass(JSONObject jsonObject, Links links, PropertyRetriever cuiPropertyRetriever, PropertyRetriever semanticGroupPropertyRetriever, UMLSGroupIndex groupIndex) {
        return new BioPortalLazyAnnotatedClass(jsonObject, links, cuiPropertyRetriever, semanticGroupPropertyRetriever, groupIndex);
    }

    @Override
    public AnnotatedClass createAnnotatedClass(JSONObject jsonObject, Links links) {
        return new BioPortalLazyAnnotatedClass(jsonObject, links);
    }

    @Override
    public AnnotationToken createAnnotationToken(JSONObject jsonObject) {
        return new BioPortalLazyAnnotationToken(jsonObject);
    }

    @Override
    public AnnotationTokens createAnnotationTokens(List<AnnotationToken> annotationTokens, JSONArray jsonObject) {
        return new BioPortalLazyAnnotationTokens(annotationTokens, jsonObject);
    }

    @Override
    public HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, JSONObject jsonObject) {
        return new BioPortalLazyHierarchyElement(annotatedClass, jsonObject);
    }

    @Override
    public HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, int distance) {
        return null;
    }

    @Override
    public Links createLinks(LinkMetadata linksMetadata, LinkMetadata linksContextMetadata, JSONObject jsonObject) {
        return new BioPortalLazyLinks(linksMetadata, linksContextMetadata, jsonObject);
    }

    @Override
    public LinkMetadata createLinkMetadata(JSONObject jsonObject) {
        return new BioportalLazyLinkContext(jsonObject);
    }

    @Override
    public Mapping createMapping(AnnotatedClass annotatedClass, JSONObject jsonObject) {
        return new BioPortalLazyMapping(annotatedClass, jsonObject);
    }

    @Override
    public Mappings createMappings(List<Mapping> mappings, JSONArray jsonObject) {
        return new BioportalLazyMappings(mappings, jsonObject);
    }
}
