package org.sifrproject.annotatorclient;


import org.sifrproject.annotatorclient.api.BioPortalAnnotator;
import org.sifrproject.annotatorclient.api.BioPortalAnnotatorQuery;
import org.sifrproject.annotatorclient.util.Pair;
import org.sifrproject.annotatorclient.util.PairImpl;
import org.sifrproject.annotatorclient.util.RestfulQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DefaultBioPortalAnnotator implements BioPortalAnnotator {

    private static Logger logger = LoggerFactory.getLogger(BioPortalAnnotator.class);

    private final String baseURI;
    private final String apiKey;

    DefaultBioPortalAnnotator(String baseURI, String apiKey) {
        this.baseURI = baseURI;
        this.apiKey = apiKey;
    }

    /**
     * Sends a restful query to the Bioportal Annotator as defined by the {@link BioPortalAnnotatorQuery},
     * following the below specification.
     *
     * @param query The {@link BioPortalAnnotatorQuery} object that defines the query
     *              <p>
     *              text=This is some imput text
     *              ontologies=Ontology1,Ontology2,...
     *              longest_only=[true|false]
     *              exclude_numbers=[true|false]
     *              whole_word_only=[true|false]
     *              exclude_synonyms=[true|false]
     *              expand_mappings=[true|false]
     *              score=[old|cvalue|cvalueh]
     *              expand_class_hierarchy=[true|false]
     *              class_hierarchy_max_level=[1..10|999] (999 for All)
     *              semantic_types=Type1, Type2,...
     **/
    @Override
    public String runQuery(BioPortalAnnotatorQuery query) throws IOException {
        List<Pair<String, String>> parameters = new ArrayList<>();

        parameters.add(new PairImpl<>("text", query.getText()));

        parameters.add(new PairImpl<>("apikey", apiKey));

        String ontologyString = query.generateOntologyString();
        if (!ontologyString.isEmpty()) {
            parameters.add(new PairImpl<>("ontologies", ontologyString));
        }

        String semanticTypesString = query.generateSemanticTypesString();
        if (!semanticTypesString.isEmpty()) {
            parameters.add(new PairImpl<>("semantic_types", semanticTypesString));
        }

        if (query.isExcludeNumbers()) {
            parameters.add(new PairImpl<>("exclude_numbers", "true"));
        }
        if (query.isWholeWordOnly()) {
            parameters.add(new PairImpl<>("whole_word_only", "true"));
        }
        if (query.isExcludeSynonyms()) {
            parameters.add(new PairImpl<>("exclude_synonyms", "true"));
        }
        if (query.isExpandMappings()) {
            parameters.add(new PairImpl<>("expand_mappings", "true"));
        }
        if (query.isExpandClassHierarchy()) {
            parameters.add(new PairImpl<>("expand_class_hierarchy", "true"));
        }

        if (!query.getScore().isEmpty()) {
            parameters.add(new PairImpl<>("score", query.getScore()));
        }
        if (query.getClassHierarchyMaxLevel() > 0) {
            parameters.add(new PairImpl<>("class_hierarchy_max_level", Integer.toString(query.getClassHierarchyMaxLevel())));
        }
        if(query.isNegation()){
            parameters.add(new PairImpl<String, String>("negation",Boolean.toString(query.isNegation())));
        }
        if(query.isTemporality()){
            parameters.add(new PairImpl<String, String>("temporality",Boolean.toString(query.isTemporality())));
        }

        if(query.isExperiencer()){
            parameters.add(new PairImpl<String, String>("experiencer",Boolean.toString(query.isExperiencer())));
        }
        parameters.add(new PairImpl<>("format", query.getFormat()));


        /*
         * Request parameters
         */

        List<Pair<String, String>> requestProperties = new ArrayList<>();
        requestProperties.add(new PairImpl<>("Authorization", String.format("apikey token=%s", apiKey)));
        requestProperties.add(new PairImpl<>("Accept", "application/rdf+xml"));

        URLConnection connection = RestfulQuery.restfulQuery(baseURI, parameters, requestProperties, "ISO-8859-15");

        //logger.error(connection.toString());

        return RestfulQuery.getRequestOutput(connection);
    }
}
