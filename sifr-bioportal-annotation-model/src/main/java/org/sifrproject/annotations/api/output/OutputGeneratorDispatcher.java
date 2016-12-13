package org.sifrproject.annotations.api.output;

import org.sifrproject.annotations.api.model.Annotation;

/**
 * Dispatches the generation
 */
public interface OutputGeneratorDispatcher extends OutputGenerator {
    String generate(String generatorTrigger, Iterable<Annotation> annotations);

    @Override
    String generate(Iterable<Annotation> annotations);
}
