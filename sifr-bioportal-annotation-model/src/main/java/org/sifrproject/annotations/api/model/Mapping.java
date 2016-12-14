package org.sifrproject.annotations.api.model;


public interface Mapping extends ScoreableElement {

    AnnotatedClass getAnnotatedClass();

    double getScore();

    void setScore(double score);
}
