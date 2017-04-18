package org.sifrproject.annotations.api.input;

import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.exceptions.InvalidFormatException;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;

import java.util.List;

/**
 * Parser for the custom json-ld format from NCBO Annotator
 */
public interface AnnotationParser {
    /**
     * Parse the json-ld annotations contained in queryResponse to generate a list of {@link Annotation} instances
     * @param queryResponse The String containing the json-ld returned by a query to the NCBO bioportal REST API
     * @return The list of {@link Annotation} instances that correspond to the json-ld source.
     * @throws ParseException The format of the supplied {@code queryResponse} is invalid.
     * @throws NCBOAnnotatorErrorException The supplied {@code queryResponse} contains an error message.
     */
    List<Annotation> parseAnnotations(String queryResponse) throws ParseException, NCBOAnnotatorErrorException, InvalidFormatException;
}
