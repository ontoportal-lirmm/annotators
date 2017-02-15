package org.sifrproject.annotatorclient.api;


/**
 * Interface specifying the datamodel of the Bioportal Annotator REST API
 */
public interface BioPortalAnnotatorQuery {

    /**
     * Add an ontology by name to restrict the annotations produced. {@code ontologies} REST parameter.
     *
     * @param ontologyName The name of the ontology (acronym)
     */
    void addOntology(String ontologyName);

    /**
     * Generate the comma separated list of ontologies restricting the annotations.
     *
     * @return the comma separated list of ontologies restricting the annotations or an empty string when no were added.
     */
    String generateOntologyString();

    /**
     * Add a UMLS sémantic type (e.g. T021) to restrict the output annotations to this type.
     *
     * @param semanticType The semantic type to add
     */
    void addSemanticType(String semanticType);

    /**
     * Generate the comma-separated list of semanticTypes from the group restructions and previous semantic type
     * restrictions
     * @return The comma-separated list of semanticTypes
     */
    String generateSemanticTypesString();

    void setLongestOnly(boolean longestOnly);

    boolean isLongestOnly();

    String getText();

    void setText(String text);

    boolean isExcludeNumbers();

    void setExcludeNumbers(boolean excludeNumbers);

    boolean isWholeWordOnly();

    void setWholeWordOnly(boolean wholeWordOnly);

    boolean isExcludeSynonyms();

    void setExcludeSynonyms(boolean excludeSynonyms);

    boolean isExpandMappings();

    void setExpandMappings(boolean expandMappings);

    String getScore();

    void setScore(String score);

    boolean isExpandClassHierarchy();

    void setExpandClassHierarchy(boolean expandClassHierarchy);

    int getClassHierarchyMaxLevel();

    void setClassHierarchyMaxLevel(int classHierarchyMaxLevel);

    String getFormat();

    void setFormat(String format);

    boolean isNegation();

    void setNegation(boolean negation);

    boolean isExperiencer();

    void setExperiencer(boolean experiencer);

    boolean isTemporality();

    void setTemporality(boolean temporality);

    void addSemanticGroup(String semanticGroup);
    String generateSemanticGroupsString();
}
