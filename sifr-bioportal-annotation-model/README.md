AnnotatorPlus and LIRMM Annotation Proxy
=============
 
Author: [Andon Tchechmedjiev](https://github.com/twktheainur/)


Development
-----------

This project is structured as a multimodule maven project and requires maven for development. It is recommended to 
use and IDE that includes built-in maven support. As of 2016, NetBeans can have some issues with complex maven projects, 
it is therefore advisable to use Eclipse or IntelliJ. 

*Module structure*

  - The root package is _org.sifrproject.annotations_. 
  - The _api_ package contains the interfaces that define the model API and its factories, while the other packages contain 
      implementations, matching the subpackage structure in the _api_ package. 
  - The _exceptions_ package implements custom exceptions for error handling. 
  - The _input_ package implements the default JSON parser for the NCBO BioPortal annotator.
  - The _model_ package contains the 
       

*Local dependencies*

Local dependencies are required and can be found in the [local-dep](local-dep) folder.
Before building this project, it should be installed in the local maven repository, 
which can be done by executing `maven_install`, from the same folder.

Note that, in eclipse, it is necessary to update the project:

  - in command line, execute `mvn eclipse:eclipse`
  - close and reopen your project
 
