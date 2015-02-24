package org.sifrproject.format;

import java.io.StringWriter;
import java.util.Date;
import java.util.Random;

import org.sifrproject.util.JSON;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.icu.text.SimpleDateFormat;

public class JsonToRdf {
    
    // Prefix
    public static final String aofPrefix = "http://purl.org/ao/foaf/";
    public static final String aoPrefix = "http://purl.org/ao/";
    public static final String aotrPrefix = "http://purl.org/ao/types/";
    public static final String pavPrefix = "http://purl.org/pav/2.0/";
    public static final String annPrefix = "http://www.w3.org/2000/10/annotation-ns#";
    public static final String aosPrefix = "http://purl.org/ao/selectors/";
    public static final String aot = "http://purl.org/ao/types/";
    // URLs
    public static final String createdByURL = "http://bioportal.bioontology.org/annotator";
    public static final String contextURL = "http://my.example.org/se/10300";
    public static final String rootURL = "http://bioportal.bioontology.org/annotator/ann/";
    public static final String root2URL = "http://bioportal.bioontology.org/annotator/sel/";
    public static final String onDocumentURL = "http://data.bioontology.org/annotator?";
    
    
    public static String convert(JSON jsonAnnotation) {
        Model m = ModelFactory.createDefaultModel();
        int uid = (new Random()).nextInt(10000);
        int count = 0;
 
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

        StringWriter rdfOutput = new StringWriter();
        m.write(rdfOutput, "RDF/XML");
        
        return rdfOutput.toString();
    }

    private static int append2model(Model m, JSON annotations, String topicURL, String qualifierSubclass, int count, int uid){

        for (JSON uneannotation : annotations.arrayContent()) {
            String text = uneannotation.getString("text");
            Long from   = uneannotation.getLong("from");
            Long to     = uneannotation.getLong("to");
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
            SimpleDateFormat formater  = new SimpleDateFormat("dd-MM-yy");
            
            Resource root2 = m.createResource(root2URL + uid + "/" + count);
            
            // Annotation Selector
            Property exact  = m.createProperty(aoPrefix + "exact");
            Property offset = m.createProperty(aoPrefix + "offset");
            Property range  = m.createProperty(aoPrefix + "range");
            
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
            
            m.add(root2, onDocument, onDocumentResource)
                    .add(root2, range, taill.toString())
                    .add(root2, exact, text)
                    .add(root2, offset, from.toString())
                    .add(root2, RDF.type, r7).add(root2, RDF.type, r6)
                    .add(root2, RDF.type, r5);
            
            m.add(root, createdBy, createdByResource)
                    .add(root, createdOn, formater.format(today))
                    .add(root, hasTopic, hasTopicResource)
                    .add(root, context, root2)
                    .add(root, annotatesDocument, annotatesDocumentResource)
                    .add(root, RDF.type, r4).add(root, RDF.type, r3)
                    .add(root, RDF.type, r2).add(root, RDF.type, r1);

            count++;
        }
        
        return count;
    }
    
}
