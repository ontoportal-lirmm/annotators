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

    /**
     * Get the hierarchy associated with the annotation. For convenience Hierarchy is an {@link Iterable<HierarchyElement>}
     * @return the hierarchy associated with the annotations
     */
    Hierarchy getHierarchy();

    /**
     * Get the mappings associated with the annotation. For convenience Mappings is an {@link Iterable<Mapping>}
     * @return the mappings associated with the annotations
     */
    Mappings getMappings();
}
