package org.sifrproject.annotations.model;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.HierarchyElement;
import org.sifrproject.annotations.api.model.LazyModelElement;

/**
 * Default lazy dereference implementation of HierarchyElement. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
public class BioPortalLazyHierarchyElement implements HierarchyElement, LazyModelElement {
    private AnnotatedClass annotatedClass;

    private int distance;

    private double score;

    private JsonObject jsonObject;

    BioPortalLazyHierarchyElement(final AnnotatedClass annotatedClass, final JsonObject jsonObject) {
        this.annotatedClass = annotatedClass;
        this.jsonObject = jsonObject;
        distance = -1;
        score = -1;
    }

    @Override
    public JsonObject getJSONObject() {
        return jsonObject;
    }


    @Override
    public AnnotatedClass getAnnotatedClass() {
        return annotatedClass;
    }

    @Override
    public int getDistance() {
        if (distance < 0) {
            distance = jsonObject.get("distance").asInt();
        }
        return distance;
    }

    @Override
    public double getScore() {
        if (score < 0) {
            final JsonValue jsonValue = jsonObject.get("score");
            if(jsonValue == null ){
                score = 0d;
            } else {
                score = jsonValue.asDouble();
            }
        }
        return score;
    }

    @SuppressWarnings("all")
    @Override
    public void setScore(double score) {
        this.score = score;
        jsonObject.add("score", Double.valueOf(score));
    }

    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }
}
