package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.api.model.visitor.VisitableAnnotationElement;

public interface Links extends LinkMetadata, VisitableAnnotationElement {
    LinkMetadata getLinksContextMetadata();
}
