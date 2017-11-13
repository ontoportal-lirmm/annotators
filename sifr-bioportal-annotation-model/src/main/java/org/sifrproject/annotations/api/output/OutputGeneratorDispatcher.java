package org.sifrproject.annotations.api.output;

import org.sifrproject.annotations.api.model.Annotation;

import java.util.List;

/**
 * Dispatches the output generation to the right output generator depending on the generator trigger
 */
public interface OutputGeneratorDispatcher extends OutputGenerator {
    /**
     * Generate the output with the appropriate generator associated with the generatorTrigger.
     * @param generatorTrigger The generator trigger
     * @param annotations An interable collection of annotations
     * @param annotatorURI The annotator URI that will serve the content
     * @return The annotation output
     */
    AnnotatorOutput generate(String generatorTrigger, List<Annotation> annotations, String annotatorURI, String sourceText);

}
