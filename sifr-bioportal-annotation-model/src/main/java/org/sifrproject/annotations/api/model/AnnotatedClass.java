package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.umls.UMLSGroup;

import java.util.Set;

public interface AnnotatedClass {
    String getId();

    String getType();

    Links getLinks();

    String getContextVocab();

    Set<String> getCuis();

    Set<UMLSGroup> getSemanticGroups();
}
