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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetCUIAltLabelQueryProcessor extends AbstractQueryProcessor<String> {

    private final String conceptURI;
    private static final String CUI_VAR = "cui";

    public GetCUIAltLabelQueryProcessor(final Graph graph, final String conceptURI) {
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
        final List<String> cuis = new ArrayList<>();
        while (hasNextResult()) {
            final QuerySolution qs = nextSolution();
            final RDFNode resultUri = qs.get(CUI_VAR);

            final Pattern pattern = Pattern.compile("(C[0-9][0-9][0-9][0-9][0-9][0-9][0-9])");
            final Matcher matcher = pattern.matcher(resultUri.asLiteral().getString());
            if (matcher.matches()) {
                final String cui = matcher.group(1);
                cuis.add(cui);
            }
        }
        return cuis;
    }
}
