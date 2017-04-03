package org.sifrproject.annotatorclient;


import org.sifrproject.annotatorclient.api.BioPortalAnnotatorQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BioportalAnnotatorQueryBuilder {

    public static final BioportalAnnotatorQueryBuilder DEFAULT = new BioportalAnnotatorQueryBuilder();

    private String text = "";
    private List<String> ontologies = new ArrayList<>();
    private boolean longestOnly = false;
    private boolean excludeNumbers = false;
    private boolean wholeWordOnly = true;
    private boolean excludeSynonyms = false;
    private boolean expandMappings = false;
    private String score = "";
    private boolean expandClassHierarchy = false;
    private int classHierarchyMaxLevel = 0;
    private List<String> semanticTypes = new ArrayList<>();
    private List<String> semanticGroups = new ArrayList<>();
    private String format = "json";

    private boolean negation;
    private boolean experiencer;
    private boolean temporality;

    private boolean lemmatize;

    private BioportalAnnotatorQueryBuilder() {
    }

    public BioportalAnnotatorQueryBuilder text(String text) {
        this.text = text;
        return this;
    }

    public BioportalAnnotatorQueryBuilder ontologies(String... ontology) {
        ontologies.addAll(Arrays.asList(ontology));
        return this;
    }


    public BioportalAnnotatorQueryBuilder longest_only(boolean longestOnly) {
        this.longestOnly = longestOnly;
        return this;
    }


    public BioportalAnnotatorQueryBuilder exclude_numbers(boolean excludeNumbers) {
        this.excludeNumbers = excludeNumbers;
        return this;
    }


    public BioportalAnnotatorQueryBuilder whole_word_only(boolean wholeWordOnly) {
        this.wholeWordOnly = wholeWordOnly;
        return this;
    }


    public BioportalAnnotatorQueryBuilder exclude_synonyms(boolean excludeSynonyms) {
        this.excludeSynonyms = excludeSynonyms;
        return this;
    }


    public BioportalAnnotatorQueryBuilder expand_mappings(boolean expandMappings) {
        this.expandMappings = expandMappings;
        return this;
    }


    public BioportalAnnotatorQueryBuilder score(String score) {
        this.score = score;
        return this;
    }


    public BioportalAnnotatorQueryBuilder expand_class_hierarchy(boolean expandClassHierarchy) {
        this.expandClassHierarchy = expandClassHierarchy;
        return this;
    }


    public BioportalAnnotatorQueryBuilder class_hierarchy_max_level(int classHierarchyMaxLevel) {
        this.classHierarchyMaxLevel = classHierarchyMaxLevel;
        return this;
    }


    public BioportalAnnotatorQueryBuilder semantic_types(String... types) {
        this.semanticTypes.addAll(Arrays.asList(types));
        return this;
    }

    public BioportalAnnotatorQueryBuilder semantic_groups(String... groups) {
        this.semanticGroups.addAll(Arrays.asList(groups));
        return this;
    }


    public BioportalAnnotatorQueryBuilder format(String format) {
        this.format = format;
        return this;
    }

    public BioportalAnnotatorQueryBuilder lemmatize(boolean lemmataze) {
        this.lemmatize = lemmataze;
        return this;
    }

    public BioportalAnnotatorQueryBuilder negation(boolean negation){
        this.negation=negation;
        return this;
    }

    public BioportalAnnotatorQueryBuilder temporality(boolean temporality){
        this.temporality=temporality;
        return this;
    }

    public BioportalAnnotatorQueryBuilder experiencer(boolean experiencer){
        this.experiencer=experiencer;
        return this;
    }

    public BioPortalAnnotatorQuery build() {
        DefaultBioPortalAnnotatorQuery defaultBioportalAnnotatorQuery = new DefaultBioPortalAnnotatorQuery(text);
        defaultBioportalAnnotatorQuery.setExcludeNumbers(excludeNumbers);
        defaultBioportalAnnotatorQuery.setClassHierarchyMaxLevel(classHierarchyMaxLevel);
        for(String ontology: ontologies){
            defaultBioportalAnnotatorQuery.addOntology(ontology);
        }
        for(String semanticType: semanticTypes){
            defaultBioportalAnnotatorQuery.addSemanticType(semanticType);
        }

        for(String semanticGroup: semanticGroups){
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

        return defaultBioportalAnnotatorQuery;
    }
}