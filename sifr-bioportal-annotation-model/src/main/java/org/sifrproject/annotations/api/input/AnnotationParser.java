package org.sifrproject.annotations.api.input;

import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.exceptions.InvalidFormatException;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    static Map<AnnotationToken, List<Annotation>> perTokenAnnotations(final List<Annotation> annotations){
        /*final List<MutableInt> annotationCounters = new ArrayList<>();
        final List<Boolean> doneList = new ArrayList<>();
        for(int i=0;i<annotations.size();i++){
            doneList.add(false);
            annotationCounters.add(new MutableInt(0));
        }
        int i=0;
        boolean cont = true;*/
        final Map<AnnotationToken, List<Annotation>> perTokenAnnotations = new LinkedHashMap<>();
       /* while (cont) {
            final Annotation annotation = annotations.get(i);
            final List<AnnotationToken> tokens = new ArrayList<>();
            annotation.getAnnotations().forEach(tokens::add);
            final int counter = annotationCounters.get(i).intValue();
            if(counter<tokens.size()){
                final AnnotationToken annotationToken = tokens.get(counter);
                if (annotationToken != null) {
                    if (!perTokenAnnotations.containsKey(annotationToken)) {
                        perTokenAnnotations.put(annotationToken, new ArrayList<>());
                    }
                    perTokenAnnotations.get(annotationToken).add(annotation);
                }
                annotationCounters.get(i).increment();
            } else {
                doneList.set(i,true);
            }
            if(i==annotations.size()){
                i=0;
            }
            cont = false;
            for (final boolean isDone: doneList){
                if(!isDone){
                    cont = true;
                    break;
                }
            }
        }*/
        for (final Annotation annotation : annotations) {
            for (final AnnotationToken annotationToken : annotation.getAnnotations()) {
                if (annotationToken != null) {
                    if (!perTokenAnnotations.containsKey(annotationToken)) {
                        perTokenAnnotations.put(annotationToken, new ArrayList<>());
                    }
                    perTokenAnnotations
                            .get(annotationToken)
                            .add(annotation);
                }
            }
        }
        return perTokenAnnotations;
    }
}
