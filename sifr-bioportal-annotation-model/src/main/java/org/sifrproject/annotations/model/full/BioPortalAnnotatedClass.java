package org.sifrproject.annotations.model.full;


import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Links;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;
import org.sifrproject.annotations.umls.groups.UMLSGroup;

import java.util.Set;

public class BioPortalAnnotatedClass implements AnnotatedClass {
    private String id;
    private String type;

    private Links links;

    private String contextVocab;

    private Set<String> cuis;

    private Set<UMLSGroup> semanticGroups;

    BioPortalAnnotatedClass(String id, String type, Links links, String contextVocab, Set<String> cuis, Set<UMLSGroup> semanticGroups) {
        this.id = id;
        this.type = type;
        this.links = links;
        this.contextVocab = contextVocab;
        this.cuis = cuis;
        this.semanticGroups = semanticGroups;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Links getLinks() {
        return links;
    }

    @Override
    public String getContextVocab() {
        return contextVocab;
    }

    public Set<String> getCuis() {
        return cuis;
    }

    public Set<UMLSGroup> getSemanticGroups() {
        return semanticGroups;
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        links.accept(annotationVisitor);
        annotationVisitor.visitAfter(this);
    }
}
