package org.sifrproject.annotations.api.model.retrieval;


/**
 * Generic interface that retrieves a list of properties associated with a particular ontology class
 */
@FunctionalInterface
public interface UMLSPropertyRetriever {
    /**
     * Retrieve some properties bound to the class given by the URI
     * @param URI The URI of the class
     * @return the list of properties
     */
    UMLSProperties retrievePropertyValues(String URI);


}
