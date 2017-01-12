package org.sparqy.graph.storage;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CachableResultSet implements ResultSet {
    private List<QuerySolution> querySolutionList;
    private List<String> resultVars;
    private Model model;

    public CachableResultSet(ResultSet originalSet) {
        querySolutionList = new ArrayList<>();
        resultVars = new ArrayList<>();
        resultVars.addAll(originalSet.getResultVars());
        model = originalSet.getResourceModel();
        while (originalSet.hasNext()) {
            QuerySolutionMap copy = new QuerySolutionMap();
            copy.addAll(originalSet.next());
            querySolutionList.add(copy);
        }
    }

    @Override
    public boolean hasNext() {
        return querySolutionList.iterator().hasNext();
    }

    @Override
    public QuerySolution next() {
        return querySolutionList.iterator().next();
    }

    @Override
    public QuerySolution nextSolution() {
        return querySolutionList.iterator().next();
    }

    @Override
    public Binding nextBinding() {
        throw new NotImplementedException();
    }

    @Override
    public int getRowNumber() {
        return querySolutionList.size();
    }

    @Override
    public List<String> getResultVars() {
        return Collections.unmodifiableList(resultVars);
    }

    @Override
    public Model getResourceModel() {
        return model;
    }

    @Override
    public void remove() {
        // TODO Auto-generated method stub

    }
}
