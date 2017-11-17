package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.Links;
import org.sifrproject.annotations.api.model.retrieval.UMLSProperties;
import org.sifrproject.annotations.api.model.retrieval.UMLSPropertyRetriever;
import org.sifrproject.annotations.umls.UMLSGroup;
import org.sifrproject.annotations.umls.UMLSGroupIndex;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public JsonValue getJSONObject() {
        return jsonObject;
    }

    @SuppressWarnings("all")
    BioPortalLazyAnnotatedClass(JsonObject jsonObject, Links links, UMLSPropertyRetriever umlsPropertyRetriever, UMLSGroupIndex umlsGroupIndex) {
        this.jsonObject = jsonObject;
        this.id = jsonObject.get("@id").asString();
        this.type = jsonObject.get("@type").asString();
        this.links = links;
        cuis = new TreeSet<>();
        semanticGroups = new ArrayList<>();
        UMLSPropertyRetriever semanticGroupRetriever = umlsPropertyRetriever;
        UMLSGroupIndex umlsGroupIndex1 = umlsGroupIndex;
        UMLSProperties properties = umlsPropertyRetriever.retrievePropertyValues(links.getSelf());
        if (umlsPropertyRetriever != null) {
            List<String> types = properties.getTUIs();
            JsonArray tuiArray = new JsonArray();
            for(String type: types){
                final UMLSGroup group = umlsGroupIndex.getGroupByType(type);
                if(group !=null && !semanticGroups.contains(group)) {
                    semanticGroups.add(group);
                }
                tuiArray.add(type);
            }
            jsonObject.add("semantic_types", tuiArray);

            cuis.addAll(properties.getCUIs());

            if (!semanticGroups.isEmpty()) {
                JsonArray guiArray = new JsonArray();

                for (UMLSGroup umlsGroup : semanticGroups) {
                    guiArray.add(umlsGroup.getName());
                }
                jsonObject.add("semantic_groups", guiArray);
            }

            if(!cuis.isEmpty()){
                JsonArray cuiArray = new JsonArray();
                for (String cui : cuis) {
                    cuiArray.add(cui);
                }
                jsonObject.add("cuis", cuiArray);
            }
        }
    }



    BioPortalLazyAnnotatedClass(final JsonObject jsonObject, final Links links) {
        this(jsonObject, links, null, null);
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
            type = jsonObject.get("@type").asString();
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

    @Override
    public Set<String> getCuis() {
//        if (cuis.isEmpty() && (cuiPropertyRetriever != null)) {
//            cuis.addAll(cuiPropertyRetriever.retrievePropertyValues(getId()));
//        }
        return Collections.unmodifiableSet(cuis);
    }

    @Override
    public List<UMLSGroup> getSemanticGroups() {
//        if (value!=null){
//            final String groupString = value.asString();
//            for(final String group: groupString.split(",")){
//                semanticGroups.add(umlsGroupIndex.getGroupByName(group));
//            }
//        } else if (semanticGroups.isEmpty() && (semanticGroupRetriever != null)) {
//            final List<String> types = semanticGroupRetriever.retrievePropertyValues(getId());
//            for(final String type: types){
//                semanticGroups.add(umlsGroupIndex.getGroupByType(type));
//            }
//        }
        return Collections.unmodifiableList(semanticGroups);
    }
    @Override
    public void setSemanticGroups(final List<UMLSGroup> groups){
        semanticGroups = new ArrayList<>(groups);
        final String groupString = groups.stream().map(UMLSGroup::getName).collect(Collectors.joining(","));
        jsonObject.set("semantic_groups", groupString);
    }

    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }


}
