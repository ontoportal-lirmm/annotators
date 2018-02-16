package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.LinkMetadata;
import org.sifrproject.annotations.api.model.Links;

/**
 * Default lazy dereference implementation of Links. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
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

    private JsonObject jsonObject;

    @Override
    public JsonValue getJSONObject() {
        return jsonObject;
    }

    BioPortalLazyLinks(final LinkMetadata linksMetadata, final LinkMetadata linksContextMetadata, final JsonObject jsonObject) {
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
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }
}
