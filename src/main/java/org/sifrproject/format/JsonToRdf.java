package org.sifrproject.format;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Date;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.icu.text.SimpleDateFormat;


public class JsonToRdf {
	public static Model m = ModelFactory.createDefaultModel();
	//              Prefixs
	public static String aofPrefix = "http://purl.org/ao/foaf/";
	public static  String aoPrefix = "http://purl.org/ao/";
	public static  String aotrPrefix = "http://purl.org/ao/types/";
	public static String pavPrefix ="http://purl.org/pav/2.0/";
	public static  String annPrefix ="http://www.w3.org/2000/10/annotation-ns#";
	public static String aosPrefix ="http://purl.org/ao/selectors/";
	public static String aot="http://purl.org/ao/types/";
	//               URLs
	public static String hasTopicURL ="";
	public static String createdByURL =  "http://bioportal.bioontology.org/annotator" ;
	public static String  contextURL = "http://my.example.org/se/10300";
	public static String  rootURL ="http://bioportal.bioontology.org/annotator/ann/";
	public static String  root2URL = "http://bioportal.bioontology.org/annotator/sel/";     
	public static String  onDocumentURL = "http://data.bioontology.org/annotator?"; 
	public static Random generator; 
	public static int CptAnnotation=0; 
	public static ArrayList<String> termeAnnote;
	public static Dictionary<String, ArrayList<String>> Dictscoreconcept = new Hashtable<String, ArrayList<String>>();
	
	public static String FromJsonToRDF(JSONArray fullJsonAnnotation) 
	{
		JSONParser parser = new JSONParser();
		generator = new Random();
		 int CptAnnotationR = generator.nextInt(10000);
		 
		 Random R = new Random();
		 int RondomRessource = R.nextInt(11000);

 			JSONArray a = fullJsonAnnotation;
			JSONObject annotatedClass;
			for (Object o : a) 
			{
				JSONObject annotation = (JSONObject) o;
				// Calculer le score de l'annotation directe
				annotatedClass = (JSONObject) annotation.get("annotatedClass");
				// Récupérer l'id de concept de l'annotation directe
				hasTopicURL = (String) annotatedClass.get("@id");
				// Récupérer l'annotation directe et son matchType
				JSONArray annotations = (JSONArray) annotation.get("annotations");
			    termeAnnote = new ArrayList();
				//Récupérer les annotations directes
				for (Object c : annotations) 
				{ 
					JSONObject uneannotation = (JSONObject) c;
					String text = (String) uneannotation.get("text");
					Long from = (Long) uneannotation.get("from");
					Long to = (Long) uneannotation.get("to");
					Long taill =to-from+1;
					 Resource root = m.createResource( rootURL+CptAnnotationR+"/"+CptAnnotation );
					 System.out.println("Concept est  :"+CptAnnotation+ "topic "+hasTopicURL);
					 //Document provenanace
					 Property annotatesDocument = m.createProperty( aofPrefix+"annotatesDocument" );
					 Resource annotatesDocumentResource = m.createResource( onDocumentURL+CptAnnotationR );
					 //Annotation topic
					 Property context = m.createProperty( aoPrefix+"context" );
					 Property hasTopic = m.createProperty( aoPrefix+"hasTopic" );
					 Resource contextResource = m.createResource( contextURL);
					 Resource hasTopicResource = m.createResource(hasTopicURL );
					 //Annotation provenanace
					 Property createdBy = m.createProperty( pavPrefix+"createdBy" );
					 Property createdOn = m.createProperty(pavPrefix+"createdOn");
					 Resource createdByResource = m.createResource( createdByURL );
					 SimpleDateFormat formater = null;
					 Date aujourdhui1 = new Date();
					 formater = new SimpleDateFormat("dd-MM-yy");
					 System.out.println(formater.format(aujourdhui1));
					 Resource root2;
					 root2 = m.createResource( root2URL+CptAnnotationR+"/"+CptAnnotation  );
					 System.out.println("Max est :"+CptAnnotation+"  terme : "+text);
					 // //Annotation Selecteur
					 Property exact = m.createProperty( aoPrefix+"exact" );
					 Property offset = m.createProperty( aoPrefix+"offset" );
					 Property range = m.createProperty( aoPrefix+"range" );
					 //Document provenanace
					 Property onDocument = m.createProperty(aofPrefix+"onDocument");
					 Resource  r1 = m.createResource(aot+"ExactQualifier");
					 Resource  r2 = m.createResource(aot+"Qualifier");
					 Resource  r3 = m.createResource(aoPrefix+"Annotation");
					 Resource  r4 = m.createResource(annPrefix+"Annotation");
					 //
					 Resource  r5 = m.createResource(aoPrefix+"Selector");
					 Resource  r6 = m.createResource(aosPrefix+"TextSelector");
					 Resource  r7 = m.createResource(aosPrefix+"OffsetRangeSelector");
					 Resource onDocumentResource = m.createResource(onDocumentURL+CptAnnotationR);	 
					 m.add(root2, onDocument, onDocumentResource).add(root2, range, taill.toString()).add(root2, exact, text).add(root2,offset,from.toString()).add(root2, RDF.type, r7).add(root2, RDF.type, r6).add(root2, RDF.type, r5);
					 m.add( root, createdBy, createdByResource ).add(root, createdOn, formater.format(aujourdhui1)).add( root, hasTopic, hasTopicResource ).add( root, context, root2 ).add( root, annotatesDocument, annotatesDocumentResource ).add(root, RDF.type, r4).add(root, RDF.type, r3).add(root, RDF.type, r2).add(root, RDF.type, r1);
						
					 CptAnnotation++;
				}
				//Récupérer les annotations hiérarchiques
				JSONArray hierarchy = (JSONArray) annotation.get("hierarchy");
				for (Object h : hierarchy) 
				{
					JSONObject unehierarchie = (JSONObject) h;
					annotatedClass = (JSONObject) unehierarchie.get("annotatedClass");
					hasTopicURL = (String) annotatedClass.get("@id");
					for (Object c : annotations) 
					{
						JSONObject uneannotation = (JSONObject) c;
						String text = (String) uneannotation.get("text");
						Long from = (Long) uneannotation.get("from");
						Long to = (Long) uneannotation.get("to");
						Long taill =to-from+1;
						 Resource root = m.createResource( rootURL+CptAnnotationR+"/"+CptAnnotation );
						 System.out.println("Concept est  :"+CptAnnotation+ "topic "+hasTopicURL);
						 //Document provenanace
						 Property annotatesDocument = m.createProperty( aofPrefix+"annotatesDocument" );
						 Resource annotatesDocumentResource = m.createResource( onDocumentURL+CptAnnotationR );
						 //Annotation topic
						 Property context = m.createProperty( aoPrefix+"context" );
						 Property hasTopic = m.createProperty( aoPrefix+"hasTopic" );
						 Resource contextResource = m.createResource( contextURL);
						 Resource hasTopicResource = m.createResource(hasTopicURL );
						 //Annotation provenanace
						 Property createdBy = m.createProperty( pavPrefix+"createdBy" );
						 Property createdOn = m.createProperty(pavPrefix+"createdOn");
						 Resource createdByResource = m.createResource( createdByURL );
						 SimpleDateFormat formater = null;
						 Date aujourdhui1 = new Date();
						 formater = new SimpleDateFormat("dd-MM-yy");
						 System.out.println(formater.format(aujourdhui1));
						 
						 Resource root2;
						 root2 = m.createResource( root2URL+CptAnnotationR+"/"+CptAnnotation  );
						 System.out.println("Max est :"+CptAnnotation+"  terme : "+text);
						 // //Annotation Selecteur
						 Property exact = m.createProperty( aoPrefix+"exact" );
						 Property offset = m.createProperty( aoPrefix+"offset" );
						 Property range = m.createProperty( aoPrefix+"range" );
						 //Document provenanace
						 Property onDocument = m.createProperty(aofPrefix+"onDocument");
						 Resource onDocumentResource = m.createResource(onDocumentURL+CptAnnotationR);	
						 Resource  r1 = m.createResource(aot+"BroadQualifier");
						 Resource  r2 = m.createResource(aot+"Qualifier");
						 Resource  r3 = m.createResource(aoPrefix+"Annotation");
						 Resource  r4 = m.createResource(annPrefix+"Annotation");
						 //
						 Resource  r5 = m.createResource(aoPrefix+"Selector");
						 Resource  r6 = m.createResource(aosPrefix+"TextSelector");
						 Resource  r7 = m.createResource(aosPrefix+"OffsetRangeSelector");	
						 m.add(root2, onDocument, onDocumentResource).add(root2, range, taill.toString()).add(root2, exact, text).add(root2,offset,from.toString()).add(root2, RDF.type, r7).add(root2, RDF.type, r6).add(root2, RDF.type, r5);;
						 m.add( root, createdBy, createdByResource ).add(root, createdOn, formater.format(aujourdhui1)).add( root, hasTopic, hasTopicResource ).add( root, context, root2 ).add( root, annotatesDocument, annotatesDocumentResource ).add(root, RDF.type, r4).add(root, RDF.type, r3).add(root, RDF.type, r2).add(root, RDF.type, r1);
						 CptAnnotation++;
					}
				}
				//Récupérer les mapping
				JSONArray mappings = (JSONArray) annotation.get("mappings");
				for (Object h : mappings) 
				{
					JSONObject unehierarchie = (JSONObject) h;
					annotatedClass = (JSONObject) unehierarchie.get("annotatedClass");
					hasTopicURL = (String) annotatedClass.get("@id");
					for (Object c : annotations) 
					{
						JSONObject uneannotation = (JSONObject) c;
						String text = (String) uneannotation.get("text");
						Long from = (Long) uneannotation.get("from");
						Long to = (Long) uneannotation.get("to");
						Long taill =to-from+1;
						 Resource root = m.createResource( rootURL+CptAnnotationR+"/"+CptAnnotation );
						 System.out.println("Concept est  :"+CptAnnotation+ "topic "+hasTopicURL);
						 //Document provenanace
						 Property annotatesDocument = m.createProperty( aofPrefix+"annotatesDocument" );
						 Resource annotatesDocumentResource = m.createResource( onDocumentURL+CptAnnotationR );
						 //Annotation topic
						 Property context = m.createProperty( aoPrefix+"context" );
						 Property hasTopic = m.createProperty( aoPrefix+"hasTopic" );
						 Resource contextResource = m.createResource( contextURL);
						 Resource hasTopicResource = m.createResource(hasTopicURL );
						 //Annotation provenanace
						 Property createdBy = m.createProperty( pavPrefix+"createdBy" );
						 Property createdOn = m.createProperty(pavPrefix+"createdOn");
						 Resource createdByResource = m.createResource( createdByURL );
						 SimpleDateFormat formater = null;
						 Date aujourdhui1 = new Date();
						 formater = new SimpleDateFormat("dd-MM-yy");
						 System.out.println(formater.format(aujourdhui1));
						 
						 Resource root2;
						 root2 = m.createResource( root2URL+CptAnnotationR+"/"+CptAnnotation  );
						 System.out.println("Max est :"+CptAnnotation+"  terme : "+text);
						 // //Annotation Selecteur
						 Property exact = m.createProperty( aoPrefix+"exact" );
						 Property offset = m.createProperty( aoPrefix+"offset" );
						 Property range = m.createProperty( aoPrefix+"range" );
						 //Document provenanace
						 Property onDocument = m.createProperty(aofPrefix+"onDocument");
						 Resource onDocumentResource = m.createResource(onDocumentURL+CptAnnotationR);	
						 Resource  r1 = m.createResource(aot+"CloseQualifier");
						 Resource  r2 = m.createResource(aot+"Qualifier");
						 Resource  r3 = m.createResource(aoPrefix+"Annotation");
						 Resource  r4 = m.createResource(annPrefix+"Annotation");
						 //
						 Resource  r5 = m.createResource(aoPrefix+"Selector");
						 Resource  r6 = m.createResource(aosPrefix+"TextSelector");
						 Resource  r7 = m.createResource(aosPrefix+"OffsetRangeSelector");	
						 m.add(root2, onDocument, onDocumentResource).add(root2, range, taill.toString()).add(root2, exact, text).add(root2,offset,from.toString()).add(root2, RDF.type, r7).add(root2, RDF.type, r6).add(root2, RDF.type, r5);;
						 m.add( root, createdBy, createdByResource ).add(root, createdOn, formater.format(aujourdhui1)).add( root, hasTopic, hasTopicResource ).add( root, context, root2 ).add( root, annotatesDocument, annotatesDocumentResource ).add(root, RDF.type, r4).add(root, RDF.type, r3).add(root, RDF.type, r2).add(root, RDF.type, r1);
						 CptAnnotation++;
					}
				}
			}
			m.setNsPrefix( "aof", aofPrefix );
			m.setNsPrefix( "ao", aoPrefix );
			m.setNsPrefix( "pav", pavPrefix );
			m.setNsPrefix( "aos", aosPrefix );
			//m.write( System.out );
			StringWriter rdfOutput = new StringWriter();
			m.write(rdfOutput, "RDF/XML");
			return rdfOutput.toString();
	}
	
	
	public static void main (String[] args) throws FileNotFoundException, IOException, ParseException{
		String jsonArrayStr = "[  	{  	\"annotatedClass\": {  		\"@id\": \"http://edamontology.org/topic_2640\",  		\"@type\": \"http://www.w3.org/2002/07/owl#Class\",  	\"links\": {  		\"self\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM/classes/http%3A%2F%2Fedamontology.org%2Ftopic_2640\",  		\"ontology\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM\",  		\"children\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM/classes/http%3A%2F%2Fedamontology.org%2Ftopic_2640/children\",  		\"parents\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM/classes/http%3A%2F%2Fedamontology.org%2Ftopic_2640/parents\",  		\"descendants\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM/classes/http%3A%2F%2Fedamontology.org%2Ftopic_2640/descendants\",  		\"ancestors\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM/classes/http%3A%2F%2Fedamontology.org%2Ftopic_2640/ancestors\",  		\"tree\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM/classes/http%3A%2F%2Fedamontology.org%2Ftopic_2640/tree\",  		\"notes\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM/classes/http%3A%2F%2Fedamontology.org%2Ftopic_2640/notes\",  		\"mappings\": \"http://bioportal.lirmm.fr:8082/ontologies/EDAM/classes/http%3A%2F%2Fedamontology.org%2Ftopic_2640/mappings\",  		\"ui\": \"http://http://bioportal.lirmm.fr/ontologies/EDAM?p=classes&conceptid=http%3A%2F%2Fedamontology.org%2Ftopic_2640\",  		\"@context\": {  			\"self\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ontology\": \"http://data.bioontology.org/metadata/Ontology\",  			\"children\": \"http://www.w3.org/2002/07/owl#Class\",  			\"parents\": \"http://www.w3.org/2002/07/owl#Class\",  			\"descendants\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",  			\"tree\": \"http://www.w3.org/2002/07/owl#Class\",  			\"notes\": \"http://data.bioontology.org/metadata/Note\",  			\"mappings\": \"http://data.bioontology.org/metadata/Mapping\",  			\"ui\": \"http://www.w3.org/2002/07/owl#Class\"  		}  	},  		\"@context\": {  			\"@vocab\": \"http://data.bioontology.org/metadata/\"  		}  	},  	\"hierarchy\": [ ],  	\"annotations\": [  			{  				\"from\": 1,  				\"to\": 6,  				\"matchType\": \"SYN\",  				\"text\": \"CANCER\"  			}  		],  		\"mappings\": [ ]  	},  	{  	\"annotatedClass\": {  		\"@id\": \"http://purl.bioontology.org/ontology/MSHFRE/D009369\",  		\"@type\": \"http://www.w3.org/2002/07/owl#Class\",  	\"links\": {  		\"self\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369\",  		\"ontology\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE\",  		\"children\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369/children\",  		\"parents\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369/parents\",  		\"descendants\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369/descendants\",  		\"ancestors\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369/ancestors\",  		\"tree\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369/tree\",  		\"notes\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369/notes\",  		\"mappings\": \"http://bioportal.lirmm.fr:8082/ontologies/MSHFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369/mappings\",  		\"ui\": \"http://http://bioportal.lirmm.fr/ontologies/MSHFRE?p=classes&conceptid=http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMSHFRE%2FD009369\",  		\"@context\": {  			\"self\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ontology\": \"http://data.bioontology.org/metadata/Ontology\",  			\"children\": \"http://www.w3.org/2002/07/owl#Class\",  			\"parents\": \"http://www.w3.org/2002/07/owl#Class\",  			\"descendants\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",  			\"tree\": \"http://www.w3.org/2002/07/owl#Class\",  			\"notes\": \"http://data.bioontology.org/metadata/Note\",  			\"mappings\": \"http://data.bioontology.org/metadata/Mapping\",  			\"ui\": \"http://www.w3.org/2002/07/owl#Class\"  		}  	},  		\"@context\": {  			\"@vocab\": \"http://data.bioontology.org/metadata/\"  		}  	},  	\"hierarchy\": [ ],  	\"annotations\": [  			{  				\"from\": 1,  				\"to\": 6,  				\"matchType\": \"SYN\",  				\"text\": \"CANCER\"  			}  		],  		\"mappings\": [ ]  	},  	{  	\"annotatedClass\": {  		\"@id\": \"http://purl.bioontology.org/ontology/MDRFRE/10007050\",  		\"@type\": \"http://www.w3.org/2002/07/owl#Class\",  	\"links\": {  		\"self\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050\",  		\"ontology\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE\",  		\"children\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050/children\",  		\"parents\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050/parents\",  		\"descendants\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050/descendants\",  		\"ancestors\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050/ancestors\",  		\"tree\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050/tree\",  		\"notes\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050/notes\",  		\"mappings\": \"http://bioportal.lirmm.fr:8082/ontologies/MDRFRE/classes/http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050/mappings\",  		\"ui\": \"http://http://bioportal.lirmm.fr/ontologies/MDRFRE?p=classes&conceptid=http%3A%2F%2Fpurl.bioontology.org%2Fontology%2FMDRFRE%2F10007050\",  		\"@context\": {  			\"self\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ontology\": \"http://data.bioontology.org/metadata/Ontology\",  			\"children\": \"http://www.w3.org/2002/07/owl#Class\",  			\"parents\": \"http://www.w3.org/2002/07/owl#Class\",  			\"descendants\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",  			\"tree\": \"http://www.w3.org/2002/07/owl#Class\",  			\"notes\": \"http://data.bioontology.org/metadata/Note\",  			\"mappings\": \"http://data.bioontology.org/metadata/Mapping\",  			\"ui\": \"http://www.w3.org/2002/07/owl#Class\"  		}  	},  		\"@context\": {  			\"@vocab\": \"http://data.bioontology.org/metadata/\"  		}  	},  	\"hierarchy\": [ ],  	\"annotations\": [  			{  				\"from\": 1,  				\"to\": 6,  				\"matchType\": \"PREF\",  				\"text\": \"CANCER\"  			}  		],  		\"mappings\": [ ]  	},  	{  	\"annotatedClass\": {  		\"@id\": \"http://chu-rouen.fr/cismef/SNOMED_int.#M-80003\",  		\"@type\": \"http://www.w3.org/2002/07/owl#Class\",  	\"links\": {  		\"self\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003\",  		\"ontology\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE\",  		\"children\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/children\",  		\"parents\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/parents\",  		\"descendants\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/descendants\",  		\"ancestors\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/ancestors\",  		\"tree\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/tree\",  		\"notes\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/notes\",  		\"mappings\": \"http://bioportal.lirmm.fr:8082/ontologies/SNMIFRE/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003/mappings\",  		\"ui\": \"http://http://bioportal.lirmm.fr/ontologies/SNMIFRE?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FSNOMED_int.%23M-80003\",  		\"@context\": {  			\"self\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ontology\": \"http://data.bioontology.org/metadata/Ontology\",  			\"children\": \"http://www.w3.org/2002/07/owl#Class\",  			\"parents\": \"http://www.w3.org/2002/07/owl#Class\",  			\"descendants\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",  			\"tree\": \"http://www.w3.org/2002/07/owl#Class\",  			\"notes\": \"http://data.bioontology.org/metadata/Note\",  			\"mappings\": \"http://data.bioontology.org/metadata/Mapping\",  			\"ui\": \"http://www.w3.org/2002/07/owl#Class\"  		}  	},  		\"@context\": {  			\"@vocab\": \"http://data.bioontology.org/metadata/\"  		}  	},  	\"hierarchy\": [ ],  	\"annotations\": [  			{  				\"from\": 1,  				\"to\": 6,  				\"matchType\": \"SYN\",  				\"text\": \"CANCER\"  			}  		],  		\"mappings\": [ ]  	},  	{  	\"annotatedClass\": {  		\"@id\": \"http://chu-rouen.fr/cismef/MedlinePlus#T25\",  		\"@type\": \"http://www.w3.org/2002/07/owl#Class\",  	\"links\": {  		\"self\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25\",  		\"ontology\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS\",  		\"children\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25/children\",  		\"parents\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25/parents\",  		\"descendants\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25/descendants\",  		\"ancestors\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25/ancestors\",  		\"tree\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25/tree\",  		\"notes\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25/notes\",  		\"mappings\": \"http://bioportal.lirmm.fr:8082/ontologies/MEDLINEPLUS/classes/http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25/mappings\",  		\"ui\": \"http://http://bioportal.lirmm.fr/ontologies/MEDLINEPLUS?p=classes&conceptid=http%3A%2F%2Fchu-rouen.fr%2Fcismef%2FMedlinePlus%23T25\",  		\"@context\": {  			\"self\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ontology\": \"http://data.bioontology.org/metadata/Ontology\",  			\"children\": \"http://www.w3.org/2002/07/owl#Class\",  			\"parents\": \"http://www.w3.org/2002/07/owl#Class\",  			\"descendants\": \"http://www.w3.org/2002/07/owl#Class\",  			\"ancestors\": \"http://www.w3.org/2002/07/owl#Class\",  			\"tree\": \"http://www.w3.org/2002/07/owl#Class\",  			\"notes\": \"http://data.bioontology.org/metadata/Note\",  			\"mappings\": \"http://data.bioontology.org/metadata/Mapping\",  			\"ui\": \"http://www.w3.org/2002/07/owl#Class\"  		}  	},  		\"@context\": {  			\"@vocab\": \"http://data.bioontology.org/metadata/\"  		}  	},  	\"hierarchy\": [ ],  	\"annotations\": [  				{  					\"from\": 1,  					\"to\": 6,  					\"matchType\": \"PREF\",  					\"text\": \"CANCER\"  				}  			],  			\"mappings\": [ ]  	}  ]";
		
		JSONParser parser = new JSONParser();
		JSONArray jsonInput = (JSONArray) parser.parse(jsonArrayStr);
		
		String rdfResults = FromJsonToRDF(jsonInput);

	    System.out.println(rdfResults);
	}
}

