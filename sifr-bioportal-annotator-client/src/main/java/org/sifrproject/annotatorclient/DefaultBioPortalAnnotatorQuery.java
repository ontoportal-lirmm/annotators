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
    private List<String> semanticTypes; //semantic_types=Type1, Type2,...
    private String format;
    private boolean negation;
    private boolean experiencer;
    private boolean temporality;
    private boolean lemmatize;
    private final List<String> semanticGroups; //semantic_types=Type1, Type2,...


    DefaultBioPortalAnnotatorQuery(String text) {
        this.text = text;
        ontologies = new ArrayList<>();
        semanticTypes = new ArrayList<>();
        semanticGroups = new ArrayList<>();
    }

    @Override
    public void addOntology(String ontologyName) {
        ontologies.add(ontologyName);
    }

    @Override
    public String generateOntologyString() {
        return generateEnumString(ontologies);
    }

    private String generateEnumString(Iterable<String> iterable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ");
        for (String item : iterable) {
            stringBuilder.append(item).append(",");
        }
        String string = stringBuilder.toString();
        return string.substring(0, string.length() - 1).trim();
    }

    @Override
    public void addSemanticType(String semanticType) {
        semanticTypes.add(semanticType);
    }

    @Override
    public String generateSemanticTypesString() {
        return generateEnumString(semanticTypes);
    }

    @Override
    public void setLongestOnly(boolean longestOnly) {
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
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean isExcludeNumbers() {
        return excludeNumbers;
    }

    @Override
    public void setExcludeNumbers(boolean excludeNumbers) {
        this.excludeNumbers = excludeNumbers;
    }

    @Override
    public boolean isWholeWordOnly() {
        return wholeWordOnly;
    }

    @Override
    public void setWholeWordOnly(boolean wholeWordOnly) {
        this.wholeWordOnly = wholeWordOnly;
    }

    @Override
    public boolean isExcludeSynonyms() {
        return excludeSynonyms;
    }

    @Override
    public void setExcludeSynonyms(boolean excludeSynonyms) {
        this.excludeSynonyms = excludeSynonyms;
    }

    @Override
    public boolean isExpandMappings() {
        return expandMappings;
    }

    @Override
    public void setExpandMappings(boolean expandMappings) {
        this.expandMappings = expandMappings;
    }

    @Override
    public String getScore() {
        return score;
    }

    @Override
    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public boolean isExpandClassHierarchy() {
        return expandClassHierarchy;
    }

    @Override
    public void setExpandClassHierarchy(boolean expandClassHierarchy) {
        this.expandClassHierarchy = expandClassHierarchy;
    }

    @Override
    public int getClassHierarchyMaxLevel() {
        return classHierarchyMaxLevel;
    }

    @Override
    public void setClassHierarchyMaxLevel(int classHierarchyMaxLevel) {
        this.classHierarchyMaxLevel = classHierarchyMaxLevel;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public boolean isNegation() {
        return negation;
    }

    @Override
    public void setNegation(boolean negation) {
        this.negation = negation;
    }

    @Override
    public boolean isExperiencer() {
        return experiencer;
    }

    @Override
    public void setExperiencer(boolean experiencer) {
        this.experiencer = experiencer;
    }

    @Override
    public boolean isTemporality() {
        return temporality;
    }

    @Override
    public void setTemporality(boolean temporality) {
        this.temporality = temporality;
    }

    @Override
    public void addSemanticGroup(String semanticGroup) {
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
    public void setLemmatize(boolean lemmatize) {
        this.lemmatize = lemmatize;
    }
}
