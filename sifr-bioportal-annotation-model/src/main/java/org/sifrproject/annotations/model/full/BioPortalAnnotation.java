package org.sifrproject.annotations.model.full;


import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class BioPortalAnnotation implements Annotation {

    private AnnotatedClass annotatedClass;

    private List<HierarchyElement> hierarchyList;
    private List<AnnotationToken> annotationsList;
    private List<Mapping> mappingsList;

    private Hierarchy hierarchy = new Hierarchy() {
        @Override
        public Iterator<HierarchyElement> iterator() {
            return hierarchyList.iterator();
        }

        @Override
        public void accept(AnnotationVisitor annotationVisitor) {
            annotationVisitor.visitBefore(this);
            for (HierarchyElement hierarchyElement : hierarchyList) {
                hierarchyElement.accept(annotationVisitor);
            }
            annotationVisitor.visitAfter(this);
        }
    };

    private AnnotationTokens annotationTokens = new AnnotationTokens() {
        @Override
        public Iterator<AnnotationToken> iterator() {
            return annotationsList.iterator();
        }

        @Override
        public void accept(AnnotationVisitor annotationVisitor) {
            annotationVisitor.visitBefore(this);
            for (AnnotationToken annotationToken : annotationsList) {
                annotationToken.accept(annotationVisitor);
            }
            annotationVisitor.visitAfter(this);
        }
    };

    private Mappings mappings = new Mappings() {
        @Override
        public Iterator<Mapping> iterator() {
            return mappingsList.iterator();
        }

        @Override
        public void accept(AnnotationVisitor annotationVisitor) {
            annotationVisitor.visitBefore(this);
            for (Mapping mapping : mappingsList) {
                mapping.accept(annotationVisitor);
            }
            annotationVisitor.visitAfter(this);
        }
    };


    private double score;


    BioPortalAnnotation(AnnotatedClass annotatedClass, List<HierarchyElement> hierarchy, List<AnnotationToken> annotations, List<Mapping> mappings) {
        this(annotatedClass, hierarchy, annotations, mappings, -1);
    }

    BioPortalAnnotation(AnnotatedClass annotatedClass, List<HierarchyElement> hierarchy, List<AnnotationToken> annotations, List<Mapping> mappings, double score) {
        this.annotatedClass = annotatedClass;
        this.score = score;
        this.annotationsList = Collections.unmodifiableList(annotations);
        this.hierarchyList = Collections.unmodifiableList(hierarchy);
        this.mappingsList = Collections.unmodifiableList(mappings);
    }

    @Override
    public AnnotatedClass getAnnotatedClass() {
        return annotatedClass;
    }

    public AnnotationTokens getAnnotations() {
        return annotationTokens;
    }

    @Override
    public double getScore() {
        return score;
    }


    @Override
    public void setScore(double score) {
        this.score = score;
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public Mappings getMappings() {
        return mappings;
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        annotatedClass.accept(annotationVisitor);
        annotationTokens.accept(annotationVisitor);
        hierarchy.accept(annotationVisitor);
        mappings.accept(annotationVisitor);
        annotationVisitor.visitAfter(this);

    }
}
