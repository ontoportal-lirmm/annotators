package org.sifrproject.annotations.exceptions;

public class NCBOAnnotatorErrorException extends Exception {
    public NCBOAnnotatorErrorException(String message) {
        super("Bioportal Annotator error: " + message);
    }
}
