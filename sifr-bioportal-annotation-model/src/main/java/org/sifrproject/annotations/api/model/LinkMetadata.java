package org.sifrproject.annotations.api.model;


public interface LinkMetadata {
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
