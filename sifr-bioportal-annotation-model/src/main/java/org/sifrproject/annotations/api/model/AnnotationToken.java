package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.api.model.context.ContextAnnotationElement;

public interface AnnotationToken extends ContextAnnotationElement {
    int getFrom();

    int getTo();

    String getMatchType();

    String getText();

}
