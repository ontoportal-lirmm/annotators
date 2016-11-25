package org.sifrproject.annotations.api.umls;


import java.util.List;

public interface PropertyRetriever {
    List<String> retrievePropertyValues(String URI);
}
