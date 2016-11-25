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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetCUIAltLabelQueryProcessor extends AbstractQueryProcessor<String> {

    private String conceptURI;
    private final String CUI_VAR = "cui";

    public GetCUIAltLabelQueryProcessor(Graph graph, String conceptURI) {
        super(graph);
        this.conceptURI = conceptURI;
        initialize();
    }

    @Override
    protected void defineQuery() {


        setQuery(new ARQSelectQueryImpl());

        addTriple(NodeFactory.createURI(conceptURI),
                NodeFactory.createURI("http://www.w3.org/2004/02/skos/core#altLabel"),
                Var.alloc(CUI_VAR));

        addResultVar(CUI_VAR);
    }

    @Override
    public List<String> processResults() {
        List<String> cuis = new ArrayList<>();
        while (hasNextResult()) {
            QuerySolution qs = nextSolution();
            RDFNode resultUri = qs.get(CUI_VAR);

            Pattern pattern = Pattern.compile("(C[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9])");
            Matcher matcher = pattern.matcher(resultUri.asLiteral().getString());
            if (matcher.matches()) {
                String cui = matcher.group(1);
                cuis.add(cui);
            }
        }
        return cuis;
    }
}
