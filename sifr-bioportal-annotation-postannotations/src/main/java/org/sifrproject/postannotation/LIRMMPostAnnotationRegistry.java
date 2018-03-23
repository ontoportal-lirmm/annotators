package org.sifrproject.postannotation;

import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.postannotation.api.PostAnnotationFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Default implementation of the PostAnnotationRegistry
 */
public class LIRMMPostAnnotationRegistry implements PostAnnotationRegistry {

    private final Collection<PostAnnotationFilter> postAnnotationFilters = new ArrayList<>();

    @Override
    public void registerPostAnnotator(final PostAnnotationFilter postAnnotationFilter) {
        postAnnotationFilters.add(postAnnotationFilter);
    }

    @Override
    public void apply(final List<Annotation> annotations, final String text) {
        for (final PostAnnotationFilter postAnnotationFilter : postAnnotationFilters){
            postAnnotationFilter.postAnnotate(annotations, text);
        }
    }

    @Override
    public void clear() {
        postAnnotationFilters.clear();
    }


}
