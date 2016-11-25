package org.sifrproject.annotations.api;

/**
 * An abstract factory interface to create {@code BioportalAnnotation} instances
 */
public interface BioPortalAnnotationFactory {
    /**
     * Abstract factory method to create a {@code BioportalAnnotation} instance
     *
     * @param text     The string containing the text overed by the annotation
     * @param classId  The URI of the annotation class
     * @param ontology The URI to the Ontology to which the class belongs
     * @param type     The semantic type of the annotation
     * @param cuis     The UMLS CUIs for this annotation
     * @param score    The score associated with the annotation
     * @param begin    The start character offset of the annotation in the source text
     * @param end      The end character offset of the annotation in the source text
     * @return An appropriate instance of {@code BioPortalAnnotation}
     */
    BioPortalAnnotation createAnnotation(String text, String classId, String ontology, String type, String cuis, double score, int begin, int end);
}
