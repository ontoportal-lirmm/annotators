package org.sifrproject.annotations.model.lazy;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.sifrproject.annotations.api.model.Hierarchy;
import org.sifrproject.annotations.api.model.HierarchyElement;
import org.sifrproject.annotations.api.model.lazy.LazyModelElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

import java.util.Iterator;
import java.util.List;


public class BioportalLazyHierarchy implements Hierarchy, LazyModelElement {

    private List<HierarchyElement> hierarchyList;

    private JSONArray jsonObject;

    public BioportalLazyHierarchy(List<HierarchyElement> hierarchyList, JSONArray jsonObject) {
        this.hierarchyList = hierarchyList;
        this.jsonObject = jsonObject;
    }

    public Object getJSONObject() {
        return jsonObject;
    }

    @Override
    public Iterator<HierarchyElement> iterator() {
        return hierarchyList.iterator();
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        for (HierarchyElement hierarchyElement : hierarchyList) {
            hierarchyElement.accept(annotationVisitor);
        }
        annotationVisitor.visitAfter(this);
    }

    @Override
    public String toString() {
        return StringEscapeUtils.unescapeJson(jsonObject.toJSONString());
    }
}
