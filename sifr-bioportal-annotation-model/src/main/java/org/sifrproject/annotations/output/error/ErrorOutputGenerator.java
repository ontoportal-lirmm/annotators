package org.sifrproject.annotations.output.error;


import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;
import org.sifrproject.annotations.output.MimeTypes;


/**
 * Generates an error output in JSON in the same way as Bioportal Annotator
 */
public class ErrorOutputGenerator implements OutputGenerator {

    @Override
    public AnnotatorOutput generate(final Iterable<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final StringBuilder stringBuilder = new StringBuilder();
        for(final Annotation annotation: annotations) {
          stringBuilder.append(annotation);
        }
        return new LIRMMAnnotatorOutput(stringBuilder.toString(), MimeTypes.APPLICATION_JSON);
    }
}
