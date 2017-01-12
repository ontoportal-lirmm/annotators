/**
 *
 */
package org.sparqy.graph;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparqy.api.graph.OntologyModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * An OWL graph api model wrapper around Jena
 */
public class OWLTBoxModel implements OntologyModel {
    private OntModel model;
    private String propPath = String.format("data%sgraphapi.properties", File.separatorChar);
    private Properties properties;

    private static final Logger logger = LoggerFactory.getLogger(OWLTBoxModel.class);

    /**
     * Default constructor
     *
     * @throws IOException When loading the default properties fails (data/graphapi.properties)
     */
    @SuppressWarnings("unused")
    public OWLTBoxModel() throws IOException{
        try {
            loadProperties();
        } catch (IOException e){
            logger.warn("Cannot load default ontology properties");
        }
        createModel(null);
    }

    /**
     * Build an {@code OWLTBoxModel} based on an existing model <code>m</code>
     *
     * @param model An existing base graphapi model
     * @throws IOException When loading the default properties fails (data/graphapi.properties)
     */
    @SuppressWarnings("unused")
    public OWLTBoxModel(Model model) throws IOException {
        loadProperties();
        createModel(model);
    }

    /**
     * Load an OWLTBoxModel with a custom properties path
     *
     * @param propPath The path to the properties file
     * @throws IOException When the properties file at {@code propPath} cannot be loaded
     */
    public OWLTBoxModel(String propPath) throws IOException {
        this.propPath = propPath;
        loadProperties();
        createModel(null);
    }

    private void createModel(Model model) {
        if (model == null) {
            this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        } else {
            this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, model);
        }
        if (properties.containsKey("ontologies")) {
            String[] ontologies = properties.getProperty("ontologies").split(",");
            for (String ont : ontologies) {
                this.model.read(ont.trim());
            }
        }
    }

    /**
     * Load the properties from the properties path
     *
     * @throws IOException When the properties cannot be loaded
     */
    protected void loadProperties() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(propPath));
    }


    @Override
    public Node getNode(String uri) {
        return NodeFactory.createURI(model.expandPrefix(uri));
    }
}
