package org.sifrproject.annotations.model.retrieval;


import io.github.twktheainur.sparqy.Graph;
import io.github.twktheainur.sparqy.graph.DefaultGraph;
import io.github.twktheainur.sparqy.graph.OWLTBoxModel;
import io.github.twktheainur.sparqy.queries.QueryProcessor;
import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.model.retrieval.queries.GetUMLSTypeQueryProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SemanticTypePropertyRetriever implements PropertyRetriever {

    private Graph graph;

    public SemanticTypePropertyRetriever() throws IOException {
        graph = new DefaultGraph("http://purl.lirmm.fr/ontology/", new OWLTBoxModel());
    }

    @Override
    public List<String> retrievePropertyValues(String URI) {
        List<String> tuis = new ArrayList<>();

        QueryProcessor<String> tuiProcessor = new GetUMLSTypeQueryProcessor(graph, URI);
        tuiProcessor.runQuery();
        tuis.addAll(tuiProcessor.processResults());

        return tuis;
    }
}
