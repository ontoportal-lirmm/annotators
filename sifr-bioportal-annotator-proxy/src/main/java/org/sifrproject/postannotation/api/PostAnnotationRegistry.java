package org.sifrproject.postannotation.api;


import org.sifrproject.annotations.api.model.Annotation;

import java.util.List;

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
    void apply(List<Annotation> annotations, String text);

    /**
     * Remove all registered post-processors
     */
    void clear();
}
