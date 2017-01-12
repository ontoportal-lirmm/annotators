package org.sifrproject.annotations.model;


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Links;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.umls.UMLSGroup;
import org.sifrproject.annotations.umls.UMLSGroupIndex;

import java.util.*;

public class BioPortalLazyAnnotatedClass implements AnnotatedClass, LazyModelElement {
    private String id = "";

    private String type = "";

    private Links links;

    private String contextVocab = "";

    private Set<String> cuis;

    private Set<UMLSGroup> semanticGroups;

    private JSONObject jsonObject;

    private PropertyRetriever cuiPropertyRetriever;
    private PropertyRetriever semanticGroupRetriever;

    private final UMLSGroupIndex umlsGroupIndex;

    public JSONObject getJSONObject() {
        return jsonObject;
    }

    @SuppressWarnings("all")
    BioPortalLazyAnnotatedClass(JSONObject jsonObject, Links links, PropertyRetriever cuiPropertyRetriever, PropertyRetriever semanticGroupRetriever, UMLSGroupIndex umlsGroupIndex) {
        this.jsonObject = jsonObject;
        this.id = (String) jsonObject.get("@id");
        this.type = (String) jsonObject.get("@type");
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

                jsonObject.put("semantic_groups", value.toString());
            }
        }
    }

    BioPortalLazyAnnotatedClass(JSONObject jsonObject, Links links) {
        this(jsonObject, links, null, null, null);
    }

    @Override
    public String getId() {
        if (id.isEmpty()) {
            id = (String) jsonObject.get("@id");
        }
        return id;
    }

    @Override
    public String getType() {
        if (type.isEmpty()) {
            type = (String) jsonObject.get("@type");
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
            contextVocab = (String) ((JSONObject) jsonObject.get("@context")).get("@vocab");
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
        return StringEscapeUtils.unescapeJson(jsonObject.toJSONString());
    }
}
