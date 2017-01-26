package org.sifrproject.annotations.api.model;


/**
 * This interface is an alias for LinkMetadata, as in the Bioportal JSON-LD data model, the link object gives
 * the context of the annotated class in the hierarchy, while the context object in the link context gives the context
 * of the direct supertype.
 */
public interface LinkContext extends LinkMetadata {
}
