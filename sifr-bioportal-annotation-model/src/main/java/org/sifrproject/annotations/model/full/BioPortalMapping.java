package org.sifrproject.annotations.model.full;


import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Mapping;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioPortalMapping implements Mapping {
    private AnnotatedClass annotatedClass;
    private double score;

    BioPortalMapping(AnnotatedClass annotatedClass) {
        this(annotatedClass, -1);
    }

    BioPortalMapping(AnnotatedClass annotatedClass, double score) {
        this.annotatedClass = annotatedClass;
        this.score = score;
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
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        annotatedClass.accept(annotationVisitor);
        annotationVisitor.visitAfter(this);
    }
}
