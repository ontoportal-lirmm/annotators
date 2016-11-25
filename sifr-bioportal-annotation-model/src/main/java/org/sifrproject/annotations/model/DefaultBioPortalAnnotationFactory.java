package org.sifrproject.annotations.model;


import org.sifrproject.annotations.api.BioPortalAnnotation;
import org.sifrproject.annotations.api.BioPortalAnnotationFactory;

public class DefaultBioPortalAnnotationFactory implements BioPortalAnnotationFactory {

    @Override
    public BioPortalAnnotation createAnnotation(String text, String classId, String ontology, String type, String cuis, double score, int begin, int end) {
        return new DefaultBioPortalAnnotation(text, classId, ontology, type, cuis, score, begin, end);
    }
}
