package org.sifrproject.annotations.umls.queries;


import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.Var;
import io.github.twktheainur.sparqy.Graph;
import io.github.twktheainur.sparqy.queries.ARQSelectQueryImpl;
import io.github.twktheainur.sparqy.queries.AbstractQueryProcessor;

import java.util.ArrayList;
import java.util.List;


public class GetExactMatchesQueryProcessor extends AbstractQueryProcessor<String> {

    private String conceptURI;
    private final String M_VAR = "m";

    public GetExactMatchesQueryProcessor(Graph graph, String conceptURI) {
        super(graph);
        this.conceptURI = conceptURI;
        initialize();
    }

    @Override
    protected void defineQuery() {


        setQuery(new ARQSelectQueryImpl());

        addTriple(NodeFactory.createURI(conceptURI),
                NodeFactory.createURI("http://www.w3.org/2004/02/skos/core#exactMatch"),
                Var.alloc(M_VAR));

        addResultVar(M_VAR);
    }

    @Override
    public List<String> processResults() {
        List<String> matches = new ArrayList<>();
        while (hasNextResult()) {
            QuerySolution qs = nextSolution();
            RDFNode resultUri = qs.get(M_VAR);
            matches.add(resultUri.asResource().getURI());
        }
        return matches;
    }
}
