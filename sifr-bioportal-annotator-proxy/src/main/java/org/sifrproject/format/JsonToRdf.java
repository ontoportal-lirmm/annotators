package org.sifrproject.format;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.icu.text.SimpleDateFormat;
import org.sifrproject.util.JSON;

import java.io.StringWriter;
import java.util.Date;
import java.util.Random;

public class JsonToRdf {
    
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
    //public static String annotatorURI = "";
    
    
    public static String convert(JSON jsonAnnotation, String annotatorURI) {
        Model m = ModelFactory.createDefaultModel();
        int uid = (new Random()).nextInt(10000);
        int count = 0;

        //Describing the servlet used
        Resource annotatorResource = m.createResource(annotatorURI);
        Resource annotatorType1 = m.createResource("http://www.w3.org/ns/prov#SoftwareAgent");
        Resource annotatorType2 = m.createResource("http://bioontology.org/ontologies/BiomedicalResourceOntology.owl#Data_Computation_Service");
        Resource annotatorType3 = m.createResource("http://dbpedia.org/resource/Software");
        
        Property foafNameProp = m.createProperty(foafPrefix + "name");
        Property foafDescriptionProp = m.createProperty(foafPrefix + "description");
        
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
        
        m.add(annotatorResource, RDF.type, annotatorType1)
        		.add(annotatorResource, RDF.type, annotatorType2)
        		.add(annotatorResource, RDF.type, annotatorType3)
        		.add(annotatorResource, foafNameProp, annotatorFoafName)
        		.add(annotatorResource, foafDescriptionProp, annotatorFoafDescription);
        
        
        for (JSON annotation : jsonAnnotation.arrayContent()) {
            
            // convert this annotatedClass
            JSON annotations    = annotation.get("annotations");
            String topicURL  = annotation.get("annotatedClass").getString("@id");
            count = append2model(m, annotations, topicURL, "ExactQualifier", count, uid);

            // convert annotatedClass in hierarchy 
            JSON hierarchies = annotation.get("hierarchy");
            for (JSON hierarchy : hierarchies.arrayContent()) {
                topicURL = hierarchy.get("annotatedClass").getString("@id");
                count = append2model(m, annotations, topicURL, "BroadQualifier", count, uid);
            }
            
            // convert annotatedClass in mapping
            JSON mappings = annotation.get("mappings");
            for (JSON mapping : mappings.arrayContent()) {
                topicURL = mapping.get("annotatedClass").getString("@id");
                count = append2model(m, annotations, topicURL, "CloseQualifier", count, uid);
            }
        }
        
        m.setNsPrefix("aof", aofPrefix);
        m.setNsPrefix("ao",  aoPrefix);
        m.setNsPrefix("pav", pavPrefix);
        m.setNsPrefix("aos", aosPrefix);
        m.setNsPrefix("foaf", foafPrefix);
        

        StringWriter rdfOutput = new StringWriter();
        m.write(rdfOutput, "RDF/XML");
        
        return rdfOutput.toString();
    }

    private static int append2model(Model m, JSON annotations, String topicURL, String qualifierSubclass, int count, int uid){

        for (JSON uneannotation : annotations.arrayContent()) {
            String text = uneannotation.getString("text");
            Long from   = uneannotation.getLong("from");
            Long to     = uneannotation.getLong("to");
            //int to = uneannotation.getInt("to");
            Long taill  = to - from + 1;
            
            Resource root = m.createResource(rootURL + uid + "/" + count);
            
            // Document provenance
            Property annotatesDocument = m.createProperty(aofPrefix + "annotatesDocument");
            Resource annotatesDocumentResource = m.createResource(onDocumentURL + uid);
            
            // Annotation topic
            Property context  = m.createProperty(aoPrefix + "context");
            Property hasTopic = m.createProperty(aoPrefix + "hasTopic");
            Resource hasTopicResource = m.createResource(topicURL);
            
            // Annotation provenance
            Property createdBy = m.createProperty(pavPrefix + "createdBy");
            Property createdOn = m.createProperty(pavPrefix + "createdOn");
            Resource createdByResource = m.createResource(createdByURL);
            
            Date today = new Date();
            SimpleDateFormat formater  = new SimpleDateFormat("yy-MM-dd");
            
            
            // Annotation Selector
            Property exact  = m.createProperty(aosPrefix + "exact");
            Property offset = m.createProperty(aosPrefix + "offset");
            Property range  = m.createProperty(aosPrefix + "range");
            
            // Document provenance
            Property onDocument = m.createProperty(aofPrefix + "onDocument");
            Resource onDocumentResource = m.createResource(onDocumentURL + uid);
            
            Resource r1 = m.createResource(aot + qualifierSubclass);
            Resource r2 = m.createResource(aot + "Qualifier");
            Resource r3 = m.createResource(aoPrefix  + "Annotation");
            Resource r4 = m.createResource(annPrefix + "Annotation");
            Resource r5 = m.createResource(aoPrefix  + "Selector");
            Resource r6 = m.createResource(aosPrefix + "TextSelector");
            Resource r7 = m.createResource(aosPrefix + "OffsetRangeSelector");
            
            String selectorURI = getSelectorURI(from, taill, m);
            Resource root2;
            
            if (selectorURI.equals("")) {
            	root2 = m.createResource(root2URL + uid + "/" + count);
	            m.add(root2, onDocument, onDocumentResource)
	                    .add(root2, range, m.createTypedLiteral(taill.toString(), XSDDatatype.XSDinteger))
	                    .add(root2, exact, text)
	                    .add(root2, offset, m.createTypedLiteral(from.toString(), XSDDatatype.XSDinteger))
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
        
        return count;
    }
    
    private static String getSelectorURI (Long from, Long size, Model m) {
    	String queryString = "select distinct ?sel where {?sel <" + aosPrefix + "range> ?range ; <" + aosPrefix + "offset> ?offset . FILTER (?range = " + size.toString() + " && ?offset = " + from.toString() + ") } LIMIT 10";
    	
        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, m);
	    ResultSet results = qexec.execSelect() ;
	    Resource r = null;
	    for ( ; results.hasNext() ; )
	    {
	    	QuerySolution soln = results.nextSolution() ;
  	    	r = soln.getResource("sel") ;
	    }
	    qexec.close() ;
        
        String selectorURI = "";
        if (r != null) {
        	selectorURI = r.getURI();
        }

    	return selectorURI;
    }
    
}
