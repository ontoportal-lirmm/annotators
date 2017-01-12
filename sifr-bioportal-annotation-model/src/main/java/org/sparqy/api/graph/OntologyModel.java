package org.sparqy.api.graph;

import java.io.Serializable;

/**
 * Interface to the Jena OntModel Wrapper
 */
public interface OntologyModel extends Serializable{


    /**
     * The graph node that corresponds to the {@code uri}.
     * This method expands prefixes that are loaded into the model
     *
     * @param uri The uri of the node.
     */
    com.hp.hpl.jena.graph.Node getNode(String uri);

}
