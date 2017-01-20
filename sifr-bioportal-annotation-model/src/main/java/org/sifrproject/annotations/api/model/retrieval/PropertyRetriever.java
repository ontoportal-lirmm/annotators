package org.sifrproject.annotations.api.model.retrieval;


import java.util.List;

/**
 * Generic interface that retrieves a list of properties associated with a particular ontology class
 */
public interface PropertyRetriever {
    /**
     * Retrieve some properties bound to the class given by the URI
     * @param URI The URI of the class
     * @return the list of properties
     */
    List<String> retrievePropertyValues(String URI);
}
