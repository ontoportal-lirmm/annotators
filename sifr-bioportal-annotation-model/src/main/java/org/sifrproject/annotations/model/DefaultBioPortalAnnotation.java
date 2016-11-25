package org.sifrproject.annotations.model;


import org.sifrproject.annotations.api.BioPortalAnnotation;

import java.util.Map;

class DefaultBioPortalAnnotation implements BioPortalAnnotation {
    private String text;
    private String type;
    private String matchType;
    private String classId;
    private String ontology;
    private String semanticGroup;
    private String cuis;
    private double score;
    private int begin;
    private int end;

    private Map<String, String> links;

    DefaultBioPortalAnnotation(String text, String classId, String ontology, String semanticGroup, String cuis, double score, int begin, int end) {
        this.text = text;
        this.classId = classId;
        this.ontology = ontology;
        this.semanticGroup = semanticGroup;
        this.score = score;
        this.begin = begin;
        this.end = end;
        this.cuis = cuis;
    }

    @Override
    public String getText() {
        return text;
    }


    @Override
    public String getClassId() {
        return classId;
    }

    @Override
    public String getOntology() {
        return ontology;
    }


    public String getSemanticGroup() {
        return semanticGroup;
    }


    @Override
    public double getScore() {
        return score;
    }

    @Override
    public int getBegin() {
        return begin;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "DefaultBioPortalAnnotation{" +
                "text='" + text + '\'' +
                ", classId='" + classId + '\'' +
                ", ontology='" + ontology + '\'' +
                ", semanticGroup='" + semanticGroup + '\'' +
                ", score=" + score +
                ", begin=" + begin +
                ", end=" + end +
                '}';
    }

    @Override
    public String getCuis() {
        return cuis;
    }
}
