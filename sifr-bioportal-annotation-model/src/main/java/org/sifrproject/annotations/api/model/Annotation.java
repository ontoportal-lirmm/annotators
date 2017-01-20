package org.sifrproject.annotations.api.model;

/**
 * Interface specifying the data of an annotation retrieved from the NCBO Bioportal/LIRMM Bioportal Annotator
 */
public interface Annotation extends ScoreableElement {

    /**
     * Get the ontology class that characterizes the annotation
     * @return the ontology class that characterizes the annotation
     */
    AnnotatedClass getAnnotatedClass();

    /**
     * Get the tokens covered by this annotation. AnnotationTokens is an {@code Iterable<AnnotationTokens>} for convenience.
     * @return the tokens covered by this annotation.
     */
    AnnotationTokens getAnnotations();

    double getScore();

    void setScore(double score);

    Hierarchy getHierarchy();

    Mappings getMappings();
}
