package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.Mapping;

/**
 * Default lazy dereference implementation of Mapping. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
public class BioPortalLazyMapping implements Mapping, LazyModelElement {
    private AnnotatedClass annotatedClass;
    private double score;

    private JsonObject jsonObject;

    public JsonObject getJSONObject() {
        return jsonObject;
    }

    BioPortalLazyMapping(AnnotatedClass annotatedClass, JsonObject jsonObject) {
        this.annotatedClass = annotatedClass;
        this.score = -1;
        JsonValue scoreObj = jsonObject.get("score");
        if (scoreObj!=null) {
            score = scoreObj.asDouble();
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
        jsonObject.add("score",score);
    }

    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }
}
