package org.sifrproject.annotations.model.full;

import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.HierarchyElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioPortalHierarchyElement implements HierarchyElement {
    private AnnotatedClass annotatedClass;

    private int distance;

    private double score;

    BioPortalHierarchyElement(AnnotatedClass annotatedClass, int distance, double score) {
        this.annotatedClass = annotatedClass;
        this.distance = distance;
        this.score = score;
    }

    BioPortalHierarchyElement(AnnotatedClass annotatedClass, int distance) {
        this(annotatedClass, distance, -1);
    }

    @Override
    public AnnotatedClass getAnnotatedClass() {
        return annotatedClass;
    }

    @Override
    public int getDistance() {
        return distance;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
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
