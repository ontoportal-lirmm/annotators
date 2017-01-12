package org.sparqy.api.queries;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.expr.Expr;
import org.sparqy.api.Graph;


/**
 * Wrapper for creating and running queries through Jena ARQ
 */
public interface ARQQuery {

    /**
     * Run a SPARQL Query
     *
     * @param limit Maximum number of results retrieved
     * @return Returns a set of a number <code>limit</code> of the results
     */
    ResultSet runQuery(int limit);

    /**
     * Run a SPARQL Query
     *
     * @return Returns a set of all the results
     */
    ResultSet runQuery();

    /**
     * Adds a variable as a annotresult variable in the ResultSet
     *
     * @param var The variable name to add
     */
    void addResult(String var);

    /**
     * Adds a from statement having for target the URI in <code>Graph</code> <code>g</code>
     *
     * @param g the graph the uri of which is to be added to the query
     */
    void addToFromStatement(Graph g);

    /**
     * Initialize the query on <code>Graph</code> <code>g</code> with where statement <code>Triple</code> defined in <code>triples</code>
     * and annotresult variables defined in <code>resultVars</code>
     *
     * @param g          The <code>Graph</code> from which to query
     * @param triples    The triples to add to the WHERE statement
     * @param resultVars Result variables to consider for the output
     * @return The results of the query
     */
    public ResultSet runQuery(Graph g, Iterable<Triple> triples, Iterable<Triple> optionalTriples, Iterable<String> resultVars);

    /**
     * Initialize the query on <code>Graph</code> <code>g</code> with where statement <code>Triple</code> defined in <code>triples</code>
     * and annotresult variables defined in <code>resultVars</code>. <code>filters</code> is a list of FILTER expressions for
     * the query.
     *
     * @param g          The <code>Graph</code> from which to query
     * @param triples    The triples to add to the WHERE statement
     * @param resultVars Result variables to consider for the output
     * @param filters    The filter list.
     * @return The results of the query
     */
    public ResultSet runQuery(Graph g, Iterable<Triple> triples, Iterable<Triple> optionalTriples, Iterable<String> resultVars, Iterable<Expr> filters);

    /**
     * Initialize the query on <code>Graph</code> <code>g</code> with where statement <code>Triple</code> defined in <code>triples</code>
     * and annotresult variables defined in <code>resultVars</code>
     *
     * @param g          The <code>Graph</code> from which to query
     * @param triples    The triples to add to the WHERE statement
     * @param resultVars Result variables to consider for the output
     */
    public void initialize(Graph g, Iterable<Triple> triples, Iterable<Triple> optionalTriples, Iterable<String> resultVars);

    /**
     * Initialize the query on <code>Graph</code> <code>g</code> with where statement <code>Triple</code> defined in <code>triples</code>
     * and annotresult variables defined in <code>resultVars</code>. <code>filters</code> is a list of FILTER expressions for
     * the query.
     *
     * @param g          The <code>Graph</code> from which to query.
     * @param triples    The triples to add to the WHERE statement.
     * @param resultVars Result variables to consider for the output.
     * @param filters    The filter list.
     */
    public void initialize(Graph g, Iterable<Triple> triples, Iterable<Triple> optionalTriples, Iterable<String> resultVars, Iterable<Expr> filters);

}
