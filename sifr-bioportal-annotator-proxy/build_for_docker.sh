echo "annotatorURI=http://localhost:8080/annotator?" > src/main/resources/annotatorProxy.properties
mvn validate 
mvn clean install
