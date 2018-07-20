package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.Links;
import org.sifrproject.annotations.umls.UMLSGroup;
import org.sifrproject.annotations.umls.UMLSGroupIndex;

import java.util.*;

/**
 * Default lazy dereference implementation of AnnotatedClass. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
public class BioPortalLazyAnnotatedClass implements AnnotatedClass, LazyModelElement {
    private String id = "";

    private String type = "";

    private final Links links;

    private String contextVocab = "";

    private final Set<String> cuis;

    private List<UMLSGroup> semanticGroups;

    private final JsonObject jsonObject;

    private boolean fetchedSemInfo;

    //    private final UMLSPropertyRetriever umlsPropertyRetriever;

    @Override
    public JsonValue getJSONObject() {
        return jsonObject;
    }

    @SuppressWarnings("all")
    BioPortalLazyAnnotatedClass(JsonObject jsonObject, Links links, UMLSGroupIndex umlsGroupIndex) {
        this.jsonObject = jsonObject;
        this.id = jsonObject
                .get("@id")
                .asString();
        this.type = jsonObject
                .get("@type")
                .asString();
        this.links = links;
        cuis = new TreeSet<>();
        semanticGroups = new ArrayList<>();

        JsonValue cuisValue = jsonObject.get("cui");
        if(cuisValue!=null && cuisValue.isArray()){
            for(JsonValue value: cuisValue.asArray()){
                cuis.add(value.asString());
            }
        }

        JsonValue semanticTypesValue = jsonObject.get("semanticType");
        if(semanticTypesValue!=null && semanticTypesValue.isArray()){
            final JsonArray guiArray = new JsonArray();
            for (JsonValue semanticType: semanticTypesValue.asArray()){
                final UMLSGroup group = umlsGroupIndex.getGroupByType(semanticType.asString());
                if ((group != null) && !semanticGroups.contains(group)) {
                    semanticGroups.add(group);
                    guiArray.add(group.getName());
                }
            }
            jsonObject.add("semantic_groups", guiArray);
        }


    }


//    private void fetchSemanticInformation() {
//        final UMLSProperties properties = umlsPropertyRetriever
//                .retrievePropertyValues(
//                        links.getSelf());
//
//        final List<String> types = properties.getTUIs();
//        final JsonArray tuiArray = new JsonArray();
//        final JsonArray guiArray = new JsonArray();
//        for (final String type : types) {
//            final UMLSGroup group = umlsGroupIndex.getGroupByType(type);
//            if ((group != null) && !semanticGroups.contains(group)) {
//                semanticGroups.add(group);
//                guiArray.add(group.getName());
//            }
//            tuiArray.add(type);
//        }
//        jsonObject.add("semantic_groups", guiArray);
//        jsonObject.add("semantic_types", tuiArray);
//
//        cuis.addAll(properties.getCUIs());
//
//        if (!cuis.isEmpty()) {
//            final JsonArray cuiArray = new JsonArray();
//            for (final String cui : cuis) {
//                cuiArray.add(cui);
//            }
//            jsonObject.add("cuis", cuiArray);
//        }
//        fetchedSemInfo = true;
//    }


    BioPortalLazyAnnotatedClass(final JsonObject jsonObject, final Links links) {
        this(jsonObject, links, null);
    }

    @Override
    public String getId() {
        if (id.isEmpty()) {
            id = jsonObject
                    .get("@id")
                    .asString();
        }
        return id;
    }

    @Override
    public String getType() {
        if (type.isEmpty()) {
            type = jsonObject
                    .get("@type")
                    .asString();
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
            contextVocab = jsonObject
                    .get("@context")
                    .asObject()
                    .get("@vocab")
                    .asString();
        }
        return contextVocab;
    }

    @Override
    public Set<String> getCuis() {
//        if (!fetchedSemInfo) {
//            fetchSemanticInformation();
//        }
        return Collections.unmodifiableSet(cuis);
    }

    @Override
    public List<UMLSGroup> getSemanticGroups() {
//        if (!fetchedSemInfo) {
//            fetchSemanticInformation();
//        }
        return Collections.unmodifiableList(semanticGroups);
    }

    @Override
    public void setSemanticGroups(final List<UMLSGroup> groups) {
        semanticGroups = new ArrayList<>(groups);
        final JsonArray groupArray = new JsonArray();
        groups.forEach(g -> groupArray.add(g.getName()));
        jsonObject.set("semantic_groups", groupArray);
    }

    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BioPortalLazyAnnotatedClass that = (BioPortalLazyAnnotatedClass) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
