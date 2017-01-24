package org.sifrproject.annotations.api.model;

import com.eclipsesource.json.JsonValue;

/**
 * An interface specifying a lazy model element that has and underlying JSON object. This is used for a generic
 * implementation of a lazy fetch model from the JSON datamodel.
 */
public interface LazyModelElement {
    /**
     * Get the JSONObject associated with the lazy element
     * @return the JSONObject associated with the lazy element
     */
    JsonValue getJSONObject();
}
