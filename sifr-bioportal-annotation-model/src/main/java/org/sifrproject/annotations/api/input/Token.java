package org.sifrproject.annotations.api.input;


import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;

import java.util.Collection;

public interface Token extends Comparable<Token> {
    AnnotationToken getAnnotationToken();
    void addAnnotation(Annotation annotation);
    Collection<Annotation> getAnnotations();
}
