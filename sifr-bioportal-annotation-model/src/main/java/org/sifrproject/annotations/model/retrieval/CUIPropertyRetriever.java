package org.sifrproject.annotations.model.retrieval;


import io.github.twktheainur.sparqy.Graph;
import io.github.twktheainur.sparqy.graph.DefaultGraph;
import io.github.twktheainur.sparqy.graph.OWLTBoxModel;
import io.github.twktheainur.sparqy.queries.QueryProcessor;
import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.model.retrieval.queries.GetCUIUMLSPropQueryProcessor;

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

        QueryProcessor<String> cuiProcessor = new GetCUIUMLSPropQueryProcessor(graph, URI);
        cuiProcessor.runQuery();
        cuis.addAll(cuiProcessor.processResults());

        /*QueryProcessor<String> cuiProcessorAlt = new GetCUIAltLabelQueryProcessor(graph, URI);
        cuiProcessorAlt.runQuery();
        cuis.addAll(cuiProcessorAlt.processResults());*/
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
