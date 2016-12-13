package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.api.model.visitor.VisitableAnnotationElement;
import org.sifrproject.annotations.umls.groups.UMLSGroup;

import java.util.Set;

public interface AnnotatedClass extends VisitableAnnotationElement {
    String getId();

    String getType();

    Links getLinks();

    String getContextVocab();

    Set<String> getCuis();

    Set<UMLSGroup> getSemanticGroups();
}
