package org.sifrproject.annotations.output.json;


import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;
import org.sifrproject.annotations.output.MimeTypes;

import java.util.List;

/**
 * Lazy JSON generator, relies on the toString method of the Lazy model implementation, to generate JSON directly
 * through the underlying Json object
 */
public class JSONLazyOutputGenerator implements OutputGenerator {

    @Override
    public AnnotatorOutput generate(final List<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final StringBuilder result = new StringBuilder();
        result.append("[");
        boolean isFirst = true;
        for (final Annotation annotation : annotations) {
            if (isFirst) {
                isFirst = false;
            } else {
                result.append(",");
            }
            result.append(annotation);
        }
        result.append("]");
        return new LIRMMAnnotatorOutput(result.toString(), MimeTypes.APPLICATION_JSON);
    }
}
