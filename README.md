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

This project is structured as a multimodule maven project and requires maven for development. It is recommended to 
use and IDE that includes built-in maven support. As of 2016, NetBeans can have some issues with complex maven projects, 
it is therefore advisable to use Eclipse or IntelliJ. 

*Project structure*

The modules of the project are the following:
  - **sifr-bioportal-annotation-model**: This is the core module of the project. It implements a custom (non-standard) JSON-LD datamodel matching that of NCBO bioportal and adds additional annotation
    capabilities matching additional post-processing annotations implemented in the proxy, as well as input/output capabilities
     from and to various formats. 
  
       

*Local dependencies*

Some local dependencies are required and can be found in the [local-dep](local-dep) folder.
The dependencies will be installed automatically upon performing `mvn clean install` for the first time.

Note that eclipse does not automatically update the eclipse project upon changes in the pom.xml files, you may need to update the project manually:
  - in the command line, execute `mvn eclipse:eclipse`
  - close and reopen your project
 
*Testing deployment*