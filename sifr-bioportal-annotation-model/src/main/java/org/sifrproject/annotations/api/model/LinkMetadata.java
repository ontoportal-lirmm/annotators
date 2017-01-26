package org.sifrproject.annotations.api.model;


/**
 * The metadata for the link object or its @context sub-object
 */
public interface LinkMetadata {
    /**
     * Get the URI of the class being described
     * @return the URI of the class being described
     */
    String getSelf();

    /**
     * Get the ontology to which the class being described belongs to
     * @return the ontology to which the class being described belongs to
     */
    String getOntology();

    /**
     * Get the URI listing the children of the class being described.
     * @return the URI listing the children of the class being described.
     */
    String getChildren();

    /**
     * Get the URI listing the ancestors of the class being described.
     * @return the URI listing the ancestors of the class being described.
     */
    String getAncestors();

    /**
     * Get the URI listing the parents of the class being described.
     * @return the URI listing the parents of the class being described.
     */
    String getParents();

    /**
     * Get the URI listing the descendant of the class being described.
     * @return the URI listing the descendant of the class being described.
     */
    String getDescendants();

    /**
     * Get the URI listing the instances of the class being described.
     * @return the URI listing the instances of the class being described.
     */
    String getInstances();

    /**
     * Get the URI giving the type hierarchy tree of the class being described.
     * @return the URI giving the type hierarchy tree of the class being described.
     */
    String getTree();

    /**
     * Get the URI showing the notes associated to the class being described.
     * @return the URI showing the notes associated to the class being described.
     */
    String getNotes();

    /**
     * Get the URI listing the mappings associated to class being described.
     * @return the URI listing the mappings associated to class being described.
     */
    String getMappings();

    String getUi();
}
