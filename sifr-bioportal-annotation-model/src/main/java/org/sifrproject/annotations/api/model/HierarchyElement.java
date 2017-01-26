package org.sifrproject.annotations.api.model;

/**
 * An element belonging to the hierarchy of the class that corresponds to a particular annotation.
 */
public interface HierarchyElement extends ScoreableElement {
    /**
     * Get the annotatedClass that corresponds to the element of the hierarchy (e.g. superclass)
     * @return The annotatedClass that corresponds to the element of the hierarchy (e.g. superclass)
     */
    AnnotatedClass getAnnotatedClass();

    /**
     * Get the distance in the hierarchy relative to the initial annotation
     * @return the distance in the hierarchy relative to the initial annotation
     */
    int getDistance();

    /**
     * Get the score associated with this hierarchy element
     * @return the score associated with this hierarchy element
     */
    double getScore();

    /**
     * Set the score associated with the hierarchy element
     * @param score The score associated with the hierarchy element
     */
    void setScore(double score);
}
