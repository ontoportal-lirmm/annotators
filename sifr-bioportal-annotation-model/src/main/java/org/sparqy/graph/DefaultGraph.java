/**
 *
 */
package org.sparqy.graph;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import org.sparqy.api.Graph;
import org.sparqy.api.graph.OntologyModel;


/**
 * The default Graph interface implementation
 */
public class DefaultGraph implements Graph {
    private String nodeUri;
    private final OntologyModel model;

    public DefaultGraph(String nodeUri, OntologyModel model) {
        this.nodeUri = nodeUri;
        this.model = model;
    }

    @Override
    public OntologyModel getModel() {
        return model;
    }

    @Override
    public Node getJenaNode() {
        return NodeFactory.createURI(nodeUri);
    }
}
