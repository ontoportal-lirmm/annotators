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
    public BioportalErrorAnnotation(String message) {
        this.message = message;
    }

    @Override
    public AnnotatedClass getAnnotatedClass() {
        return new AnnotatedClass() {
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
            public Set<UMLSGroup> getSemanticGroups() {
                return Collections.emptySet();
            }
        };
    }

    @Override
    public AnnotationTokens getAnnotations() {
        return new AnnotationTokens() {
            @Override
            public Iterator<AnnotationToken> iterator() {
                return new ArrayList<AnnotationToken>().iterator();
            }
        };
    }

    @Override
    public double getScore() {
        return -1;
    }

    @Override
    public void setScore(double score) {

    }

    @Override
    public Hierarchy getHierarchy() {
        return new Hierarchy() {
            @Override
            public Iterator<HierarchyElement> iterator() {
                return new ArrayList<HierarchyElement>().iterator();
            }
        };
    }

    @Override
    public Mappings getMappings() {
        return new Mappings() {
            @Override
            public Iterator<Mapping> iterator() {
                return new ArrayList<Mapping>().iterator();
            }
        };
    }

    @Override
    public String toString() {
        return String.format("[{\"error\":\"%s\"}]", message);
    }
}
