package org.sifrproject.annotations.model.lazy;

import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.HierarchyElement;
import org.sifrproject.annotations.api.model.lazy.LazyModelElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioPortalLazyHierarchyElement implements HierarchyElement, LazyModelElement {
    private AnnotatedClass annotatedClass;

    private int distance;

    private double score;

    private JSONObject jsonObject;

    BioPortalLazyHierarchyElement(AnnotatedClass annotatedClass, JSONObject jsonObject) {
        this.annotatedClass = annotatedClass;
        this.jsonObject = jsonObject;
        distance = -1;
        score = -1;
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }


    @Override
    public AnnotatedClass getAnnotatedClass() {
        return annotatedClass;
    }

    @Override
    public int getDistance() {
        if (distance < 0) {
            distance = ((Long) jsonObject.get("distance")).intValue();
        }
        return distance;
    }

    @Override
    public double getScore() {
        if (score < 0) {
            score = Double.valueOf((String) jsonObject.get("score"));
        }
        return score;
    }

    @SuppressWarnings("all")
    @Override
    public void setScore(double score) {
        this.score = score;
        jsonObject.put("score", Double.valueOf(score));
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        annotatedClass.accept(annotationVisitor);
        annotationVisitor.visitAfter(this);
    }

    @Override
    public String toString() {
        return jsonObject.toJSONString();
    }
}
