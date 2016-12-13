package org.sifrproject.annotations.api.model;

import org.sifrproject.annotations.umls.groups.UMLSGroup;

import java.util.List;
import java.util.Set;

/**
 * An abstract factory interface to create {@code BioportalAnnotation} instances
 */
public interface AnnotationFactory {
    /**
     * Abstract factory method to create a {@code BioportalAnnotation} instance
     *
     * @param score          The score associated with the annotation
     * @param annotatedClass An instance of AnnotatedClass containing the annotation meta information
     * @param annotations    The list of {@code TokenAnnotation} instances containing the delimitation of matching tokens, their type type and the text covered
     * @param hierarchy      The list of classes belonging to the hierarchy of the annotated class
     * @return An appropriate instance of {@code Annotation}
     */
    Annotation createAnnotation(AnnotatedClass annotatedClass, List<AnnotationToken> annotations, List<HierarchyElement> hierarchy, List<Mapping> mappings, double score);

    /**
     * Abstract factory method to create a {@code BioportalAnnotation} instance
     *
     * @param annotatedClass An instance of AnnotatedClass containing the annotation meta information
     * @param annotations    The list of {@code TokenAnnotation} instances containing the delimitation of matching tokens, their type type and the text covered
     * @param hierarchy      The list of classes belonging to the hierarchy of the annotated class
     * @return An appropriate instance of {@code Annotation}
     */
    Annotation createAnnotation(AnnotatedClass annotatedClass, List<AnnotationToken> annotations, List<HierarchyElement> hierarchy, List<Mapping> mappings);


    AnnotatedClass createAnnotatedClass(String id, String type, Links links, String contextVocab, Set<String> cuis, Set<UMLSGroup> semanticGroups);


    /**
     * Creates an instance of {@code {@link AnnotationToken}}
     *
     * @param from      start position character offset of the token in the source text.
     * @param to        end position character offset of the token in the source text.
     * @param matchType the type of the match
     * @param text      the text matched corresponding to the token
     * @return the {@code {@link AnnotationToken}} instance
     */
    AnnotationToken createAnnotationToken(int from, int to, String matchType, String text);


    /**
     * Creates an instance of {@code {@link HierarchyElement}}
     *
     * @param annotatedClass The annotated class describing the hierarchy element
     * @param distance       The distance in the hierarchy from the annotated concept
     * @param score          the score associated to the hierarchy element
     * @return The instance of {@code {@link HierarchyElement}}
     */
    HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, int distance, double score);

    /**
     * Creates an instance of {@code {@link HierarchyElement}}
     *
     * @param annotatedClass The annotated class describing the hierarchy element
     * @param distance       The distance in the hierarchy from the annotated concept
     * @return The instance of {@code {@link HierarchyElement}}
     */
    HierarchyElement createHierarchyElement(AnnotatedClass annotatedClass, int distance);


    Links createLinks(LinkMetadata linksMetadata, LinkMetadata linksContextMetadata);

    LinkMetadata createLinkMetadata(String self, String ontology, String children, String parents, String descendants, String ancestors, String instances,
                                    String tree, String notes, String mappings, String ui);


    /**
     * Adds a mapping to the referenced annotated class
     *
     * @param annotatedClass The annotated class corresponding to the destination of the mapping.
     * @param score          The score associated with the mapping
     * @return The {@code {@link Mapping}} instance
     */
    Mapping createMapping(AnnotatedClass annotatedClass, double score);

    /**
     * Adds a mapping to the corresponding Annotation
     *
     * @param annotatedClass The annotated class corresponding to the destination of the mapping.
     * @return The {@code {@link Mapping}} instance
     */
    Mapping createMapping(AnnotatedClass annotatedClass);

}
