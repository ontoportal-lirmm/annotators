package org.sifrproject.annotations.api.input;

import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.exceptions.InvalidFormatException;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;
import org.sifrproject.annotations.input.TokenImpl;

import java.util.*;

/**
 * Parser for the custom json-ld format from NCBO Annotator
 */
@FunctionalInterface
public interface AnnotationParser {
    /**
     * Parse the json-ld annotations contained in queryResponse to generate a list of {@link Annotation} instances
     * @param queryResponse The String containing the json-ld returned by a query to the NCBO bioportal REST API
     * @return The list of {@link Annotation} instances that correspond to the json-ld source.
     * @throws ParseException The format of the supplied {@code queryResponse} is invalid.
     * @throws NCBOAnnotatorErrorException The supplied {@code queryResponse} contains an error message.
     */
    List<Annotation> parseAnnotations(String queryResponse) throws ParseException, NCBOAnnotatorErrorException, InvalidFormatException;

//    static Map<AnnotationToken, List<Annotation>> perTokenAnnotations(final Iterable<Annotation> annotations){
//        final Map<AnnotationToken, List<Annotation>> perTokenAnnotations = new HashMap<>();
//        for (final Annotation annotation : annotations) {
//            for (final AnnotationToken annotationToken : annotation.getAnnotations()) {
//                if (annotationToken != null) {
//                    if (!perTokenAnnotations.containsKey(annotationToken)) {
//                        perTokenAnnotations.put(annotationToken, new ArrayList<>());
//                    }
//                    perTokenAnnotations.get(annotationToken).add(annotation);
//                }
//            }
//        }
//        return perTokenAnnotations;
//    }

    static List<Token> perTokenAnnotations(final Iterable<Annotation> annotations){
        final Collection<Token> tokens = new HashSet<>();

        for (final Annotation annotation : annotations) {
            for (final AnnotationToken annotationToken : annotation.getAnnotations()) {
                if (annotationToken != null) {
                    final Token token = new TokenImpl(annotationToken);
                    tokens.add(token);
                    token.addAnnotation(annotation);
                }
            }
        }
        final List<Token> tokensList = new ArrayList<>(tokens);
        tokensList.sort(Comparable::compareTo);
        return tokensList;
    }


}
