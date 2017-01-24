package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.Links;
import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.umls.UMLSGroup;
import org.sifrproject.annotations.umls.UMLSGroupIndex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BioPortalLazyAnnotatedClass implements AnnotatedClass, LazyModelElement {
    private String id = "";

    private String type = "";

    private Links links;

    private String contextVocab = "";

    private Set<String> cuis;

    private Set<UMLSGroup> semanticGroups;

    private JsonObject jsonObject;

    private PropertyRetriever cuiPropertyRetriever;
    private PropertyRetriever semanticGroupRetriever;

    private final UMLSGroupIndex umlsGroupIndex;

    public JsonValue getJSONObject() {
        return jsonObject;
    }

    @SuppressWarnings("all")
    BioPortalLazyAnnotatedClass(JsonObject jsonObject, Links links, PropertyRetriever cuiPropertyRetriever, PropertyRetriever semanticGroupRetriever, UMLSGroupIndex umlsGroupIndex) {
        this.jsonObject = jsonObject;
        this.id = jsonObject.get("@id").asString();
        this.type = jsonObject.get("@type").asString();
        this.links = links;
        cuis = new TreeSet<>();
        semanticGroups = new HashSet<>();
        this.cuiPropertyRetriever = cuiPropertyRetriever;
        this.semanticGroupRetriever = semanticGroupRetriever;
        this.umlsGroupIndex = umlsGroupIndex;
        if (semanticGroupRetriever != null) {
            List<String> types = semanticGroupRetriever.retrievePropertyValues(getId());
            for(String type: types){
                semanticGroups.add(umlsGroupIndex.getGroupByType(type));
            }

            if (!semanticGroups.isEmpty()) {
                StringBuilder value = new StringBuilder();
                boolean first = true;
                for (UMLSGroup umlsGroup : semanticGroups) {
                    if (!first) {
                        value.append(",");
                    }
                    first = false;
                    value.append(umlsGroup.name());
                }

                jsonObject.add("semantic_groups", value.toString());
            }
        }
    }

    BioPortalLazyAnnotatedClass(JsonObject jsonObject, Links links) {
        this(jsonObject, links, null, null, null);
    }

    @Override
    public String getId() {
        if (id.isEmpty()) {
            id = jsonObject.get("@id").asString();
        }
        return id;
    }

    @Override
    public String getType() {
        if (type.isEmpty()) {
            type = (String) jsonObject.get("@type").asString();
        }
        return type;
    }

    @Override
    public Links getLinks() {
        return links;
    }

    @Override
    public String getContextVocab() {
        if (contextVocab.isEmpty()) {
            contextVocab = jsonObject.get("@context").asObject().get("@vocab").asString();
        }
        return contextVocab;
    }

    public Set<String> getCuis() {
        if (cuis.isEmpty() && cuiPropertyRetriever != null) {
            cuis.addAll(cuiPropertyRetriever.retrievePropertyValues(getId()));
        }
        return cuis;
    }

    public Set<UMLSGroup> getSemanticGroups() {
        if (semanticGroups.isEmpty() && semanticGroupRetriever != null) {
            List<String> types = semanticGroupRetriever.retrievePropertyValues(getId());
            for(String type: types){
                semanticGroups.add(umlsGroupIndex.getGroupByType(type));
            }
        }
        return semanticGroups;
    }

    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }
}
