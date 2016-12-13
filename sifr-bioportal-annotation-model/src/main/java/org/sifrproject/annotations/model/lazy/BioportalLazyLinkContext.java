package org.sifrproject.annotations.model.lazy;


import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.LinkContext;
import org.sifrproject.annotations.api.model.lazy.LazyModelElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioportalLazyLinkContext implements LinkContext, LazyModelElement {

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

    BioportalLazyLinkContext(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        self = "";
        ontology = "";
        children = "";
        parents = "";
        descendants = (String) jsonObject.get("descendants");
        ancestors = (String) jsonObject.get("ancestors");
        instances = (String) jsonObject.get("instances");
        tree = (String) jsonObject.get("tree");
        notes = (String) jsonObject.get("descendants");
        mappings = (String) jsonObject.get("mappings");
        ui = (String) jsonObject.get("descendants");
    }

    @Override
    public JSONObject getJSONObject() {
        return jsonObject;
    }

    @Override
    public String getSelf() {
        if (self.isEmpty()) {
            self = (String) jsonObject.get("self");
        }
        return self;
    }

    @Override
    public String getOntology() {
        if (ontology.isEmpty()) {
            ontology = (String) jsonObject.get("ontology");
        }
        return ontology;
    }

    @Override
    public String getChildren() {
        if (children.isEmpty()) {
            children = (String) jsonObject.get("children");
        }
        return children;
    }

    @Override
    public String getParents() {
        if (parents.isEmpty()) {
            parents = (String) jsonObject.get("parents");
        }
        return parents;
    }

    @Override
    public String getDescendants() {
        if (descendants.isEmpty()) {
            descendants = (String) jsonObject.get("descendants");
        }
        return descendants;
    }

    @Override
    public String getAncestors() {
        if (ancestors.isEmpty()) {
            ancestors = (String) jsonObject.get("ancestors");
        }
        return ancestors;
    }

    @Override
    public String getInstances() {
        if (instances.isEmpty()) {
            instances = (String) jsonObject.get("instances");
        }
        return instances;
    }

    @Override
    public String getTree() {
        if (tree.isEmpty()) {
            tree = (String) jsonObject.get("tree");
        }
        return tree;
    }

    @Override
    public String getNotes() {
        if (notes.isEmpty()) {
            notes = (String) jsonObject.get("notes");
        }
        return notes;
    }

    @Override
    public String getMappings() {
        if (mappings.isEmpty()) {
            mappings = (String) jsonObject.get("mappings");
        }
        return mappings;
    }

    @Override
    public String getUi() {
        if (ui.isEmpty()) {
            ui = (String) jsonObject.get("ui");
        }
        return ui;
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        annotationVisitor.visitAfter(this);
    }

    @Override
    public String toString() {
        return jsonObject.toJSONString();
    }
}
