package org.sifrproject.postannotation;


/**
 * Specification of a registry for {@link PostAnnotator} components that allows registering and chain-applying the
 * post annotators.
 */
public interface PostAnnotationRegistry {
    /**
     * Register a {@link PostAnnotator}
     *
     * @param postAnnotator The post annotator instance
     */
    void registerPostAnnotator(PostAnnotator postAnnotator);

    /**
     * Apply all the registered post-annotators in their order of registration
     */
    void apply();
}
