package org.sifrproject.annotations.model;


import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.umls.UMLSGroup;

import java.util.*;

/**
 * Error/dummy implementation of Annotation, meant to encapsulate errors from the source Bioportal so that it may be
 * displayed appropriately through the generic output generator
 */
public class BioportalErrorAnnotation implements Annotation {

    private final String message;


    /**
     * Build an error annotation
     * @param message The message
     */
    public BioportalErrorAnnotation(final String message) {
        this.message = message;
    }

    @Override
    public AnnotatedClass getAnnotatedClass() {
        return new MyAnnotatedClass();
    }

    @Override
    public AnnotationTokens getAnnotations() {
        return () -> new ArrayList<AnnotationToken>().iterator();
    }

    @Override
    public double getScore() {
        return -1;
    }

    @Override
    public void setScore(final double score) {

    }

    @Override
    public Hierarchy getHierarchy() {
        return () -> new ArrayList<HierarchyElement>().iterator();
    }

    @Override
    public Mappings getMappings() {
        return () -> new ArrayList<Mapping>().iterator();
    }

    @Override
    public String toString() {
        return String.format("[{\"error\":\"%s\"}]", message);
    }

    private static class MyAnnotatedClass implements AnnotatedClass {
        @Override
        public String getId() {
            return "";
        }

        @Override
        public String getType() {
            return "";
        }

        @Override
        public Links getLinks() {
            return null;
        }

        @Override
        public String getContextVocab() {
            return "";
        }

        @Override
        public Set<String> getCuis() {
            return Collections.emptySet();
        }

        @Override
        public List<UMLSGroup> getSemanticGroups() {
            return Collections.emptyList();
        }

        @Override
        public void setSemanticGroups(final List<UMLSGroup> groups) {

        }
    }
}
