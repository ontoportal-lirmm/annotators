echo "annotatorURI=http://localhost:8080/annotator?" > sifr-bioportal-annotator-proxy/src/main/resources/annotatorProxy.properties
mvn validate clean install
