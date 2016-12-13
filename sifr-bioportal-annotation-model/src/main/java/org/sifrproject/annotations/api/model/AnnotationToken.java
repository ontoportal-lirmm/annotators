package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.api.model.context.ContextAnnotationElement;
import org.sifrproject.annotations.api.model.visitor.VisitableAnnotationElement;

public interface AnnotationToken extends VisitableAnnotationElement, ContextAnnotationElement {
    int getFrom();

    int getTo();

    String getMatchType();

    String getText();

}
