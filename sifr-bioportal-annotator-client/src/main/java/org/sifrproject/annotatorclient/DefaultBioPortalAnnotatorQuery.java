package org.sifrproject.annotatorclient;


import org.sifrproject.annotatorclient.api.BioPortalAnnotatorQuery;

import java.util.ArrayList;
import java.util.List;

class DefaultBioPortalAnnotatorQuery implements BioPortalAnnotatorQuery {

//http://services.bioportal.lirmm.fr/annotator?

    private String text; //text=This is some imput text
    private List<String> ontologies; //ontologies=Ontology1,Ontology2,...
    private boolean longestOnly;//longest_only=[true|false]
    private boolean excludeNumbers; // exclude_numbers=[true|false]
    private boolean wholeWordOnly; // whole_word_only=[true|false]
    private boolean excludeSynonyms; // exclude_synonyms=[true|false]
    private boolean expandMappings; // expand_mappings=[true|false]
    private String score; // score=[old|cvalue|cvalueh]
    private boolean expandClassHierarchy; //expand_class_hierarchy=[true|false]
    private int classHierarchyMaxLevel; // class_hierarchy_max_level=[1..10|999] (999 for All)
    private final List<String> semanticTypes; //semantic_types=Type1, Type2,...
    private String format;
    private boolean negation;
    private boolean experiencer;
    private boolean temporality;
    private boolean lemmatize;
    private final List<String> semanticGroups; //semantic_types=Type1, Type2,...
    private boolean uniqueGroups;

    DefaultBioPortalAnnotatorQuery(final String text) {
        this.text = text;
        ontologies = new ArrayList<>();
        semanticTypes = new ArrayList<>();
        semanticGroups = new ArrayList<>();
    }

    @Override
    public void addOntology(final String ontologyName) {
        ontologies.add(ontologyName);
    }

    @Override
    public String generateOntologyString() {
        return generateEnumString(ontologies);
    }

    private String generateEnumString(final Iterable<String> iterable) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ");
        for (final String item : iterable) {
            stringBuilder.append(item).append(",");
        }
        final String builderString = stringBuilder.toString();
        return builderString.substring(0, builderString.length() - 1).trim();
    }

    @Override
    public void addSemanticType(final String semanticType) {
        semanticTypes.add(semanticType);
    }

    @Override
    public String generateSemanticTypesString() {
        return generateEnumString(semanticTypes);
    }

    @Override
    public void setLongestOnly(final boolean longestOnly) {
        this.longestOnly = longestOnly;
    }

    @Override
    public boolean isLongestOnly() {
        return longestOnly;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public boolean isExcludeNumbers() {
        return excludeNumbers;
    }

    @Override
    public void setExcludeNumbers(final boolean excludeNumbers) {
        this.excludeNumbers = excludeNumbers;
    }

    @Override
    public boolean isWholeWordOnly() {
        return wholeWordOnly;
    }

    @Override
    public void setWholeWordOnly(final boolean wholeWordOnly) {
        this.wholeWordOnly = wholeWordOnly;
    }

    @Override
    public boolean isExcludeSynonyms() {
        return excludeSynonyms;
    }

    @Override
    public void setExcludeSynonyms(final boolean excludeSynonyms) {
        this.excludeSynonyms = excludeSynonyms;
    }

    @Override
    public boolean isExpandMappings() {
        return expandMappings;
    }

    @Override
    public void setExpandMappings(final boolean expandMappings) {
        this.expandMappings = expandMappings;
    }

    @Override
    public String getScore() {
        return score;
    }

    @Override
    public void setScore(final String score) {
        this.score = score;
    }

    @Override
    public boolean isExpandClassHierarchy() {
        return expandClassHierarchy;
    }

    @Override
    public void setExpandClassHierarchy(final boolean expandClassHierarchy) {
        this.expandClassHierarchy = expandClassHierarchy;
    }

    @Override
    public int getClassHierarchyMaxLevel() {
        return classHierarchyMaxLevel;
    }

    @Override
    public void setClassHierarchyMaxLevel(final int classHierarchyMaxLevel) {
        this.classHierarchyMaxLevel = classHierarchyMaxLevel;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public void setFormat(final String format) {
        this.format = format;
    }

    @Override
    public boolean isNegation() {
        return negation;
    }

    @Override
    public void setNegation(final boolean negation) {
        this.negation = negation;
    }

    @Override
    public boolean isExperiencer() {
        return experiencer;
    }

    @Override
    public void setExperiencer(final boolean experiencer) {
        this.experiencer = experiencer;
    }

    @Override
    public boolean isTemporality() {
        return temporality;
    }

    @Override
    public void setTemporality(final boolean temporality) {
        this.temporality = temporality;
    }

    @Override
    public void addSemanticGroup(final String semanticGroup) {
        semanticGroups.add(semanticGroup);
    }
    @Override
    public String generateSemanticGroupsString() {
        return generateEnumString(semanticGroups);
    }

    @Override
    public boolean isLemmatize() {
        return lemmatize;
    }

    @Override
    public void setLemmatize(final boolean lemmatize) {
        this.lemmatize = lemmatize;
    }

    @Override
    public void setUniqueGroups(final boolean uniqueGroups){
        this.uniqueGroups = uniqueGroups;
    }

    @Override
    public boolean isUniqueGroups() {
        return uniqueGroups;
    }
}
