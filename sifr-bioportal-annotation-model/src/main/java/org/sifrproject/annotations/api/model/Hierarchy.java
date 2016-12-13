package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.api.model.visitor.VisitableAnnotationElement;

public interface Hierarchy extends VisitableAnnotationElement, Iterable<HierarchyElement> {
}
