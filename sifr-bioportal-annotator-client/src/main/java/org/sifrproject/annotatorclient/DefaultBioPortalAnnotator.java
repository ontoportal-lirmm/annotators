package org.sifrproject.annotatorclient;


import org.sifrproject.annotatorclient.api.BioPortalAnnotator;
import org.sifrproject.annotatorclient.api.BioPortalAnnotatorQuery;
import org.sifrproject.annotatorclient.util.RequestGenerator;
import org.sifrproject.annotatorclient.util.RestfulRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultBioPortalAnnotator implements BioPortalAnnotator {

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


        /*
         * Request parameters
         */

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Authorization", String.format("apikey token=%s", apiKey));
        requestProperties.put("Accept", "application/rdf+xml");

        RequestGenerator requestGenerator = new RequestGenerator(baseURI,"POST", requestProperties);

        requestGenerator.put("text", query.getText());

        requestGenerator.put("apikey", apiKey);

        String ontologyString = query.generateOntologyString();
        if (!ontologyString.isEmpty()) {
            requestGenerator.put("ontologies", ontologyString);
        }

        String semanticTypesString = query.generateSemanticTypesString();
        if (!semanticTypesString.isEmpty()) {
            requestGenerator.put("semantic_types", semanticTypesString);
        }

        String semanticGroupsString = query.generateSemanticGroupsString();
        if (!semanticGroupsString.isEmpty()) {
            requestGenerator.put("semantic_groups", semanticGroupsString);
        }

        if (query.isExcludeNumbers()) {
            requestGenerator.put("exclude_numbers", "true");
        }
        if (query.isWholeWordOnly()) {
            requestGenerator.put("whole_word_only", "true");
        }
        if (query.isExcludeSynonyms()) {
            requestGenerator.put("exclude_synonyms", "true");
        }
        if (query.isExpandMappings()) {
            requestGenerator.put("expand_mappings", "true");
        }
        if (query.isExpandClassHierarchy()) {
            requestGenerator.put("expand_class_hierarchy", "true");
        }
        if(query.isLongestOnly()){
            requestGenerator.put("longest_only", "true");
        }

        if (!query.getScore().isEmpty()) {
            requestGenerator.put("score", query.getScore());
        }
        if (query.getClassHierarchyMaxLevel() > 0) {
            requestGenerator.put("class_hierarchy_max_level", Integer.toString(query.getClassHierarchyMaxLevel()));
        }
        if(query.isNegation()){
            requestGenerator.put("negation", Boolean.toString(query.isNegation()));
        }
        if(query.isTemporality()){
            requestGenerator.put("temporality", Boolean.toString(query.isTemporality()));
        }

        if(query.isExperiencer()){
            requestGenerator.put("experiencer", Boolean.toString(query.isExperiencer()));
        }

        if(query.isLemmatize()){
            requestGenerator.put("lemmatize", Boolean.toString(query.isLemmatize()));
        }
        requestGenerator.put("format", query.getFormat());

        //logger.error(connection.toString());

        return RestfulRequest.queryAnnotator(requestGenerator);
    }
}
