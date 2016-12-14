package org.sifrproject.annotations.model;


import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.umls.UMLSGroup;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BioportalErrorAnnotation implements Annotation {

    private final String message;

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
        return () -> {
            List<AnnotationToken> dummy;
            dummy = Collections.emptyList();
            return dummy.iterator();
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
        return () -> {
            List<HierarchyElement> dummy;
            dummy = Collections.emptyList();
            return dummy.iterator();
        };
    }

    @Override
    public Mappings getMappings() {
        return () -> {
            List<Mapping> dummy;
            dummy = Collections.emptyList();
            return dummy.iterator();
        };
    }

    @Override
    public String toString() {
        return String.format("[{\"error\":\"%s\"}]", message);
    }
}
