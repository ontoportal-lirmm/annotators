package org.sifrproject.util;


import io.github.twktheainur.sparqy.graph.storage.JenaRemoteSPARQLStore;
import io.github.twktheainur.sparqy.graph.storage.StoreHandler;
import io.github.twktheainur.sparqy.graph.store.Store;
import org.sifrproject.annotations.api.BioPortalAnnotation;
import org.sifrproject.annotations.api.BioPortalAnnotationFactory;
import org.sifrproject.annotations.api.BioPortalAnnotationParser;
import org.sifrproject.annotations.api.umls.PropertyRetriever;
import org.sifrproject.annotations.model.DefaultBioPortalAnnotationFactory;
import org.sifrproject.annotations.model.LIRMMBioportalAnnotationParser;
import org.sifrproject.annotations.umls.CUIPropertyRetriever;
import org.sifrproject.annotations.umls.SemanticTypePropertyRetriever;
import org.sifrproject.annotations.umls.groups.UMLSGroupIndex;
import org.sifrproject.annotations.umls.groups.UMLSSemanticGroupsLoader;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public final class BRAT {

    private BRAT() {

    }

    public static String convertAnnotationsToBRAT(String annotationOutput, String sparqlServerURI) throws IOException {
        Store store = new JenaRemoteSPARQLStore(sparqlServerURI);
        StoreHandler.registerStoreInstance(store);
        PropertyRetriever cuiRetrieval = new CUIPropertyRetriever();
        PropertyRetriever typeRetrieval = new SemanticTypePropertyRetriever();
        UMLSGroupIndex groupIndex = UMLSSemanticGroupsLoader.load();
        BioPortalAnnotationFactory annotationFactory = new DefaultBioPortalAnnotationFactory();
        BioPortalAnnotationParser bioPortalAnnotations = new LIRMMBioportalAnnotationParser(annotationFactory, cuiRetrieval, typeRetrieval, groupIndex);
        bioPortalAnnotations.parseAnnotations(annotationOutput);
        return toBRAT(bioPortalAnnotations.annotations());
    }

    private static String toBRAT(List<BioPortalAnnotation> annotations) {
        annotations.sort(Comparator.comparingInt(BioPortalAnnotation::getBegin));
        int termCounter = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (BioPortalAnnotation annotation : annotations) {
            stringBuilder.append(String.format("T%d\t%s %d %d\t%s", termCounter, annotation.getSemanticGroup(), annotation.getBegin(), annotation.getEnd(), annotation.getText().toLowerCase())).append("\n");
            stringBuilder.append(String.format("#%d\tAnnotatorNotes T%d\t%s", termCounter, termCounter, annotation.getCuis())).append("\n");
            termCounter++;
        }
        return stringBuilder.toString();
    }

}
