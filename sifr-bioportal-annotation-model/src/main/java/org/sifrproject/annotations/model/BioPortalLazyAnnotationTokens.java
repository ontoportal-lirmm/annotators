package org.sifrproject.annotations.model;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.apache.commons.lang3.StringEscapeUtils;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.AnnotationTokens;
import org.sifrproject.annotations.api.model.LazyModelElement;

import java.util.Iterator;
import java.util.List;

/**
 * Default lazy dereference implementation of AnnotationTokens. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
public class BioPortalLazyAnnotationTokens implements AnnotationTokens, LazyModelElement {

    private List<AnnotationToken> annotationTokens;

    private JsonArray jsonObject;

    public BioPortalLazyAnnotationTokens(final List<AnnotationToken> annotationTokens, final JsonArray jsonObject) {
        this.annotationTokens = annotationTokens;
        this.jsonObject = jsonObject;
    }

    @Override
    public JsonValue getJSONObject() {
        return jsonObject;
    }

    @Override
    public Iterator<AnnotationToken> iterator() {
        return annotationTokens.iterator();
    }

    @Override
    public String toString() {
        return StringEscapeUtils.unescapeJson(jsonObject.toString(WriterConfig.PRETTY_PRINT));
    }
}
