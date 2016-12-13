package org.sifrproject.annotations.model.full;


import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.umls.groups.UMLSGroup;

import java.util.List;
import java.util.Set;

public class BioPortalAnnotationFactory implements AnnotationFactory {

    @Override
    public Annotation createAnnotation(AnnotatedClass annotatedClass, List<AnnotationToken> annotations,
                                       List<HierarchyElement> hierarchy, List<Mapping> mappings, double score) {
        return new BioPortalAnnotation(annotatedClass, hierarchy, annotations, mappings, score);
    }

    @Override
    public Annotation createAnnotation(AnnotatedClass annotatedClass, List<AnnotationToken> annotations,
                                       List<HierarchyElement> hierarchy, List<Mapping> mappings) {
        return new BioPortalAnnotation(annotatedClass, hierarchy, annotations, mappings);
    }

    @Override
    public AnnotatedClass createAnnotatedClass(String id, String type, Links links, String contextVocab, Set<String> cuis, Set<UMLSGroup> semanticGroups) {
        return new BioPortalAnnotatedClass(id, type, links, contextVocab, cuis, semanticGroups);
    }

    @Override
    public AnnotationToken createAnnotationToken(int from, int to, String matchType, String text) {
        return new BioPortalAnnotationToken(from, to, matchType, text);
    }

    @Override
    public HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, int distance, double score) {
        return new BioPortalHierarchyElement(annotatedClass, distance, score);
    }

    @Override
    public HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, int distance) {
        return new BioPortalHierarchyElement(annotatedClass, distance);
    }

    @Override
    public Links createLinks(LinkMetadata linksMetadata, LinkMetadata linksContextMetadata) {
        return new BioPortalLinks(linksMetadata, linksContextMetadata);
    }

    @Override
    public LinkMetadata createLinkMetadata(String self, String ontology, String children, String parents,
                                           String descendants, String ancestors, String instances, String tree,
                                           String notes, String mappings, String ui) {
        return new BioportalLinkContext(self, ontology, children, parents, descendants, ancestors, instances,
                tree, notes, mappings, ui);
    }


    @Override
    public Mapping createMapping(AnnotatedClass annotatedClass, double score) {
        return new BioPortalMapping(annotatedClass, score);
    }

    @Override
    public Mapping createMapping(AnnotatedClass annotatedClass) {
        return new BioPortalMapping(annotatedClass);
    }
}
