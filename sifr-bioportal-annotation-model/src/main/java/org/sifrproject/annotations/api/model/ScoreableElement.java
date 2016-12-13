package org.sifrproject.annotations.api.model;

/**
 * An element of an annotation hierarchy that can hold a score (annotation, mapping, hierarchy element)
 */
public interface ScoreableElement {
    double getScore();

    void setScore(double score);
}
