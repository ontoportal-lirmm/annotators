package org.sifrproject.annotations.api.output;


import org.sifrproject.annotations.api.model.Annotation;

/**
 * Specification of an {@link OutputGenerator} that produces a String output from an annotation model
 */
public interface OutputGenerator {
    /**
     * Generates the output from the list of annotations as defined by the annotation model API
     *
     * @param annotations The list of annotations
     * @return The corresponding String output
     */
    String generate(Iterable<Annotation> annotations);
}
