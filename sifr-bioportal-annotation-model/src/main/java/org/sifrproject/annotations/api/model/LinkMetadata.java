package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.api.model.visitor.VisitableAnnotationElement;

public interface LinkMetadata extends VisitableAnnotationElement {
    String getSelf();

    String getOntology();

    String getChildren();

    String getAncestors();

    String getParents();

    String getDescendants();

    String getInstances();

    String getTree();

    String getNotes();

    String getMappings();

    String getUi();
}
