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
import java.util.Random;


public class JenaOutputGenerator implements OutputGenerator {
    private final String outputMimeType;


    // Prefix
    private static final String aofPrefix = "http://purl.org/ao/foaf/";
    private static final String aoPrefix = "http://purl.org/ao/";
    private static final String aotrPrefix = "http://purl.org/ao/types/";
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
    public JenaOutputGenerator(String jenaOutputFormat) {
        this.outputMimeType = jenaOutputFormat;
    }

    @Override
    public AnnotatorOutput generate(Iterable<Annotation> annotations, String annotatorURI) {
        Model jenaModel = ModelFactory.createOntologyModel();
        initializeModel(jenaModel, annotatorURI);

        Integer count = 0;
        int uid = (new Random()).nextInt(10000);

        for (Annotation annotation : annotations) {
            String id = annotation.getAnnotatedClass().getId();
            AnnotationTokens tokens = annotation.getAnnotations();
            appendAnnotationsToModel(jenaModel, tokens, id, "ExactQualifier", count, uid);

            for (Mapping mapping : annotation.getMappings()) {
                appendAnnotationsToModel(jenaModel, tokens, mapping.getAnnotatedClass().getId(),
                        "CloseQualifier", count, uid);
            }
            for (HierarchyElement hierarchyElement : annotation.getHierarchy()) {
                appendAnnotationsToModel(jenaModel, tokens, hierarchyElement.getAnnotatedClass().getId(),
                        "BroadQualifier", count, uid);
            }

        }

        StringWriter rdfOutput = new StringWriter();
        jenaModel.write(rdfOutput, outputMimeType.toUpperCase());
        return new LIRMMAnnotatorOutput(rdfOutput.toString(), String.format("text/%s", outputMimeType));
    }

    private void initializeModel(Model model, String annotatorURI) {
        //Describing the servlet used
        Resource annotatorResource = model.createResource(annotatorURI);
        Resource annotatorType1 = model.createResource("http://www.w3.org/ns/prov#SoftwareAgent");
        Resource annotatorType2 = model.createResource("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Data_Computation_Service");
        Resource annotatorType3 = model.createResource("http://dbpedia.org/resource/Software");

        Property foafNameProp = model.createProperty(foafPrefix + "name");
        Property foafDescriptionProp = model.createProperty(foafPrefix + "description");

        String annotatorFoafName;
        String annotatorFoafDescription;

        switch (annotatorURI) {
            case "http://vm-bioportal-vincent:8080/servlet?":
                annotatorFoafName = "SIFR Annotator";
                annotatorFoafDescription = "The SIFR Annotator is a specific version of the NCBO Annotator but for French ontologies & terminologies. You shall use it to annotate French biomedical text with ontology concepts.";
                break;
            case "http://bioportal.lirmm.fr:8080/servlet?":
                annotatorFoafName = "SIFR Annotator";
                annotatorFoafDescription = "The SIFR Annotator is a specific version of the NCBO Annotator but for French ontologies & terminologies. You shall use it to annotate French biomedical text with ontology concepts.";
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

    private void appendAnnotationsToModel(Model m, AnnotationTokens annotationTokens, String topicURL, String qualifierSubclass, Integer count, int uid) {


        for (AnnotationToken annotationToken : annotationTokens) {
            String text = annotationToken.getText();
            int from = annotationToken.getFrom();
            int to = annotationToken.getTo();
            int taill = to - from + 1;

            Resource root = m.createResource(rootURL + uid + "/" + count);

            // Document provenance
            Property annotatesDocument = m.createProperty(aofPrefix + "annotatesDocument");
            Resource annotatesDocumentResource = m.createResource(onDocumentURL + uid);

            // Annotation topic
            Property context = m.createProperty(aoPrefix + "context");
            Property hasTopic = m.createProperty(aoPrefix + "hasTopic");
            Resource hasTopicResource = m.createResource(topicURL);

            // Annotation provenance
            Property createdBy = m.createProperty(pavPrefix + "createdBy");
            Property createdOn = m.createProperty(pavPrefix + "createdOn");
            Resource createdByResource = m.createResource(createdByURL);

            Date today = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd");


            // Annotation Selector
            Property exact = m.createProperty(aosPrefix + "exact");
            Property offset = m.createProperty(aosPrefix + "offset");
            Property range = m.createProperty(aosPrefix + "range");

            // Document provenance
            Property onDocument = m.createProperty(aofPrefix + "onDocument");
            Resource onDocumentResource = m.createResource(onDocumentURL + uid);

            Resource r1 = m.createResource(aot + qualifierSubclass);
            Resource r2 = m.createResource(aot + "Qualifier");
            Resource r3 = m.createResource(aoPrefix + "Annotation");
            Resource r4 = m.createResource(annPrefix + "Annotation");
            Resource r5 = m.createResource(aoPrefix + "Selector");
            Resource r6 = m.createResource(aosPrefix + "TextSelector");
            Resource r7 = m.createResource(aosPrefix + "OffsetRangeSelector");

            String selectorURI = getSelectorURI(from, taill, m);
            Resource root2;

            if (selectorURI.equals("")) {
                root2 = m.createResource(root2URL + uid + "/" + count);
                m.add(root2, onDocument, onDocumentResource)
                        .add(root2, range, m.createTypedLiteral(Integer.toString(taill), XSDDatatype.XSDinteger))
                        .add(root2, exact, text)
                        .add(root2, offset, m.createTypedLiteral(Integer.toString(from), XSDDatatype.XSDinteger))
                        .add(root2, RDF.type, r7).add(root2, RDF.type, r6)
                        .add(root2, RDF.type, r5);
            } else {
                root2 = m.createResource(selectorURI);
            }

            m.add(root, createdBy, createdByResource)
                    .add(root, createdOn, m.createTypedLiteral(formater.format(today), XSDDatatype.XSDdate))
                    .add(root, hasTopic, hasTopicResource)
                    .add(root, context, root2)
                    .add(root, annotatesDocument, annotatesDocumentResource)
                    .add(root, RDF.type, r4).add(root, RDF.type, r3)
                    .add(root, RDF.type, r2).add(root, RDF.type, r1);

            count++;
        }
    }

    private String getSelectorURI(int from, int size, Model m) {
        String queryString = "select distinct ?sel where {?sel <" + aosPrefix + "range> ?range ; <" + aosPrefix + "offset> ?offset . FILTER (?range = " + Integer.toString(size) + " && ?offset = " + Integer.toString(from) + ") } LIMIT 10";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, m);
        ResultSet results = qexec.execSelect();
        Resource r = null;
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            r = soln.getResource("sel");
        }
        qexec.close();

        String selectorURI = "";
        if (r != null) {
            selectorURI = r.getURI();
        }

        return selectorURI;
    }
}
