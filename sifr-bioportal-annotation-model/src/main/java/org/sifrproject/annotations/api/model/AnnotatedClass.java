package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.umls.UMLSGroup;

import java.util.Set;

/**
 * Data interface for an @annotatedClass in the JSON-LD output of Bioportal, identifies the class
 * concerned with an annotation.
 */
public interface AnnotatedClass {
    /**
     * The Id of the annotated class, i.e. its URI
     * @return the URI of the class
     */
    String getId();

    /**
     * The OWL type of the class (the class it inherits) in the ontology, e.g. owl:Class
     * @return The OWL type of the class (the class it inherits) in the ontology, e.g. owl:Class
     */
    String getType();

    /**
     * This gives the context of the class in the ontology (ontology, children, parents, etc).
     * @return The Links instance associated with the Annotated class
     */
    Links getLinks();

    /**
     * This returns a ContextVocab object that specifies the vocabulary used in the description of the class
     * @return The associated ContextVocab instance
     */
    String getContextVocab();

    /**
     *Get the set of UMLS CUIs for this AnnotatedClass
     * @return the set of UMLS CUIs for this AnnotatedClass
     */
    Set<String> getCuis();

    /**
     * Get the UMLS semantic groups that correspond to this AnnotatedClass
     * @return the UMLS semantic groups that correspond to this AnnotatedClass
     */
    Set<UMLSGroup> getSemanticGroups();
}
