package org.sifrproject.postannotation;

import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.postannotation.api.PostAnnotationFilter;

import java.util.List;

public class FetchUMLSSemanticInformationPostAnnotationFilter implements PostAnnotationFilter {
    @Override
    public void postAnnotate(final List<Annotation> annotations, final String text) {
        for(final Annotation annotation: annotations){
            fetchTypesAndCUIs(annotation.getAnnotatedClass());
        }
    }

    private void fetchTypesAndCUIs(final AnnotatedClass annotatedClass){
        annotatedClass.getSemanticGroups();
        annotatedClass.getCuis();
    }
}
