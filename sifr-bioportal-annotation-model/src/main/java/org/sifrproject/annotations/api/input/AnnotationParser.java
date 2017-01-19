package org.sifrproject.annotations.api.input;

import org.json.simple.parser.ParseException;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.exceptions.NCBOAnnotatorErrorException;

import java.util.List;

public interface AnnotationParser {
    List<Annotation> parseAnnotations(String queryResponse) throws ParseException, NCBOAnnotatorErrorException;
}