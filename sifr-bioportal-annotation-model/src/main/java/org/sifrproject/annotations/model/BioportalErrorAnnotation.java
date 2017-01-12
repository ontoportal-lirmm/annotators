package org.sifrproject.annotations.model;


import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.umls.UMLSGroup;

import java.util.*;

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
