package org.sparqy.api.graph.store;

import com.hp.hpl.jena.query.ResultSet;

public interface QueryCache {
    public ResultSet retrieveResult(String query);

    public void addResult(String query, ResultSet resultSet);

    public boolean hasResult(String query);

    public void invalidateResult(String query);

    public void invalidateAll();

    public void purge();
}
