package org.sifrproject.annotations.api;

/**
 * Interface specifying the data of an annotation retrieved from the NCBO Bioportal/LIRMM Bioportal Annotator
 */
public interface BioPortalAnnotation {
    /**
     * Get the annotation class identifier (URI)
     *
     * @return The class identifier
     */
    String getClassId();

    /**
     * Get the URI of the ontology to which the class belongs
     *
     * @return The uri of the ontology
     */
    String getOntology();

    /**
     * Get the type of the annotation, ( e.g. a UMLS semantic group)
     *
     * @return The annotation type
     */
    String getSemanticGroup();

    /**
     * The text representation of the token as found in the source text
     *
     * @return The text representation of the token as found in the source text.
     */
    String getText();

    /**
     * The score associated with the annotation if any
     *
     * @return The score of the annotation, -1 if none, a strictly positive value otherwise
     */
    double getScore();

    /**
     * Start position of the token within the source text
     *
     * @return The start position of the token (number of characters)
     */
    int getBegin();

    /**
     * End position of the token within the source text
     *
     * @return The end position of the token (number of characters)
     */
    int getEnd();

    /**
     * Get a string representing the comma séparated list of UMLS CUIs
     *
     * @return The comma séparated list of UMLS CUIs or an empty string if none
     */
    String getCuis();
}
