package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.*;

import java.util.Objects;

/**
 * Default lazy dereference implementation of Annotation. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
class BioPortalLazyAnnotation implements Annotation, LazyModelElement {

    private final AnnotatedClass annotatedClass;

    private final Hierarchy hierarchy;

    private final JsonObject jsonObject;

    @Override
    public JsonObject getJSONObject() {
        return jsonObject;
    }


    private final AnnotationTokens annotationTokens;
    private final Mappings mappings;


    private double score;


    BioPortalLazyAnnotation(final AnnotatedClass annotatedClass, final Hierarchy hierarchy, final AnnotationTokens annotations, final Mappings mappings, final JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.annotatedClass = annotatedClass;
        annotationTokens = annotations;
        this.hierarchy = hierarchy;
        this.mappings = mappings;
    }

    @Override
    public AnnotatedClass getAnnotatedClass() {
        return annotatedClass;
    }

    @Override
    public AnnotationTokens getAnnotations() {
        return annotationTokens;
    }

    @Override
    public double getScore() {
        score = -1;
        final JsonValue score = jsonObject.get("score");
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

    @Override
    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    @Override
    public Mappings getMappings() {
        return mappings;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BioPortalLazyAnnotation that = (BioPortalLazyAnnotation) o;
        return Objects.equals(getAnnotatedClass(), that.getAnnotatedClass());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getAnnotatedClass(), annotationTokens);
    }

    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }
}
