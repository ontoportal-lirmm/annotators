package org.sifrproject.annotatorclient;


import org.sifrproject.annotatorclient.api.BioPortalAnnotatorQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BioportalAnnotatorQueryBuilder {

    public static final BioportalAnnotatorQueryBuilder DEFAULT = new BioportalAnnotatorQueryBuilder();

    private String text = "";
    private final List<String> ontologies = new ArrayList<>();
    private boolean longestOnly = false;
    private boolean excludeNumbers = false;
    private boolean wholeWordOnly = true;
    private boolean excludeSynonyms = false;
    private boolean expandMappings = false;
    private String score = "";
    private boolean expandClassHierarchy = false;
    private int classHierarchyMaxLevel = 0;
    private final List<String> semanticTypes = new ArrayList<>();
    private final List<String> semanticGroups = new ArrayList<>();
    private String format = "json";

    private boolean negation;
    private boolean experiencer;
    private boolean temporality;

    private boolean lemmatize;

    private boolean uniqueGroups;

    private BioportalAnnotatorQueryBuilder() {
    }

    public BioportalAnnotatorQueryBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public BioportalAnnotatorQueryBuilder ontologies(final String... ontology) {
        ontologies.addAll(Arrays.asList(ontology));
        return this;
    }


    public BioportalAnnotatorQueryBuilder longest_only(final boolean longestOnly) {
        this.longestOnly = longestOnly;
        return this;
    }


    public BioportalAnnotatorQueryBuilder exclude_numbers(final boolean excludeNumbers) {
        this.excludeNumbers = excludeNumbers;
        return this;
    }


    public BioportalAnnotatorQueryBuilder whole_word_only(final boolean wholeWordOnly) {
        this.wholeWordOnly = wholeWordOnly;
        return this;
    }


    public BioportalAnnotatorQueryBuilder exclude_synonyms(final boolean excludeSynonyms) {
        this.excludeSynonyms = excludeSynonyms;
        return this;
    }


    public BioportalAnnotatorQueryBuilder expand_mappings(final boolean expandMappings) {
        this.expandMappings = expandMappings;
        return this;
    }


    public BioportalAnnotatorQueryBuilder score(final String score) {
        this.score = score;
        return this;
    }


    public BioportalAnnotatorQueryBuilder expand_class_hierarchy(final boolean expandClassHierarchy) {
        this.expandClassHierarchy = expandClassHierarchy;
        return this;
    }


    public BioportalAnnotatorQueryBuilder class_hierarchy_max_level(final int classHierarchyMaxLevel) {
        this.classHierarchyMaxLevel = classHierarchyMaxLevel;
        return this;
    }


    public BioportalAnnotatorQueryBuilder semantic_types(final String... types) {
        this.semanticTypes.addAll(Arrays.asList(types));
        return this;
    }

    public BioportalAnnotatorQueryBuilder semantic_groups(final String... groups) {
        this.semanticGroups.addAll(Arrays.asList(groups));
        return this;
    }


    public BioportalAnnotatorQueryBuilder format(final String format) {
        this.format = format;
        return this;
    }

    public BioportalAnnotatorQueryBuilder lemmatize(final boolean lemmataze) {
        this.lemmatize = lemmataze;
        return this;
    }

    public BioportalAnnotatorQueryBuilder negation(final boolean negation){
        this.negation=negation;
        return this;
    }

    public BioportalAnnotatorQueryBuilder temporality(final boolean temporality){
        this.temporality=temporality;
        return this;
    }

    public BioportalAnnotatorQueryBuilder experiencer(final boolean experiencer){
        this.experiencer=experiencer;
        return this;
    }

    public BioportalAnnotatorQueryBuilder unique_groups(final boolean uniqueGroups){
        this.uniqueGroups = uniqueGroups;
        return this;
    }

    public BioPortalAnnotatorQuery build() {
        final DefaultBioPortalAnnotatorQuery defaultBioportalAnnotatorQuery = new DefaultBioPortalAnnotatorQuery(text);
        defaultBioportalAnnotatorQuery.setExcludeNumbers(excludeNumbers);
        defaultBioportalAnnotatorQuery.setClassHierarchyMaxLevel(classHierarchyMaxLevel);
        for(final String ontology: ontologies){
            defaultBioportalAnnotatorQuery.addOntology(ontology);
        }
        for(final String semanticType: semanticTypes){
            defaultBioportalAnnotatorQuery.addSemanticType(semanticType);
        }

        for(final String semanticGroup: semanticGroups){
            defaultBioportalAnnotatorQuery.addSemanticGroup(semanticGroup);
        }
        defaultBioportalAnnotatorQuery.setExcludeSynonyms(excludeSynonyms);
        defaultBioportalAnnotatorQuery.setExpandClassHierarchy(expandClassHierarchy);
        defaultBioportalAnnotatorQuery.setExpandMappings(expandMappings);
        defaultBioportalAnnotatorQuery.setLongestOnly(longestOnly);
        defaultBioportalAnnotatorQuery.setWholeWordOnly(wholeWordOnly);
        defaultBioportalAnnotatorQuery.setScore(score);
        defaultBioportalAnnotatorQuery.setFormat(format);
        defaultBioportalAnnotatorQuery.setNegation(negation);
        defaultBioportalAnnotatorQuery.setTemporality(temporality);
        defaultBioportalAnnotatorQuery.setExperiencer(experiencer);
        defaultBioportalAnnotatorQuery.setLemmatize(lemmatize);
        defaultBioportalAnnotatorQuery.setUniqueGroups(uniqueGroups);

        ontologies.clear();
        semanticTypes.clear();
        semanticGroups.clear();
        text="";
        experiencer =false;
        temporality = false;
        negation = false;
        lemmatize=false;
        format="json";
        classHierarchyMaxLevel=0;
        expandClassHierarchy=false;

        score="";
        expandMappings = false;
        excludeSynonyms = false;
        wholeWordOnly = true;
        uniqueGroups = false;

        return defaultBioportalAnnotatorQuery;
    }
}