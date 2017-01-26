package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.LinkContext;

/**
 * Default lazy dereference implementation of LinkContext. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
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

    private JsonObject jsonObject;

    BioportalLazyLinkContext(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        self = "";
        ontology = "";
        children = "";
        parents = "";
        descendants = jsonObject.get("descendants").asString();
        ancestors = jsonObject.get("ancestors").asString();
        instances = jsonObject.get("instances").asString();
        tree = jsonObject.get("tree").asString();
        notes = jsonObject.get("descendants").asString();
        mappings = jsonObject.get("mappings").asString();
        ui = jsonObject.get("descendants").asString();
    }

    @Override
    public JsonObject getJSONObject() {
        return jsonObject;
    }

    @Override
    public String getSelf() {
        if (self.isEmpty()) {
            self = jsonObject.get("self").asString();
        }
        return self;
    }

    @Override
    public String getOntology() {
        if (ontology != null && ontology.isEmpty()) {
            ontology = jsonObject.get("ontology").asString();
        }
        return ontology;
    }

    @Override
    public String getChildren() {
        if (children != null && children.isEmpty()) {
            children = jsonObject.get("children").asString();
        }
        return children;
    }

    @Override
    public String getParents() {
        if (parents != null && parents.isEmpty()) {
            parents = jsonObject.get("parents").asString();
        }
        return parents;
    }

    @Override
    public String getDescendants() {
        if (descendants != null && descendants.isEmpty()) {
            descendants = jsonObject.get("descendants").asString();
        }
        return descendants;
    }

    @Override
    public String getAncestors() {
        if (ancestors != null && ancestors.isEmpty()) {
            ancestors = jsonObject.get("ancestors").asString();
        }
        return ancestors;
    }

    @Override
    public String getInstances() {
        if (instances != null && instances.isEmpty()) {
            instances = jsonObject.get("instances").asString();
        }
        return instances;
    }

    @Override
    public String getTree() {
        if (tree != null && tree.isEmpty()) {
            tree = jsonObject.get("tree").asString();
        }
        return tree;
    }

    @Override
    public String getNotes() {
        if (notes != null && notes.isEmpty()) {
            notes = jsonObject.get("notes").asString();
        }
        return notes;
    }

    @Override
    public String getMappings() {
        if (mappings.isEmpty()) {
            mappings = jsonObject.get("mappings").asString();
        }
        return mappings;
    }

    @Override
    public String getUi() {
        if (ui != null && ui.isEmpty()) {
            ui = jsonObject.get("ui").asString();
        }
        return ui;
    }

    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }
}
