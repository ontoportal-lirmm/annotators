package org.sifrproject.annotations.model.retrieval.queries;


import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.Var;
import org.sparqy.api.Graph;
import org.sparqy.queries.ARQSelectQueryImpl;
import org.sparqy.queries.AbstractQueryProcessor;

import java.util.ArrayList;
import java.util.List;


public class GetUMLSTypeQueryProcessor extends AbstractQueryProcessor<String> {

    private String conceptURI;
    private final String TUI_VAR = "tui";

    public GetUMLSTypeQueryProcessor(Graph graph, String conceptURI) {
        super(graph);
        this.conceptURI = conceptURI;
        initialize();
    }

    @Override
    protected void defineQuery() {


        setQuery(new ARQSelectQueryImpl());

        addTriple(NodeFactory.createURI(conceptURI),
                NodeFactory.createURI("http://bioportal.bioontology.org/ontologies/umls/tui"),
                Var.alloc(TUI_VAR));

        addResultVar(TUI_VAR);
    }

    @Override
    public List<String> processResults() {
        List<String> tuis = new ArrayList<>();
        while (hasNextResult()) {
            QuerySolution qs = nextSolution();
            RDFNode resultUri = qs.get(TUI_VAR);
            tuis.add(resultUri.asLiteral().getString());
        }
        return tuis;
    }
}
