package org.sifrproject.annotations.output.json;


import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;

/**
 * Lazy JSON generator, relies on the toString method of the Lazy model implementation, to generate JSON directly
 * through the underlying Json object
 */
public class JSONLazyOutputGenerator implements OutputGenerator {

    @Override
    public AnnotatorOutput generate(Iterable<Annotation> annotations, String annotatorURI) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        boolean isFirst = true;
        for (Annotation annotation : annotations) {
            if (!isFirst) {
                result.append(",");
            } else {
                isFirst = false;
            }
            result.append(annotation.toString());
        }
        result.append("]");
        return new LIRMMAnnotatorOutput(result.toString(), "application/json");
    }
}
