package org.sifrproject.annotations.api.output;

import org.sifrproject.annotations.api.model.Annotation;

/**
 * Dispatches the generation
 */
public interface OutputGeneratorDispatcher extends OutputGenerator {
    AnnotatorOutput generate(String generatorTrigger, Iterable<Annotation> annotations, String annotatorURI);

}
