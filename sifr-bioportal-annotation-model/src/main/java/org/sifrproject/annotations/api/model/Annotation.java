package org.sifrproject.annotations.api.model;

/**
 * Interface specifying the data of an annotation retrieved from the NCBO Bioportal/LIRMM Bioportal Annotator
 */
public interface Annotation extends ScoreableElement {

    AnnotatedClass getAnnotatedClass();

    AnnotationTokens getAnnotations();

    double getScore();

    void setScore(double score);

    Hierarchy getHierarchy();

    Mappings getMappings();
}
