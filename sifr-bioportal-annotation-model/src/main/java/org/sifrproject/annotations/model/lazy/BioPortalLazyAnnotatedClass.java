package org.sifrproject.annotations.model.lazy;


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Links;
import org.sifrproject.annotations.api.model.lazy.LazyModelElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;
import org.sifrproject.annotations.api.umls.PropertyRetriever;
import org.sifrproject.annotations.umls.groups.UMLSGroup;
import org.sifrproject.annotations.umls.groups.UMLSGroupIndex;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

    BioPortalLazyAnnotatedClass(JSONObject jsonObject, Links links, PropertyRetriever cuiPropertyRetriever, PropertyRetriever semanticGroupRetriever, UMLSGroupIndex umlsGroupIndex) {
        this.jsonObject = jsonObject;
        this.id = (String) jsonObject.get("@id");
        this.type = (String) jsonObject.get("@type");
        this.links = links;
        cuis = new TreeSet<>();
        semanticGroups = Collections.emptySet();
        this.cuiPropertyRetriever = cuiPropertyRetriever;
        this.semanticGroupRetriever = semanticGroupRetriever;
        this.umlsGroupIndex = umlsGroupIndex;
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
            semanticGroups = types.stream().map(umlsGroupIndex::getGroupByType).collect(Collectors.toSet());
        }
        return semanticGroups;
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        links.accept(annotationVisitor);
        annotationVisitor.visitAfter(this);
    }

    @Override
    public String toString() {
        return StringEscapeUtils.unescapeJson(jsonObject.toJSONString());
    }
}
