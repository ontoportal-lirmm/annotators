package org.sifrproject.annotations.model.lazy;


import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.LinkMetadata;
import org.sifrproject.annotations.api.model.Links;
import org.sifrproject.annotations.api.model.lazy.LazyModelElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioPortalLazyLinks implements Links, LazyModelElement {

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

    private JSONObject jsonObject;

    public JSONObject getJSONObject() {
        return jsonObject;
    }

    BioPortalLazyLinks(LinkMetadata linksMetadata, LinkMetadata linksContextMetadata, JSONObject jsonObject) {
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
        this.jsonObject = jsonObject;
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
    public String toString() {
        return jsonObject.toJSONString();
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        linksContextMetadata.accept(annotationVisitor);
        annotationVisitor.visitAfter(this);
    }
}
