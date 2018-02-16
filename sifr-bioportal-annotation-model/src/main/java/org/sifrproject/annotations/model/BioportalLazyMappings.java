package org.sifrproject.annotations.model;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.Mapping;
import org.sifrproject.annotations.api.model.Mappings;

import java.util.Iterator;
import java.util.List;


/**
 * Default lazy dereference implementation of Mappings. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
public class BioportalLazyMappings implements Mappings, LazyModelElement {

    private List<Mapping> mappings;

    private JsonArray jsonObject;

     public BioportalLazyMappings(final List<Mapping> mappings, final JsonArray jsonObject) {
        this.mappings = mappings;
        this.jsonObject = jsonObject;
    }

    @Override
    public JsonValue getJSONObject() {
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
