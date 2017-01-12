package org.sparqy.api.queries;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.expr.Expr;

/**
 * Interface for a Select SPARQL Query Wrapper for Jena ARQ
 */
public interface ARQSelectQuery extends ARQQuery {
    /**
     * Sets whether or not the results of the SELECT query should be distinct by adding the DISTINCT keyword
     *
     * @param b Add the distinct keyword to the query?
     */
    @SuppressWarnings("BooleanParameter")
    void setDistinct(boolean b);

    public void addOptionalToWhereStatement(Triple t);

    /**
     * Add a condition triple to the where statement the query
     *
     * @param t The triple to add to the where statement
     */
    void addToWhereStatement(Triple t);

    /**
     * Adds an expression as a filter to the query
     *
     * @param filter The expression to ass as a filter.
     */
    void addFilter(Expr filter);
}
