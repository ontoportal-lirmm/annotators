package org.sifrproject.annotations.model;

import org.json.simple.JSONArray;
import org.sifrproject.annotations.api.model.Mapping;
import org.sifrproject.annotations.api.model.Mappings;
import org.sifrproject.annotations.api.model.LazyModelElement;

import java.util.Iterator;
import java.util.List;


public class BioportalLazyMappings implements Mappings, LazyModelElement {

    private List<Mapping> mappings;

    private JSONArray jsonObject;

    public BioportalLazyMappings(List<Mapping> mappings, JSONArray jsonObject) {
        this.mappings = mappings;
        this.jsonObject = jsonObject;
    }

    public Object getJSONObject() {
        return jsonObject;
    }

    @Override
    public Iterator<Mapping> iterator() {
        return mappings.iterator();
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
