package org.sifrproject.format;


import java.io.FileInputStream;
import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

public class AO {


    public static OWLModel owlModel=null;
    public static String uri = "http://annotation-ontology.googlecode.com/svn/trunk/annotation.owl";
    public static  String NAME_SPACE = "URI" + "#";
    // OWL values for class name
    public static String OWL_Annotation = "Annotation";
    public static String OWL_Selector = "Selector";
    public static String OWL_AnnotationCuration = "AnnotationCuration";
    public static String OWL_AnnotationSet = "AnnotationSet";
    public static String OWL_DocumentAnnotation = "DocumentAnnotation";
    public static String OWL_ann_Annotation = "ann:Annotation";
    public static String OWL_ans_Annotation  = "ans:Annotation ";
    public static String OWL_ann_Selector = "ann:Selector";
    public static String OWL_p3_XPointerSelector = "p3:XPointerSelector";
    public static String OWL_p3_ImageSelector = "p3:ImageSelector";
    public static String OWL_p3_InitEndCornerSelector = "p3:InitEndCornerSelector";

    // OWL values for object property name
    public static String OWL_status = "status";
    public static String OWL_pav_authoredOn = "pav:authoredOn";
    public static String OWL_pav_contributedOn = "pav:contributedOn";
    public static String OWL_pav_createdOn = "pav:createdOn";
    public static String OWL_pav_curatedOn = "pav:curatedOn";
    public static String OWL_pav_importedOn = "pav:importedOn";
    public static String OWL_pav_lastRefreshedOn = "pav:lastRefreshedOn";
    public static String OWL_pav_lastUpdateOn = "pav:lastUpdateOn";
    public static String OWL_pav_retrievedOn = "pav:retrievedOn";
    public static String OWL_pav_sourceAccessedOn = "pav:sourceAccessedOn";
    public static String OWL_pav_sourceLastAccessedOn = "pav:sourceLastAccessedOn";
    public static String OWL_pav_version = "pav:version";
    public static String OWL_p3_xpointer = "p3:xpointer";
    public static String OWL_p3_init = "p3:init";
    public static String OWL_p3_end = "p3:end";
    public static String OWL_p3_exact = "p3:exact";
    public static String OWL_p3_offset = "p3:offset";
    public static String OWL_p3_range = "p3:range";
    public static String OWL_p3_prefix = "p3:prefix";
    public static String OWL_p3_postfix = "p3:postfix";
    public static String OWL_foaf_aimChatID = "foaf:aimChatID";
    public static String OWL_foaf_birthday = "foaf_birthday";
    public static String OWL_foaf_family_name = "foaf:family_name";
    public static String OWL_foaf_firstName = "foaf:firstName";
    public static String OWL_foaf_geekcode = "foafgeekcode";
    public static String OWL_foaf_gender = "foaf:gender";
    public static String OWL_foaf_givenname = "foaf:givenname";
    public static String OWL_foaf_jabberID = "foaf:jabberID";
    public static String OWL_foaf_myersBriggs = "foaf:myersBriggs";
    public static String OWL_foaf_name = "foaf:name";
    public static String OWL_foaf_nick = "foaf:nick";
    public static String OWL_foaf_plan = "foaf:plan";
    public static String OWL_foaf_sha1 = "foaf:sha1";
    public static String OWL_foaf_surname = "foaf:surname";
    public static String OWL_foaf_title = "foaf:title";
    public static String OWL_foaf_yahooChatID = "foaf:yahooChatID";
    public static String OWL_foaf_homepage = "foaf:homepage";
    public static String OWL_foaf_isPrimaryTopicOf = "foaf:isPrimaryTopicOf";
    public static String OWL_foaf_mbox = "foaf:mbox";
    public static String OWL_foaf_weblog = "foaf:weblog";
    public static String OWL_onResource = "onResource";
    public static String OWL_annotatesResource = "annotatesResource";
    public static String OWL_onSourceDocument = "onSourceDocument";
    public static String OWL_hasTopic = "hasTopic";
    public static String OWL_body = "body";
    public static String OWL_item = "item";
    public static String OWL_pav_curates = "pav:curates";
    public static String OWL_context = "context";
    public static String OWL_pav_authoredBy = "pav:authoredBy";
    public static String OWL_pav_contributedBy = "pav:contributedBy";
    public static String OWL_pav_createdAt = "pav:createdAt";
    public static String OWL_pav_createdBy = "pav:createdBy";
    public static String OWL_pav_createdWith = "pav:createdWith";
    public static String OWL_pav_curatedBy = "pav:curatedBy";
    public static String OWL_pav_derivedFrom = "pav:derivedFrom";
    public static String OWL_pav_importedBy = "pav:importedBy";
    public static String OWL_pav_importedFrom = "pav:importedFrom";
    public static String OWL_pav_previousVersion = "pav:previousVersion";
    public static String OWL_pav_providedBy = "pav:providedBy";
    public static String OWL_pav_retrievedBy = "pav:retrievedBy";
    public static String OWL_pav_retrievedFrom = "pav:retrievedFrom";
    public static String OWL_pav_sourceAccessedAt = "pav:sourceAccessedAt";
    public static String OWL_pav_sourceAccessedBy = "pav:sourceAccessedBy";
    public static String OWL_ann_hasTopic = "ann:hasTopic";
    public static String OWL_ann_body = "ann:body";
    public static String OWL_ann_annotatesResource = "ann:annotatesResource";
    public static String OWL_ann_context = "ann:context";
    public static String OWL_ans_annotates = "ans:annotates";
    public static String OWL_ans_body = "ans:body";
    public static String OWL_ans_context = "ans:context";
    public static String OWL_ans_related = "ans:related";
    public static String OWL_bkm_hasTopic = "bkm:hasTopic";
    public static String OWL_aof_annotatesDocument = "aof:annotatesDocument";
    public static String OWL_aof_onDocument = "aof:onDocument";
    public static String OWL_foaf_currentProject = "foaf:currentProject";
    public static String OWL_foaf_depiction = "foaf:depiction";
    public static String OWL_foaf_depicts = "foaf:depicts";
    public static String OWL_foaf_fundedBy = "foaf:fundedBy";
    public static String OWL_oaf_img = "oaf:img";
    public static String OWL_foaf_interest = "foaf:interest";
    public static String OWL_foaf_knows = "foaf:knows";
    public static String OWL_foaf_logo = "foaf:logo";
    public static String OWL_foaf_made = "foaf:made";
    public static String OWL_foaf_maker = "foaf:maker";
    public static String OWL_foaf_member = "foaf:member";
    public static String OWL_foaf_page = "foaf:page";
    public static String OWL_foaf_pastProject = "foaf:pastProject";
    public static String OWL_foaf_phone = "foaf:phone";
    public static String OWL_foaf_primaryTopic = "foaf:primaryTopic";
    public static String OWL_foaf_publications = "foaf:publications";
    public static String OWL_foaf_schoolHomepage = "foaf:schoolHomepage";
    public static String OWL_foaf_thumbnail = "foaf:thumbnail";
    public static String OWL_foaf_tipjar = "foaf:tipjar";
    public static String OWL_foaf_topic = "foaf:topic";
    public static String OWL_foaf_topic_interest = "foaf:topic_interest";
    public static String OWL_oaf_workInfoHomepage = "oaf:workInfoHomepage";
    public static String OWL_foaf_workplaceHomepage = "foaf:workplaceHomepage";

    public static void NcboObsOntology(){
        /*
		try {
			owlModel = ProtegeOWL.createJenaOWLModelFromURI(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int nbclasse=0;
		Collection classes = owlModel.getUserDefinedOWLNamedClasses();

		for (Iterator it = classes.iterator(); it.hasNext();) 
		{
		    OWLNamedClass cls = (OWLNamedClass) it.next();
		    Collection instances = cls.getInstances(false);
		    nbclasse++;
		    System.out.println("Class " + cls.getBrowserText() + " (" + instances.size() + ")");
		    for (Iterator jt = instances.iterator(); jt.hasNext();) 
		    {
		        OWLIndividual individual = (OWLIndividual) jt.next();
		        System.out.println(" - " + individual.getBrowserText());
		    }
		}
		Collection propertises = owlModel.getUserDefinedOWLProperties();
		for(Object a : propertises) // pour chaque String current dans a
		{
		  System.out.println(a);
		}
		    System.out.println("OWLProperties " + propertises.size());

		System.out.println(" nbclasse : " + nbclasse); */
        //CreateOWLIndividual();
        //CreateRDF();
        test();
        //queryWithAPI();
        //getOWLIndividual();
    }

    public static  void CreateOWLIndividual()
    {

        // creates the class individual
        OWLIndividual individual = owlModel.getOWLNamedClass(OWL_Annotation).createOWLIndividual("anno1");

        // assigns the individual properties
        individual.addPropertyValue(owlModel.getOWLObjectProperty(OWL_annotatesResource), "annotatesResource");
        individual.addPropertyValue(owlModel.getOWLObjectProperty(OWL_hasTopic), "hasTopic");
        System.out.println(individual.toString());

    }
    public static  void  CreateRDF(){
        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        RDFSNamedClass personClass = owlModel.createRDFSNamedClass("Person");
        RDFProperty ageProperty = owlModel.createRDFProperty("age");
        ageProperty.setRange(owlModel.getXSDint());
        ageProperty.setDomain(personClass);

        RDFIndividual individual = personClass.createRDFIndividual("Holger");
        individual.setPropertyValue(ageProperty, new Integer(33));

        Jena.dumpRDF(owlModel.getOntModel());
    }

    public static void test(){

        Model m = ModelFactory.createDefaultModel();
        String aofPrefix = "http://purl.org/ao/foaf/";
        String aoPrefix = "http://purl.org/ao/";
        String pavPrefix ="http://purl.org/pav/2.0/";
        String aosPrefix ="http://purl.org/ao/selectors/";
        String rdfPrefix ="http://purl.org/rdf/";
        //
        Resource root = m.createResource( "http://my.example.org/ann/21300" );
        Property annotatesDocument = m.createProperty( aofPrefix+"annotatesDocument" );
        Property context = m.createProperty( aoPrefix+"context" );
        Property hasTopic = m.createProperty( aoPrefix+"hasTopic" );
        Property createdBy = m.createProperty( pavPrefix+"createdBy" );
        Property createdOn = m.createProperty(pavPrefix+"createdOn");
        m.createProperty(rdfPrefix+"type");

        m.createResource( "&aot;ExactQualifier" );
        Resource annotatesDocumentResource = m.createResource( "http://tinyurl.com/ykjn87p" );
        Resource contextResource = m.createResource( "http://my.example.org/se/10300" );
        Resource hasTopicResource = m.createResource( "http://purl.obolibrary.org/obo/PRO_4615" );
        Resource createdByResource = m.createResource( "http://www.hcklab.org/foaf.rdf#me" );
        //
        Resource root2 = m.createResource( "http://my.example.org/sel/21300" );
        Property exact = m.createProperty( aosPrefix+"exact" );
        Property prefix = m.createProperty( aosPrefix+"prefix" );
        Property postfix = m.createProperty( aoPrefix+"postfix" );
        Property onSourceDocument = m.createProperty( aoPrefix+"onSourceDocument" );
        Resource onSourceDocumentResource = m.createResource( "http://my.example.org/sd/1339" );
        Property onDocument = m.createProperty( aofPrefix+"onDocument" );
        Resource onDocumentResource = m.createResource( "http://tinyurl.com/ykjn87p" );


        m.add(root2, onDocument, onDocumentResource).add(root2, onSourceDocument, onSourceDocumentResource).add(root2, postfix, "postfix").add(root2, prefix, "prefix").add(root2, exact, "BACE1").add( root, createdBy, createdByResource ).add(root, createdOn, "ccccccc").add( root, hasTopic, hasTopicResource ).add( root, context, contextResource ).add( root, annotatesDocument, annotatesDocumentResource );
        m.setNsPrefix( "aof", aofPrefix );
        m.setNsPrefix( "ao", aoPrefix );
        m.setNsPrefix( "pav", pavPrefix );
        m.setNsPrefix( "aos", aosPrefix );
        m.setNsPrefix( "rd", rdfPrefix );
        m.write( System.out );
        System.out.println(new java.util.Date().getTime());
    }

    public static  void test2()
    {
        String inputFile="F:\\MasterBCD\\Stage\\Code Clement\\exemple.rdf";
        Model model = ModelFactory.createDefaultModel();
        try{
            InputStream in =new  FileInputStream(inputFile);
            model.read(in," ");
            model.write(System.out);
        }catch(Exception e){}
    }


}
