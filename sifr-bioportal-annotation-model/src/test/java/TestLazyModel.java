import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationFactory;
import org.sifrproject.annotations.exceptions.InvalidFormatException;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;
import org.sifrproject.annotations.input.BioPortalJSONAnnotationParser;
import org.sifrproject.annotations.model.BioPortalLazyAnnotationFactory;
import org.sparqy.api.graph.store.Store;
import org.sparqy.graph.storage.JenaRemoteSPARQLStore;
import org.sparqy.graph.storage.StoreHandler;

import java.io.IOException;
import java.util.List;

public class TestLazyModel {
    @org.junit.Before
    public void setUp() throws Exception {
        final Store store = new JenaRemoteSPARQLStore("http://sparql.bioportal.lirmm.fr/sparql/");
        StoreHandler.registerStoreInstance(store);
    }

    @Test
    public void testModelConstruction() throws IOException {
//        PropertyRetriever cuiRetrieval = new CUIPropertyRetriever();
//        PropertyRetriever typeRetrieval = new SemanticTypePropertyRetriever();
//        UMLSGroupIndex umlsGroupIndex = UMLSSemanticGroupsLoader.load();
        AnnotationFactory annotationFactory = new BioPortalLazyAnnotationFactory();

        AnnotationParser parser = new BioPortalJSONAnnotationParser(annotationFactory);
        List<Annotation> annotationList = null;
        try {
            final String jsonOutput = "[{\n" +
                    "    \"annotatedClass\": {\n" +
                    "      \"@id\": \"http://chu-rouen.fr/cismef/SNOMED_int.#M-80003\",\n" +
                    "      \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "      \"links\": {\n" +
                    "        \"self\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003\",\n" +
                    "        \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE\",\n" +
                    "        \"children\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/children\",\n" +
                    "        \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/parents\",\n" +
                    "        \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/descendants\",\n" +
                    "        \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/ancestors\",\n" +
                    "        \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/instances\",\n" +
                    "        \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/tree\",\n" +
                    "        \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/notes\",\n" +
                    "        \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/mappings\",\n" +
                    "        \"ui\": \"http://bioportal.lirmm.fr/ontologies/SNMIFRE?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003\",\n" +
                    "        \"@context\": {\n" +
                    "          \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
                    "          \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"instances\": \"http://data.bioontology.org/metadata/Instance\",\n" +
                    "          \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
                    "          \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
                    "          \"ui\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"@context\": {\n" +
                    "        \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    \"hierarchy\": [\n" +
                    "      {\n" +
                    "        \"annotatedClass\": {\n" +
                    "          \"@id\": \"http://chu-rouen.fr/cismef/SNOMED_int.#M-80000_S3\",\n" +
                    "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"links\": {\n" +
                    "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3\",\n" +
                    "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE\",\n" +
                    "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3/children\",\n" +
                    "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3/parents\",\n" +
                    "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3/descendants\",\n" +
                    "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3/ancestors\",\n" +
                    "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3/instances\",\n" +
                    "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3/tree\",\n" +
                    "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3/notes\",\n" +
                    "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3/mappings\",\n" +
                    "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/SNMIFRE?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_S3\",\n" +
                    "            \"@context\": {\n" +
                    "              \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
                    "              \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"instances\": \"http://data.bioontology.org/metadata/Instance\",\n" +
                    "              \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
                    "              \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
                    "              \"ui\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          \"@context\": {\n" +
                    "            \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"distance\": 1,\n" +
                    "        \"score\": 3.19964\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"annotatedClass\": {\n" +
                    "          \"@id\": \"http://chu-rouen.fr/cismef/SNOMED_int.#M-80000_GS\",\n" +
                    "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"links\": {\n" +
                    "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS\",\n" +
                    "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE\",\n" +
                    "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS/children\",\n" +
                    "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS/parents\",\n" +
                    "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS/descendants\",\n" +
                    "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS/ancestors\",\n" +
                    "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS/instances\",\n" +
                    "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS/tree\",\n" +
                    "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS/notes\",\n" +
                    "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS/mappings\",\n" +
                    "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/SNMIFRE?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80000_GS\",\n" +
                    "            \"@context\": {\n" +
                    "              \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
                    "              \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"instances\": \"http://data.bioontology.org/metadata/Instance\",\n" +
                    "              \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
                    "              \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
                    "              \"ui\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          \"@context\": {\n" +
                    "            \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"distance\": 2,\n" +
                    "        \"score\": 2.94545\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"annotatedClass\": {\n" +
                    "          \"@id\": \"http://chu-rouen.fr/cismef/SNOMED_int.#M\",\n" +
                    "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"links\": {\n" +
                    "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M\",\n" +
                    "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE\",\n" +
                    "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M/children\",\n" +
                    "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M/parents\",\n" +
                    "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M/descendants\",\n" +
                    "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M/ancestors\",\n" +
                    "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M/instances\",\n" +
                    "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M/tree\",\n" +
                    "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M/notes\",\n" +
                    "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M/mappings\",\n" +
                    "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/SNMIFRE?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M\",\n" +
                    "            \"@context\": {\n" +
                    "              \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
                    "              \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"instances\": \"http://data.bioontology.org/metadata/Instance\",\n" +
                    "              \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
                    "              \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
                    "              \"ui\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          \"@context\": {\n" +
                    "            \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"distance\": 3,\n" +
                    "        \"score\": 2.69779\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"annotatedClass\": {\n" +
                    "          \"@id\": \"http://chu-rouen.fr/cismef/SNOMED_int.#ARBO\",\n" +
                    "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"links\": {\n" +
                    "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO\",\n" +
                    "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE\",\n" +
                    "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO/children\",\n" +
                    "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO/parents\",\n" +
                    "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO/descendants\",\n" +
                    "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO/ancestors\",\n" +
                    "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO/instances\",\n" +
                    "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO/tree\",\n" +
                    "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO/notes\",\n" +
                    "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO/mappings\",\n" +
                    "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/SNMIFRE?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23ARBO\",\n" +
                    "            \"@context\": {\n" +
                    "              \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
                    "              \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"instances\": \"http://data.bioontology.org/metadata/Instance\",\n" +
                    "              \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
                    "              \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
                    "              \"ui\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          \"@context\": {\n" +
                    "            \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"distance\": 4,\n" +
                    "        \"score\": 3.34654\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"annotations\": [\n" +
                    "      {\n" +
                    "        \"from\": 13,\n" +
                    "        \"to\": 18,\n" +
                    "        \"matchType\": \"SYN\",\n" +
                    "        \"text\": \"CANCER\"\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"mappings\": [\n" +
                    "      {\n" +
                    "        \"annotatedClass\": {\n" +
                    "          \"@id\": \"http://purl.lirmm.fr/ontology/MuEVo/vpm61\",\n" +
                    "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"links\": {\n" +
                    "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61\",\n" +
                    "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO\",\n" +
                    "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/children\",\n" +
                    "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/parents\",\n" +
                    "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/descendants\",\n" +
                    "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/ancestors\",\n" +
                    "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/instances\",\n" +
                    "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/tree\",\n" +
                    "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/notes\",\n" +
                    "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/mappings\",\n" +
                    "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/MUEVO?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61\",\n" +
                    "            \"@context\": {\n" +
                    "              \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
                    "              \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"instances\": \"http://data.bioontology.org/metadata/Instance\",\n" +
                    "              \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
                    "              \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
                    "              \"ui\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          \"@context\": {\n" +
                    "            \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"score\": 4.95419\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"annotatedClass\": {\n" +
                    "          \"@id\": \"http://purl.lirmm.fr/ontology/MuEVo/vpm61\",\n" +
                    "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "          \"links\": {\n" +
                    "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61\",\n" +
                    "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO\",\n" +
                    "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/children\",\n" +
                    "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/parents\",\n" +
                    "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/descendants\",\n" +
                    "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/ancestors\",\n" +
                    "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/instances\",\n" +
                    "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/tree\",\n" +
                    "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/notes\",\n" +
                    "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/mappings\",\n" +
                    "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/MUEVO?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61\",\n" +
                    "            \"@context\": {\n" +
                    "              \"self\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ontology\": \"http://data.bioontology.org/metadata/Ontology\",\n" +
                    "              \"children\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"parents\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"descendants\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"instances\": \"http://data.bioontology.org/metadata/Instance\",\n" +
                    "              \"tree\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
                    "              \"notes\": \"http://data.bioontology.org/metadata/Note\",\n" +
                    "              \"mappings\": \"http://data.bioontology.org/metadata/Mapping\",\n" +
                    "              \"ui\": \"http://www.w3.org/2002/07/owl#Class\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          \"@context\": {\n" +
                    "            \"@vocab\": \"http://data.bioontology.org/metadata/\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"score\": 4.954196\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"score\": 3.90689\n" +
                    "  }," +
                    "{}," +
                    "{\"annotatedClass\":{\"@id\":\"\", \"@type\":\"\", \"@context\":{\"@vocab\":\"\"},\"links\":{}},\"hierarchy\": [{}]}]";
            annotationList = parser.parseAnnotations(jsonOutput);
            assert !annotationList.isEmpty();
        } catch (ParseException | NCBOAnnotatorErrorException| InvalidFormatException e) {
            assert true;
        }

    }
}
