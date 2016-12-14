package org.sifrproject.annotations.api.model.retrieval;


import java.util.List;

public interface PropertyRetriever {
    List<String> retrievePropertyValues(String URI);
}
