package org.sifrproject.annotations.model;


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.model.LazyModelElement;

class BioPortalLazyAnnotation implements Annotation, LazyModelElement {

    private AnnotatedClass annotatedClass;

    private final Hierarchy hierarchy;

    private JSONObject jsonObject;

    public JSONObject getJSONObject() {
        return jsonObject;
    }


    private AnnotationTokens annotationTokens;
    private Mappings mappings;


    private double score;


    BioPortalLazyAnnotation(AnnotatedClass annotatedClass, Hierarchy hierarchy, AnnotationTokens annotations, Mappings mappings, JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.annotatedClass = annotatedClass;
        this.annotationTokens = annotations;
        this.hierarchy = hierarchy;
        this.mappings = mappings;
    }

    @Override
    public AnnotatedClass getAnnotatedClass() {
        return annotatedClass;
    }

    public AnnotationTokens getAnnotations() {
        return annotationTokens;
    }

    @Override
    public double getScore() {
        this.score = -1;
        if (jsonObject.containsKey("score")) {
            score = (Double) jsonObject.get("score");
        }
        return score;
    }


    @SuppressWarnings("all")
    @Override
    public void setScore(double score) {
        this.score = score;
        jsonObject.put("score", Double.valueOf(score));
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public Mappings getMappings() {
        return mappings;
    }


    @Override
    public String toString() {
        return StringEscapeUtils.unescapeJson(jsonObject.toJSONString());
    }
}
