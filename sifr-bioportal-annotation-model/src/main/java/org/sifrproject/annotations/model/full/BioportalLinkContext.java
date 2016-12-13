package org.sifrproject.annotations.model.full;


import org.sifrproject.annotations.api.model.LinkContext;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioportalLinkContext implements LinkContext {

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

    BioportalLinkContext(String self, String ontology, String children, String parents,
                         String descendants, String ancestors, String instances, String tree,
                         String notes, String mappings, String ui) {
        this.self = self;
        this.ontology = ontology;
        this.children = children;
        this.parents = parents;
        this.descendants = descendants;
        this.ancestors = ancestors;
        this.instances = instances;
        this.tree = tree;
        this.notes = notes;
        this.mappings = mappings;
        this.ui = ui;
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
        annotationVisitor.visitAfter(this);
    }
}
