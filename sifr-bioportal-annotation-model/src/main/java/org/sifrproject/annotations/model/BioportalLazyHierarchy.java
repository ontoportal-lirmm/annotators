package org.sifrproject.annotations.model;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.sifrproject.annotations.api.model.Hierarchy;
import org.sifrproject.annotations.api.model.HierarchyElement;
import org.sifrproject.annotations.api.model.LazyModelElement;

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
    public String toString() {
        return StringEscapeUtils.unescapeJson(jsonObject.toJSONString());
    }
}
