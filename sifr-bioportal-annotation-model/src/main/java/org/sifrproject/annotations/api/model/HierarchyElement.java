package org.sifrproject.annotations.api.model;

public interface HierarchyElement extends ScoreableElement {
    AnnotatedClass getAnnotatedClass();

    int getDistance();

    double getScore();

    void setScore(double score);
}
