package org.sifrproject.annotations.api.model;

/**
 * An element of an annotation hierarchy that can hold a score (annotation, mapping, hierarchy element)
 */
public interface ScoreableElement {
    /**
     * Returns the score associated with the annotation
     * @return The score associated with the annotation
     */
    double getScore();

    /**
     * Sets the score associated with the annotation
     * @param score The score the score associated with the annotation
     */
    void setScore(double score);
}
