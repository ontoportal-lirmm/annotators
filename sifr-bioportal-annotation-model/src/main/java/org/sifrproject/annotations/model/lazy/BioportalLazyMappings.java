package org.sifrproject.annotations.model.lazy;

import org.json.simple.JSONArray;
import org.sifrproject.annotations.api.model.Mapping;
import org.sifrproject.annotations.api.model.Mappings;
import org.sifrproject.annotations.api.model.lazy.LazyModelElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

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
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        for (Mapping mapping : mappings) {
            mapping.accept(annotationVisitor);
        }
        annotationVisitor.visitAfter(this);
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
