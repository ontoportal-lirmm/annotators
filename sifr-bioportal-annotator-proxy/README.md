AnnotatorPlus and LIRMM Annotation Proxy
=============


Initial Author: [Julien Diener](https://github.com/julien-diener) 

Project heads: [Clement Jonquet](https://github.com/jonquet) and [Pierre Larmande](https://github.com/pierrelarmande)

CValue Scoring [Vincent Emonet](https://github.com/vemonet/), COnTeXT integration [Amine Abdaoui](https://github.com/amineabdaoui)  

Architectural refactoring: [Andon Tchechmedjiev](https://github.com/twktheainur/)

Includes work from: Soumia Melzi and Emmanuel Castanier


Deployment
---------

* branch `master` to deploy to `/annotator`, for the LIRMM Bioportal Annotator Proxy with Bioportal running on the same server
* branch `ncbo_annotator` to deploy to `/ncbo_annotatorplus` to add post-annotation capabilities to the NCBO BioPortal Annotator

Development
-----------

*Project structure*
The root package is _org.sifrproject_. The servlet package contains the AnnotatorServlet that implements the Annotator Proxy itself.
The parameters package defines a parameter handling system, where ``ParameterHandler`` subclass instances can be registered in a ParameterRegistry. 
Each parameter handler subclass can handle one or more parameters, the registry allows to specify the set of parameters that 
will trigger the parameter handler based on the servlet query parameters. 

The proxy module uses the model, parser and output generators from the _sifr-bioportal-annotation-model_ module of this project.
The proxy module uses the _sifr-bioportal-annotation-postannotations_ 
 
 
*Testing deployment*

If you are developing new features in the proxy and wish to test them locally without having to deploy the full bioportal
infrastructure, you may run the servlet through the maven jetty plugin. 

You need to 
 first copy `annotatorProxy.properties.sample` from the root of the project to `src/main/resources/annotatorProxy.properties`.
 Then modify the annotatorURI property to point to the URL of the REST API of the NCBO annotator. For the the LIRMM annotator,
 the default value should suffice (annotatorURI=http://services.bioportal.lirmm.fr/annotator), for the NCBO bioportal, it should 
 be (annotatorURI=http://data.bioontology.org/annotator).
 
 Then you may run `mvn clean war jetty:run`. Note that you need to have done a `mvn clean install` on the parent maven project 
 before deploying the proxy.
