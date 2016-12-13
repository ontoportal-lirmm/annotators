package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.api.model.visitor.VisitableAnnotationElement;

public interface Mapping extends VisitableAnnotationElement, ScoreableElement {

    AnnotatedClass getAnnotatedClass();

    double getScore();

    void setScore(double score);
}
