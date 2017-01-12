package org.sifrproject.util.profiling;

import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.AnnotationFactory;
import org.sifrproject.annotations.api.model.retrieval.PropertyRetriever;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;
import org.sifrproject.annotations.input.BioPortalJSONAnnotationParser;
import org.sifrproject.annotations.model.BioPortalLazyAnnotationFactory;
import org.sifrproject.annotations.model.retrieval.CUIPropertyRetriever;
import org.sifrproject.annotations.model.retrieval.SemanticTypePropertyRetriever;
import org.sifrproject.annotations.umls.UMLSGroupIndex;
import org.sifrproject.annotations.umls.UMLSSemanticGroupsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparqy.api.graph.store.Store;
import org.sparqy.graph.storage.JenaRemoteSPARQLStore;
import org.sparqy.graph.storage.StoreHandler;

import java.io.IOException;

/**
 * This is a test class aimed a profiling the parsing of the bioportal output, use with VisualVM
 * Press enter in the console as soon as you start profiling in order to launch the parsing
 */
public class ParserProfiler {

    private static Logger logger = LoggerFactory.getLogger(ParserProfiler.class);

    private static final String jsonOutput = "[\n" +
            "  {\n" +
            "    \"annotatedClass\": {\n" +
            "      \"@id\": \"http://purl.lirmm.fr/ontology/MuEVo/vpm61\",\n" +
            "      \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "      \"links\": {\n" +
            "        \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61\",\n" +
            "        \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO\",\n" +
            "        \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/children\",\n" +
            "        \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/parents\",\n" +
            "        \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/descendants\",\n" +
            "        \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/ancestors\",\n" +
            "        \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/instances\",\n" +
            "        \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/tree\",\n" +
            "        \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/notes\",\n" +
            "        \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MUEVO/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61/mappings\",\n" +
            "        \"ui\": \"http://bioportal.lirmm.fr/ontologies/MUEVO?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMuEVo%2Fvpm61\",\n" +
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
            "    \"hierarchy\": [],\n" +
            "    \"annotations\": [\n" +
            "      {\n" +
            "        \"from\": 13,\n" +
            "        \"to\": 18,\n" +
            "        \"matchType\": \"PREF\",\n" +
            "        \"text\": \"CANCER\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"mappings\": [\n" +
            "      {\n" +
            "        \"annotatedClass\": {\n" +
            "          \"@id\": \"http://purl.lirmm.fr/ontology/MDRFRE/10007050\",\n" +
            "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"links\": {\n" +
            "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050\",\n" +
            "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE\",\n" +
            "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/children\",\n" +
            "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/parents\",\n" +
            "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/descendants\",\n" +
            "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/ancestors\",\n" +
            "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/instances\",\n" +
            "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/tree\",\n" +
            "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/notes\",\n" +
            "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/mappings\",\n" +
            "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/MDRFRE?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050\",\n" +
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
            "        \"score\": \"4.584962500721157\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"annotatedClass\": {\n" +
            "          \"@id\": \"http://chu-rouen.fr/cismef/SNOMED_int.#M-80003\",\n" +
            "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"links\": {\n" +
            "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003\",\n" +
            "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE\",\n" +
            "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/children\",\n" +
            "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/parents\",\n" +
            "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/descendants\",\n" +
            "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/ancestors\",\n" +
            "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/instances\",\n" +
            "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/tree\",\n" +
            "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/notes\",\n" +
            "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/mappings\",\n" +
            "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/SNMIFRE?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003\",\n" +
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
            "        \"score\": \"3.9068905956085187\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"annotatedClass\": {\n" +
            "          \"@id\": \"http://purl.lirmm.fr/ontology/MDRFRE/10007050\",\n" +
            "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"links\": {\n" +
            "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050\",\n" +
            "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE\",\n" +
            "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/children\",\n" +
            "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/parents\",\n" +
            "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/descendants\",\n" +
            "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/ancestors\",\n" +
            "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/instances\",\n" +
            "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/tree\",\n" +
            "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/notes\",\n" +
            "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/mappings\",\n" +
            "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/MDRFRE?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050\",\n" +
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
            "        \"score\": \"4.584962500721157\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"annotatedClass\": {\n" +
            "          \"@id\": \"http://chu-rouen.fr/cismef/SNOMED_int.#M-80003\",\n" +
            "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"links\": {\n" +
            "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003\",\n" +
            "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE\",\n" +
            "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/children\",\n" +
            "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/parents\",\n" +
            "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/descendants\",\n" +
            "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/ancestors\",\n" +
            "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/instances\",\n" +
            "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/tree\",\n" +
            "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/notes\",\n" +
            "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/mappings\",\n" +
            "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/SNMIFRE?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003\",\n" +
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
            "        \"score\": \"3.9068905956085187\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"annotatedClass\": {\n" +
            "          \"@id\": \"http://purl.lirmm.fr/ontology/MSHFRE/D009369\",\n" +
            "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"links\": {\n" +
            "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369\",\n" +
            "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE\",\n" +
            "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/children\",\n" +
            "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/parents\",\n" +
            "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/descendants\",\n" +
            "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/ancestors\",\n" +
            "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/instances\",\n" +
            "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/tree\",\n" +
            "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/notes\",\n" +
            "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/mappings\",\n" +
            "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/MSHFRE?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369\",\n" +
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
            "        \"score\": \"4.459431618637297\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"annotatedClass\": {\n" +
            "          \"@id\": \"http://purl.lirmm.fr/ontology/MSHFRE/D009369\",\n" +
            "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"links\": {\n" +
            "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369\",\n" +
            "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE\",\n" +
            "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/children\",\n" +
            "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/parents\",\n" +
            "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/descendants\",\n" +
            "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/ancestors\",\n" +
            "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/instances\",\n" +
            "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/tree\",\n" +
            "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/notes\",\n" +
            "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/mappings\",\n" +
            "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/MSHFRE?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369\",\n" +
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
            "        \"score\": \"4.459431618637297\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"score\": \"4.954196310386876\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"annotatedClass\": {\n" +
            "      \"@id\": \"http://purl.lirmm.fr/ontology/MDRFRE/10007050\",\n" +
            "      \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "      \"links\": {\n" +
            "        \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050\",\n" +
            "        \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE\",\n" +
            "        \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/children\",\n" +
            "        \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/parents\",\n" +
            "        \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/descendants\",\n" +
            "        \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/ancestors\",\n" +
            "        \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/instances\",\n" +
            "        \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/tree\",\n" +
            "        \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/notes\",\n" +
            "        \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050/mappings\",\n" +
            "        \"ui\": \"http://bioportal.lirmm.fr/ontologies/MDRFRE?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMDRFRE%2F10007050\",\n" +
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
            "    \"hierarchy\": [],\n" +
            "    \"annotations\": [\n" +
            "      {\n" +
            "        \"from\": 13,\n" +
            "        \"to\": 18,\n" +
            "        \"matchType\": \"PREF\",\n" +
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
            "        \"score\": \"4.954196310386876\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"annotatedClass\": {\n" +
            "          \"@id\": \"http://purl.lirmm.fr/ontology/MSHFRE/D009369\",\n" +
            "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"links\": {\n" +
            "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369\",\n" +
            "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE\",\n" +
            "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/children\",\n" +
            "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/parents\",\n" +
            "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/descendants\",\n" +
            "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/ancestors\",\n" +
            "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/instances\",\n" +
            "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/tree\",\n" +
            "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/notes\",\n" +
            "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/mappings\",\n" +
            "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/MSHFRE?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369\",\n" +
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
            "        \"score\": \"4.459431618637297\"\n" +
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
            "        \"score\": \"4.954196310386876\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"annotatedClass\": {\n" +
            "          \"@id\": \"http://purl.lirmm.fr/ontology/MSHFRE/D009369\",\n" +
            "          \"@type\": \"http://www.w3.org/2002/07/owl#Class\",\n" +
            "          \"links\": {\n" +
            "            \"self\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369\",\n" +
            "            \"ontology\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE\",\n" +
            "            \"children\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/children\",\n" +
            "            \"parents\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/parents\",\n" +
            "            \"descendants\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/descendants\",\n" +
            "            \"ancestors\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/ancestors\",\n" +
            "            \"instances\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/instances\",\n" +
            "            \"tree\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/tree\",\n" +
            "            \"notes\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/notes\",\n" +
            "            \"mappings\": \"http://data.bioportal.lirmm.fr/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369/mappings\",\n" +
            "            \"ui\": \"http://bioportal.lirmm.fr/ontologies/MSHFRE?p=classes&conceptid=http%3A%2F%2Fpurl.lirmm.fr%2Fontology%2FMSHFRE%2FD009369\",\n" +
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
            "        \"score\": \"4.459431618637297\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"score\": \"4.584962500721157\"\n" +
            "  }]";

    public static void main(String... args) throws IOException, ParseException, NCBOAnnotatorErrorException {
        VisualVMTools.delayUntilReturn();
        Store store = new JenaRemoteSPARQLStore("http://sparql.bioportal.lirmm.fr/sparql/");
        StoreHandler.registerStoreInstance(store);

        PropertyRetriever cuiRetrieval = new CUIPropertyRetriever();
        PropertyRetriever typeRetrieval = new SemanticTypePropertyRetriever();
        UMLSGroupIndex umlsGroupIndex = UMLSSemanticGroupsLoader.load();
        AnnotationFactory annotationFactory = new BioPortalLazyAnnotationFactory();

        AnnotationParser parser = new BioPortalJSONAnnotationParser(annotationFactory, cuiRetrieval, typeRetrieval, umlsGroupIndex);
        Long time = System.currentTimeMillis();
        parser.parseAnnotations(jsonOutput);
        System.err.println(System.currentTimeMillis() - time);


    }
}
