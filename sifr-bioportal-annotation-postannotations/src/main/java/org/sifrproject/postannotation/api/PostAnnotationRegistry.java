package org.sifrproject.postannotation.api;


import org.sifrproject.annotations.api.model.Annotation;

import java.util.List;

/**
 * Specification of a registry for {@link PostAnnotationFilter} components that allows registering and chain-applying the
 * post annotators.
 */
public interface PostAnnotationRegistry {
    /**
     * Register a {@link PostAnnotationFilter}
     *
     * @param postAnnotationFilter The post annotator instance
     */
    void registerPostAnnotator(PostAnnotationFilter postAnnotationFilter);

    /**
     * Apply all the registered post-annotators in their order of registration
     */
    void apply(List<Annotation> annotations, String text);

    /**
     * Remove all registered post-processors
     */
    void clear();
}
