package org.sifrproject.annotations.exceptions;

/**
 * This is a generic exception encapsulating error messages from an NCBO Bioportal api service
 */
public class NCBOAnnotatorErrorException extends Exception {
    public NCBOAnnotatorErrorException(String message) {
        super(message);
    }
}
