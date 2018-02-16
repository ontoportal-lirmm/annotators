package org.sifrproject.annotations.model;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.apache.commons.lang3.StringEscapeUtils;
import org.sifrproject.annotations.api.model.Hierarchy;
import org.sifrproject.annotations.api.model.HierarchyElement;
import org.sifrproject.annotations.api.model.LazyModelElement;

import java.util.Iterator;
import java.util.List;


/**
 * Default lazy dereference implementation of Hierarchy. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
public class BioportalLazyHierarchy implements Hierarchy, LazyModelElement {

    private List<HierarchyElement> hierarchyList;

    private JsonValue jsonObject;

    public BioportalLazyHierarchy(final List<HierarchyElement> hierarchyList, final JsonArray jsonObject) {
        this.hierarchyList = hierarchyList;
        this.jsonObject = jsonObject;
    }

    @Override
    public JsonValue getJSONObject() {
        return jsonObject;
    }

    @Override
    public Iterator<HierarchyElement> iterator() {
        return hierarchyList.iterator();
    }

    @Override
    public String toString() {
        return StringEscapeUtils.unescapeJson(jsonObject.toString(WriterConfig.PRETTY_PRINT));
    }
}
