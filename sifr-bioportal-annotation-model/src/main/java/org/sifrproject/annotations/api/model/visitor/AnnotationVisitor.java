package org.sifrproject.annotations.api.model.visitor;

import org.sifrproject.annotations.api.model.*;

public interface AnnotationVisitor {
    void visitBefore(Annotation dispatcher);

    void visitAfter(Annotation dispatcher);

    void visitBefore(AnnotatedClass dispatcher);

    void visitAfter(AnnotatedClass dispatcher);

    void visitBefore(AnnotationToken dispatcher);

    void visitAfter(AnnotationToken dispatcher);

    void visitBefore(AnnotationTokens dispatcher);

    void visitAfter(AnnotationTokens dispatcher);

    void visitBefore(Hierarchy dispatcher);

    void visitAfter(Hierarchy dispatcher);

    void visitBefore(HierarchyElement dispatcher);

    void visitAfter(HierarchyElement dispatcher);

    void visitBefore(LinkContext dispatcher);

    void visitAfter(LinkContext dispatcher);

    void visitBefore(Links dispatcher);

    void visitAfter(Links dispatcher);

    void visitBefore(Mapping dispatcher);

    void visitAfter(Mapping dispatcher);

    void visitBefore(Mappings dispatcher);

    void visitAfter(Mappings dispatcher);
}
