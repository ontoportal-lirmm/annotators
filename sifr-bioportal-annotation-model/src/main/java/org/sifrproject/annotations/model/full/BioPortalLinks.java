package org.sifrproject.annotations.model.full;


import org.sifrproject.annotations.api.model.LinkMetadata;
import org.sifrproject.annotations.api.model.Links;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioPortalLinks implements Links {

    private LinkMetadata linksContextMetadata;

    private String self;
    private String ontology;
    private String children;
    private String parents;
    private String descendants;
    private String ancestors;
    private String instances;
    private String tree;
    private String notes;
    private String mappings;
    private String ui;

    BioPortalLinks(LinkMetadata linksMetadata, LinkMetadata linksContextMetadata) {
        this.linksContextMetadata = linksContextMetadata;
        self = linksMetadata.getSelf();
        ontology = linksContextMetadata.getOntology();
        children = linksContextMetadata.getChildren();
        parents = linksContextMetadata.getParents();
        descendants = linksContextMetadata.getDescendants();
        ancestors = linksContextMetadata.getAncestors();
        instances = linksContextMetadata.getInstances();
        tree = linksContextMetadata.getTree();
        notes = linksContextMetadata.getNotes();
        mappings = linksContextMetadata.getMappings();
        ui = linksContextMetadata.getUi();
    }

    @Override
    public LinkMetadata getLinksContextMetadata() {
        return linksContextMetadata;
    }

    @Override
    public String getSelf() {
        return self;
    }

    @Override
    public String getOntology() {
        return ontology;
    }

    @Override
    public String getChildren() {
        return children;
    }

    @Override
    public String getParents() {
        return parents;
    }

    @Override
    public String getDescendants() {
        return descendants;
    }

    @Override
    public String getAncestors() {
        return ancestors;
    }

    @Override
    public String getInstances() {
        return instances;
    }

    @Override
    public String getTree() {
        return tree;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public String getMappings() {
        return mappings;
    }

    @Override
    public String getUi() {
        return ui;
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        linksContextMetadata.accept(annotationVisitor);
        annotationVisitor.visitAfter(this);
    }
}
