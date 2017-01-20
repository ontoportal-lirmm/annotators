package org.sifrproject.annotations.api.model;

/**
 * An element of an annotation hierarchy that can hold a score (annotation, mapping, hierarchy element)
 */
public interface ScoreableElement {
    /**
     * Getter for the score
     * @return the score
     */
    double getScore();

    /**
     * Setter for the score
     * @param score The score
     */
    void setScore(double score);
}
