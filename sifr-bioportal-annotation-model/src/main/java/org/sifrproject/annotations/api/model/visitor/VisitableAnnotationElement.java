package org.sifrproject.annotations.api.model.visitor;

public interface VisitableAnnotationElement {
    void accept(AnnotationVisitor annotationVisitor);
}
