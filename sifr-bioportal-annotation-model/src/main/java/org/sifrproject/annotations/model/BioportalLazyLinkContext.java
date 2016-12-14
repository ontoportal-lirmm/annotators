package org.sifrproject.annotations.model;


import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.LinkContext;
import org.sifrproject.annotations.api.model.LazyModelElement;

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
        if (ontology != null && ontology.isEmpty()) {
            ontology = (String) jsonObject.get("ontology");
        }
        return ontology;
    }

    @Override
    public String getChildren() {
        if (children != null && children.isEmpty()) {
            children = (String) jsonObject.get("children");
        }
        return children;
    }

    @Override
    public String getParents() {
        if (parents != null && parents.isEmpty()) {
            parents = (String) jsonObject.get("parents");
        }
        return parents;
    }

    @Override
    public String getDescendants() {
        if (descendants != null && descendants.isEmpty()) {
            descendants = (String) jsonObject.get("descendants");
        }
        return descendants;
    }

    @Override
    public String getAncestors() {
        if (ancestors != null && ancestors.isEmpty()) {
            ancestors = (String) jsonObject.get("ancestors");
        }
        return ancestors;
    }

    @Override
    public String getInstances() {
        if (instances != null && instances.isEmpty()) {
            instances = (String) jsonObject.get("instances");
        }
        return instances;
    }

    @Override
    public String getTree() {
        if (tree != null && tree.isEmpty()) {
            tree = (String) jsonObject.get("tree");
        }
        return tree;
    }

    @Override
    public String getNotes() {
        if (notes != null && notes.isEmpty()) {
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
        if (ui != null && ui.isEmpty()) {
            ui = (String) jsonObject.get("ui");
        }
        return ui;
    }

    @Override
    public String toString() {
        return jsonObject.toJSONString();
    }
}
