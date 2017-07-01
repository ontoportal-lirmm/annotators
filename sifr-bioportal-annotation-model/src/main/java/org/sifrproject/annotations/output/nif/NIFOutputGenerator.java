package org.sifrproject.annotations.output.nif;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.icu.text.SimpleDateFormat;
import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;

import java.io.StringWriter;
import java.util.Date;
import java.util.Random;


/**
 * RDF output generator using Jena, supports all the formats supported by Jena.
 */
@SuppressWarnings({"LawOfDemeter", "HardcodedFileSeparator"})
public class NIFOutputGenerator implements OutputGenerator {
    private final String jenaPutputFormat;


    private static final String SIFR_ANNOTATOR = "SIFR Annotator";
    private static final String THE_SIFR_ANNOTATOR_IS_A_SPECIFIC_VERSION_OF_THE_NCBO_ANNOTATOR_BUT_FOR_FRENCH_ONTOLOGIES_TERMINOLOGIES_YOU_SHALL_USE_IT_TO_ANNOTATE_FRENCH_BIOMEDICAL_TEXT_WITH_ONTOLOGY_CONCEPTS = "The SIFR Annotator is a specific version of the NCBO Annotator but for French ontologies & terminologies. You shall use it to annotate French biomedical text with ontology concepts.";
    private static final String ANNOTATION = "Annotation";

    // Prefix
    private static final String NIF_PREFIX = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#";
    private static final String ITSRDF_PREFIX= "http://www.w3.org/2005/11/its/rdf#";
    private static final String FOAF_PREFIX = "http://xmlns.com/foaf/0.1/";
    private static final String PROV_PREFIX = "http://www.w3.org/ns/prov#";
    private static final String DBPEDIA_PREFIX = "http://dbpedia.org/resource/";

    // URLs


    /**
     * Output generator based on Apache Jena, can generate the output from the annotation modelin any of the formats
     * supported by Jena
     *
     * @param jenaOutputFormat The rdf output format,
     *                         {@see https://rdf.apache.org/documentation/io/rdf-output.html#jena_model_write_formats}
     *                         for a list of all supported formats
     */
    public NIFOutputGenerator(final String jenaOutputFormat) {
        this.jenaPutputFormat = jenaOutputFormat;
    }

    @Override
    public AnnotatorOutput generate(final Iterable<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final OntModel jenaModel = ModelFactory.createOntologyModel();
        initializeModel(jenaModel, annotatorURI);

        final Integer count = 0;
        final int uid = (new Random()).nextInt(10000);

        for (final Annotation annotation : annotations) {
            final String id = annotation.getAnnotatedClass().getId();
            final AnnotationTokens tokens = annotation.getAnnotations();
            appendAnnotationsToModel(jenaModel, tokens, id, "ExactQualifier", count, uid);

            for (final Mapping mapping : annotation.getMappings()) {
                appendAnnotationsToModel(jenaModel, tokens, mapping.getAnnotatedClass().getId(),
                        "CloseQualifier", count, uid);
            }
            for (final HierarchyElement hierarchyElement : annotation.getHierarchy()) {
                appendAnnotationsToModel(jenaModel, tokens, hierarchyElement.getAnnotatedClass().getId(),
                        "BroadQualifier", count, uid);
            }

        }

        final StringWriter rdfOutput = new StringWriter();
        jenaModel.write(rdfOutput, jenaPutputFormat.toUpperCase());
        return new LIRMMAnnotatorOutput(rdfOutput.toString(), String.format("text/%s", jenaPutputFormat));
    }

    private void initializeModel(final OntModel model, final String annotatorURI) {
        //Describing the servlet used
        model.setNsPrefix("nif", NIF_PREFIX);
        model.setNsPrefix("itsrdf", ITSRDF_PREFIX);
        model.setNsPrefix("prov", PROV_PREFIX);
        model.setNsPrefix("dbpedia", DBPEDIA_PREFIX);
        model.setNsPrefix("foaf", FOAF_PREFIX);

        final Resource annotationContext = model.createResource("#char=0,");
        final Resource nifContext = model.createResource("nif:Context");
        final Resource nifString = model.createResource("nif:RFC5147String");
        final Resource annotatorResource = model.createResource(annotatorURI);
        final Resource annotatorType1 = model.createResource("prov:SoftwareAgent");
        final Resource annotatorType2 = model.createResource("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Data_Computation_Service");
        final Resource annotatorType3 = model.createResource("dbpedia:Software");
        final Property foafNameProp = model.createProperty("foaf:name");
        final Property foafDescriptionProp = model.createProperty("description");


        final String annotatorFoafName;
        final String annotatorFoafDescription;

        switch (annotatorURI) {
            case "http://vm-bioportal-vincent:8080/servlet?":
                annotatorFoafName = SIFR_ANNOTATOR;
                annotatorFoafDescription = THE_SIFR_ANNOTATOR_IS_A_SPECIFIC_VERSION_OF_THE_NCBO_ANNOTATOR_BUT_FOR_FRENCH_ONTOLOGIES_TERMINOLOGIES_YOU_SHALL_USE_IT_TO_ANNOTATE_FRENCH_BIOMEDICAL_TEXT_WITH_ONTOLOGY_CONCEPTS;
                break;
            case "http://bioportal.lirmm.fr:8080/servlet?":
                annotatorFoafName = SIFR_ANNOTATOR;
                annotatorFoafDescription = THE_SIFR_ANNOTATOR_IS_A_SPECIFIC_VERSION_OF_THE_NCBO_ANNOTATOR_BUT_FOR_FRENCH_ONTOLOGIES_TERMINOLOGIES_YOU_SHALL_USE_IT_TO_ANNOTATE_FRENCH_BIOMEDICAL_TEXT_WITH_ONTOLOGY_CONCEPTS;
                break;
            case "http://agroportal.lirmm.fr:8080/servlet?":
                annotatorFoafName = "IBC Annotator";
                annotatorFoafDescription = "The IBC Annotator is a specific version of the NCBO Annotator but for plant related ontologies. You shall use it to annotate plant related text data with ontology concepts.";
                break;
            default:
                annotatorFoafName = "NCBO Annotator";
                annotatorFoafDescription = "The NCBO BioPortal Annotator processes text submitted by users, recognizes relevant ontology terms in the text and returns the annotations to the user.";
                break;
        }

        model.add(annotationContext, RDF.type, nifContext);
        model.add(annotationContext, RDF.type, nifString);

        model.add(annotatorResource, RDF.type, annotatorType1)
                .add(annotatorResource, RDF.type, annotatorType2)
                .add(annotatorResource, RDF.type, annotatorType3)
                .add(annotatorResource, foafNameProp, annotatorFoafName)
                .add(annotatorResource, foafDescriptionProp, annotatorFoafDescription);



    }

    @SuppressWarnings("HardcodedFileSeparator")
    private void appendAnnotationsToModel(final OntModel model, final Iterable<AnnotationToken> annotationTokens, final String topicURL, final String qualifierSubclass, Integer count, final int uid) {


        for (final AnnotationToken annotationToken : annotationTokens) {
            final String text = annotationToken.getText();
            final int from = annotationToken.getFrom();
            final int to = annotationToken.getTo();
            final int taill = (to - from) + 1;



            // Document provenance
            final Property annotatesDocument = model.createProperty(aofPrefix + "annotatesDocument");
            final Resource annotatesDocumentResource = model.createResource(onDocumentURL + uid);

            // Annotation topic
            final Property context = model.createProperty(aoPrefix + "context");
            final Property hasTopic = model.createProperty(aoPrefix + "hasTopic");
            final Resource hasTopicResource = model.createResource(topicURL);

            // Annotation provenance
            final Property createdBy = model.createProperty(pavPrefix + "createdBy");
            final Property createdOn = model.createProperty(pavPrefix + "createdOn");
            final Resource createdByResource = model.createResource(createdByURL);

            final Date today = new Date();
            final SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd");


            // Annotation Selector
            final Property exact = model.createProperty(aosPrefix + "exact");
            final Property offset = model.createProperty(aosPrefix + "offset");
            final Property range = model.createProperty(aosPrefix + "range");

            // Document provenance
            final Property onDocument = model.createProperty(aofPrefix + "onDocument");
            final Resource onDocumentResource = model.createResource(onDocumentURL + uid);

            final Resource r1 = model.createResource(aot + qualifierSubclass);
            final Resource r2 = model.createResource(aot + "Qualifier");
            final Resource r3 = model.createResource(aoPrefix + ANNOTATION);
            final Resource r4 = model.createResource(annPrefix + ANNOTATION);
            final Resource r5 = model.createResource(aoPrefix + "Selector");
            final Resource r6 = model.createResource(aosPrefix + "TextSelector");
            final Resource r7 = model.createResource(aosPrefix + "OffsetRangeSelector");

            final String selectorURI = getSelectorURI(from, taill, model);
            final Resource root2;

            if (selectorURI.equals("")) {
                root2 = model.createResource(root2URL + uid + "/" + count);
                model.add(root2, onDocument, onDocumentResource)
                        .add(root2, range, model.createTypedLiteral(Integer.toString(taill), XSDDatatype.XSDinteger))
                        .add(root2, exact, text)
                        .add(root2, offset, model.createTypedLiteral(Integer.toString(from), XSDDatatype.XSDinteger))
                        .add(root2, RDF.type, r7).add(root2, RDF.type, r6)
                        .add(root2, RDF.type, r5);
            } else {
                root2 = model.createResource(selectorURI);
            }

            model.add(root, createdBy, createdByResource)
                    .add(root, createdOn, model.createTypedLiteral(formater.format(today), XSDDatatype.XSDdate))
                    .add(root, hasTopic, hasTopicResource)
                    .add(root, context, root2)
                    .add(root, annotatesDocument, annotatesDocumentResource)
                    .add(root, RDF.type, r4).add(root, RDF.type, r3)
                    .add(root, RDF.type, r2).add(root, RDF.type, r1);

            count++;
        }
    }

}
