package org.sifrproject.annotations.output.jena;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
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
import java.util.List;
import java.util.Random;


/**
 * RDF output generator using Jena, supports all the formats supported by Jena.
 */
@SuppressWarnings({"LawOfDemeter", "HardcodedFileSeparator"})
public class JenaOutputGenerator implements OutputGenerator {
    private static final String SIFR_ANNOTATOR = "SIFR Annotator";
    private static final String THE_SIFR_ANNOTATOR_IS_A_SPECIFIC_VERSION_OF_THE_NCBO_ANNOTATOR_BUT_FOR_FRENCH_ONTOLOGIES_TERMINOLOGIES_YOU_SHALL_USE_IT_TO_ANNOTATE_FRENCH_BIOMEDICAL_TEXT_WITH_ONTOLOGY_CONCEPTS = "The SIFR Annotator is a specific version of the NCBO Annotator but for French ontologies & terminologies. You shall use it to annotate French biomedical text with ontology concepts.";
    private static final String ANNOTATION = "Annotation";
    private final String jenaPutputFormat;


    // Prefix
    private static final String aofPrefix = "http://purl.org/ao/foaf/";
    private static final String aoPrefix = "http://purl.org/ao/";
    private static final String pavPrefix = "http://purl.org/pav/2.0/";
    private static final String annPrefix = "http://www.w3.org/2000/10/annotation-ns#";
    private static final String aosPrefix = "http://purl.org/ao/selectors/";
    private static final String aot = "http://purl.org/ao/types/";
    private static final String foafPrefix = "http://xmlns.com/foaf/0.1/";
    // URLs
    private static final String createdByURL = "http://bioportal.bioontology.org/servlet";
    private static final String contextURL = "http://my.example.org/se/10300";
    private static final String rootURL = "http://bioportal.bioontology.org/servlet/ann/";
    private static final String root2URL = "http://bioportal.bioontology.org/servlet/sel/";
    private static final String onDocumentURL = "http://data.bioontology.org/servlet?";

    /**
     * Output generator based on Apache Jena, can generate the output from the annotation modelin any of the formats
     * supported by Jena
     *
     * @param jenaOutputFormat The jena output format,
     *                         {@see https://jena.apache.org/documentation/io/rdf-output.html#jena_model_write_formats}
     *                         for a list of all supported formats
     */
    public JenaOutputGenerator(final String jenaOutputFormat) {
        this.jenaPutputFormat = jenaOutputFormat;
    }

    @Override
    public AnnotatorOutput generate(final List<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final Model jenaModel = ModelFactory.createOntologyModel();
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

    private void initializeModel(final Model model, final String annotatorURI) {
        //Describing the servlet used
        final Resource annotatorResource = model.createResource(annotatorURI);
        final Resource annotatorType1 = model.createResource("http://www.w3.org/ns/prov#SoftwareAgent");
        final Resource annotatorType2 = model.createResource("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Data_Computation_Service");
        final Resource annotatorType3 = model.createResource("http://dbpedia.org/resource/Software");

        final Property foafNameProp = model.createProperty(foafPrefix + "getName");
        final Property foafDescriptionProp = model.createProperty(foafPrefix + "description");

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

        model.add(annotatorResource, RDF.type, annotatorType1)
                .add(annotatorResource, RDF.type, annotatorType2)
                .add(annotatorResource, RDF.type, annotatorType3)
                .add(annotatorResource, foafNameProp, annotatorFoafName)
                .add(annotatorResource, foafDescriptionProp, annotatorFoafDescription);


    }

    @SuppressWarnings("HardcodedFileSeparator")
    private void appendAnnotationsToModel(final Model model, final Iterable<AnnotationToken> annotationTokens, final String topicURL, final String qualifierSubclass, Integer count, final int uid) {


        for (final AnnotationToken annotationToken : annotationTokens) {
            final String text = annotationToken.getText();
            final int from = annotationToken.getFrom();
            final int to = annotationToken.getTo();
            final int taill = (to - from) + 1;

            final Resource root = model.createResource(rootURL + uid + "/" + count);

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

    private String getSelectorURI(final int from, final int size, final Model model) {
        final String queryString = "select distinct ?sel where {?sel <" + aosPrefix + "range> ?range ; <" + aosPrefix + "offset> ?offset . FILTER (?range = " + Integer.toString(size) + " && ?offset = " + Integer.toString(from) + ") } LIMIT 10";

        final Query query = QueryFactory.create(queryString);
        Resource r = null;
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            final ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                final QuerySolution soln = results.nextSolution();
                r = soln.getResource("sel");
            }
            qexec.close();
        }

        String selectorURI = "";
        if (r != null) {
            selectorURI = r.getURI();
        }

        return selectorURI;
    }
}
