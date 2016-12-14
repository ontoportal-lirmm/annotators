package org.sifrproject.annotations.model;


import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Mapping;
import org.sifrproject.annotations.api.model.LazyModelElement;

public class BioPortalLazyMapping implements Mapping, LazyModelElement {
    private AnnotatedClass annotatedClass;
    private double score;

    private JSONObject jsonObject;

    public JSONObject getJSONObject() {
        return jsonObject;
    }

    BioPortalLazyMapping(AnnotatedClass annotatedClass, JSONObject jsonObject) {
        this.annotatedClass = annotatedClass;
        this.score = -1;
        if (jsonObject.containsKey("score")) {
            score = Double.valueOf((String) jsonObject.get("score"));
        }
        this.jsonObject = jsonObject;
    }

    public AnnotatedClass getAnnotatedClass() {
        return annotatedClass;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return jsonObject.toJSONString();
    }
}
