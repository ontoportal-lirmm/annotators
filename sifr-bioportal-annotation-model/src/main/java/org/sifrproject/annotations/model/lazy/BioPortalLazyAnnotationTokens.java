package org.sifrproject.annotations.model.lazy;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.AnnotationTokens;
import org.sifrproject.annotations.api.model.lazy.LazyModelElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

import java.util.Iterator;
import java.util.List;


public class BioPortalLazyAnnotationTokens implements AnnotationTokens, LazyModelElement {

    private List<AnnotationToken> annotationTokens;

    private JSONArray jsonObject;

    public BioPortalLazyAnnotationTokens(List<AnnotationToken> annotationTokens, JSONArray jsonObject) {
        this.annotationTokens = annotationTokens;
        this.jsonObject = jsonObject;
    }

    public Object getJSONObject() {
        return jsonObject;
    }

    @Override
    public Iterator<AnnotationToken> iterator() {
        return annotationTokens.iterator();
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        for (AnnotationToken annotationToken : annotationTokens) {
            annotationToken.accept(annotationVisitor);
        }
        annotationVisitor.visitAfter(this);
    }

    @Override
    public String toString() {
        return StringEscapeUtils.unescapeJson(jsonObject.toJSONString());
    }
}
