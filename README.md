AnnotatorPlus
=============


Author: [Julien Diener](https://github.com/julien-diener)

Project heads: [Clement Jonquet](https://github.com/jonquet) and [Pierre Larmande](https://github.com/pierrelarmande)

Include work of: Soumia Melzi and Emmanuel Castanier


Development
-----------

Maven package (for eclipse), dependencies are listed in [pom.xml](pom.xml)


  - org.sifrproject.annotators contains the annotator servlets which wrap additional 
    functionalities around several (external) annotation services 
  - org.sifrproject.scoring contains the code to score, and sort, annotations 
  - org.sifrproject.format contains additional output format, namely RBF


*Local dependencies*

Local dependencies are required and can be found in the [local-dep](local-dep) folder.
Before building this project, it should be installed in the local maven repository, 
which can be done by executing `maven_install`, from the same folder.

Note that, in eclipse, it is necessary to update the project:

  - in command line, execute `mvn eclipse:eclipse`
  - close and reopen your project
 