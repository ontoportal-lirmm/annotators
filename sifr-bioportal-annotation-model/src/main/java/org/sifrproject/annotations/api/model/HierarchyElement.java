package org.sifrproject.annotations.api.model;

import org.sifrproject.annotations.api.model.visitor.VisitableAnnotationElement;


public interface HierarchyElement extends VisitableAnnotationElement, ScoreableElement {
    AnnotatedClass getAnnotatedClass();

    int getDistance();

    double getScore();

    void setScore(double score);
}
