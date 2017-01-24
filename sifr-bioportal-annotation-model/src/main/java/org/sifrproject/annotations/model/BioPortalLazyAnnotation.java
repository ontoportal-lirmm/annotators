package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.*;

class BioPortalLazyAnnotation implements Annotation, LazyModelElement {

    private AnnotatedClass annotatedClass;

    private final Hierarchy hierarchy;

    private JsonObject jsonObject;

    public JsonObject getJSONObject() {
        return jsonObject;
    }


    private AnnotationTokens annotationTokens;
    private Mappings mappings;


    private double score;


    BioPortalLazyAnnotation(AnnotatedClass annotatedClass, Hierarchy hierarchy, AnnotationTokens annotations, Mappings mappings, JsonObject jsonObject) {
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
        JsonValue score = jsonObject.get("score");
        if (score !=null) {
            this.score =  score.asDouble();
        }
        return this.score;
    }


    @SuppressWarnings("all")
    @Override
    public void setScore(double score) {
        this.score = score;
        jsonObject.add("score", Double.valueOf(score));
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public Mappings getMappings() {
        return mappings;
    }


    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }
}
