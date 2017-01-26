package org.sifrproject.annotations.api.model;


/**
 * This interface models a mapping associated with an annotation
 */
public interface Mapping extends ScoreableElement {

    /**
     * Get the annotated class of the mapping.
     * @return the annotated class of the mapping.
     */
    AnnotatedClass getAnnotatedClass();

    /**
     * Get the score associated with the mapping
     * @return the score associated with the mapping
     */
    double getScore();

    /**
     * Sets the score associated with the mapping
     * @param score The score associated with the mapping
     */
    void setScore(double score);
}
