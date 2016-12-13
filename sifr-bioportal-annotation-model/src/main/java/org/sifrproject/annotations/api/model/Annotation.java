package org.sifrproject.annotations.api.model;

import org.sifrproject.annotations.api.model.visitor.VisitableAnnotationElement;

/**
 * Interface specifying the data of an annotation retrieved from the NCBO Bioportal/LIRMM Bioportal Annotator
 */
public interface Annotation extends VisitableAnnotationElement, ScoreableElement {

    AnnotatedClass getAnnotatedClass();

    AnnotationTokens getAnnotations();

    double getScore();


    void setScore(double score);

    Hierarchy getHierarchy();

    Mappings getMappings();
}
