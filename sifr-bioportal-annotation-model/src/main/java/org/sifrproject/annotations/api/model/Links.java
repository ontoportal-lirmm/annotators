package org.sifrproject.annotations.api.model;


/**
 * This interface models the links object in the JSON-LD output of Bioportal
 */
public interface Links extends LinkMetadata {
    /**
     * Get the context metadata of the links object
     * @return the context metadata of the links object
     */
    LinkMetadata getLinksContextMetadata();
}
