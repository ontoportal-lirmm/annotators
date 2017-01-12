/**
 *
 */
package org.sparqy.graph.storage;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparqy.api.graph.store.Store;

import java.io.IOException;

public class JenaMemoryStore implements Store {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Model model;

    public JenaMemoryStore(String aBoxFile) throws IOException {
        model = ModelFactory.createOntologyModel();
        model.read(aBoxFile);
    }

    @Override
    public ResultSet runQuery(Query q) {
        ResultSet rs = null;
        QueryExecution queryExecution = QueryExecutionFactory.create(q, model);
        if (StoreHandler.DEBUG_ON) {
            logger.info(queryExecution.getQuery().toString(Syntax.defaultSyntax));
        }
        try {
            rs = queryExecution.execSelect();
        } catch (RuntimeException e) {
            logger.error(e.getLocalizedMessage());
        }
        return rs;
    }

    @Override
    public Model getABox() {
        return model;
    }

    @Override
    public synchronized void close() {
        model.commit();
        model.close();
    }

    @SuppressWarnings("BooleanParameter")
    @Override
    public void setCachingEnabled(boolean cachingEnabled) {

    }
}
