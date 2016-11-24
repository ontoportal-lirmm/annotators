package org.sifrproject.util;


import fr.lirmm.advance.annotatorapi.annotationmodel.BioPortalAnnotations;
import fr.lirmm.advance.annotatorapi.annotationmodel.DefaultBioPortalAnnotationFactory;
import fr.lirmm.advance.annotatorapi.api.BioPortalAnnotation;
import fr.lirmm.advance.annotatorapi.api.BioPortalAnnotationFactory;
import fr.lirmm.advance.annotatorapi.umls.cui.CUIRetrieval;
import fr.lirmm.advance.annotatorapi.umls.groups.UMLSGroupIndex;
import fr.lirmm.advance.annotatorapi.umls.groups.UMLSSemanticGroupsLoader;
import fr.lirmm.advance.annotatorapi.umls.groups.UMLSTypeRetrieval;
import lib.sparqy.graph.storage.JenaRemoteSPARQLStore;
import lib.sparqy.graph.storage.StoreHandler;
import lib.sparqy.graph.store.Store;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class BRAT {

    private BRAT() {

    }

    public static String convertAnnotationsToBRAT(String annotationOutput) throws IOException {
        Store store = new JenaRemoteSPARQLStore("http://sparql.bioportal.lirmm.fr/sparql/");
        StoreHandler.registerStoreInstance(store);
        CUIRetrieval cuiRetrieval = new CUIRetrieval();
        UMLSTypeRetrieval typeRetrieval = new UMLSTypeRetrieval();
        UMLSGroupIndex groupIndex = UMLSSemanticGroupsLoader.load();
        BioPortalAnnotationFactory annotationFactory = new DefaultBioPortalAnnotationFactory();
        BioPortalAnnotations bioPortalAnnotations = new BioPortalAnnotations(annotationFactory, cuiRetrieval, typeRetrieval, groupIndex);
        bioPortalAnnotations.parseJSONAnnotations(annotationOutput);
        return toBRAT(bioPortalAnnotations.annotations());
    }

    private static String toBRAT(List<BioPortalAnnotation> annotations) {
        Collections.sort(annotations, (o1, o2) -> Integer.compare(o1.getBegin(), o2.getBegin()));
        int termCounter = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (BioPortalAnnotation annotation : annotations) {
            stringBuilder.append(String.format("T%d\t%s %d %d\t%s", termCounter, annotation.getType(), annotation.getBegin(), annotation.getEnd(), annotation.getText().toLowerCase())).append("\n");
            stringBuilder.append(String.format("#%d\tAnnotatorNotes T%d\t%s", termCounter, termCounter, annotation.getCuis())).append("\n");
            termCounter++;
        }
        return stringBuilder.toString();
    }

}
