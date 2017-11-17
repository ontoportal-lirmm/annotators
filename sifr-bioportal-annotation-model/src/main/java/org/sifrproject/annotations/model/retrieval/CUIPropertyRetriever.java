package org.sifrproject.annotations.model.retrieval;


import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.model.retrieval.queries.GetCUIAltLabelQueryProcessor;
import org.sifrproject.annotations.model.retrieval.queries.GetCUIUMLSPropQueryProcessor;
import org.sparqy.api.Graph;
import org.sparqy.api.queries.QueryProcessor;
import org.sparqy.graph.DefaultGraph;
import org.sparqy.graph.OWLTBoxModel;
import org.sparqy.graph.storage.StoreHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CUIPropertyRetriever implements PropertyRetriever {

    private Graph graph;

    public CUIPropertyRetriever() throws IOException {
        graph = new DefaultGraph("http://purl.lirmm.fr/ontology/", new OWLTBoxModel());
    }

    private List<String> getFlatCUIS(String URI) {
        List<String> cuis = new ArrayList<>();

        if (StoreHandler.getStore()!=null) {
            QueryProcessor<String> cuiProcessor = new GetCUIUMLSPropQueryProcessor(graph, URI);
            cuiProcessor.runQuery();
            cuis.addAll(cuiProcessor.processResults());

            QueryProcessor<String> cuiProcessorAlt = new GetCUIAltLabelQueryProcessor(graph, URI);
            cuiProcessorAlt.runQuery();
            cuis.addAll(cuiProcessorAlt.processResults());
        }
        return cuis;
    }

    @Override
    public List<String> retrievePropertyValues(String URI) {
        List<String> cuis = new ArrayList<>();

        cuis.addAll(getFlatCUIS(URI));

        /*QueryProcessor<String> matchProcessor = new GetExactMatchesQueryProcessor(graph,URI);
        matchProcessor.runQuery();
        for(String matchURI: matchProcessor.processResults()){
            cuis.addAll(getFlatCUIS(matchURI));
        }*/
        return cuis;
    }
}
