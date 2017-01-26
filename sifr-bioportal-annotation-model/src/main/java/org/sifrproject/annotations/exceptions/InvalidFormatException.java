package org.sifrproject.annotations.exceptions;

/**
 * This exception is thrown when the format of the input Bioportal JSON-LD syntax is invalid
 */
public class InvalidFormatException extends Exception {
    public InvalidFormatException(String message) {
        super("Invalid format: " + message);
    }
}
