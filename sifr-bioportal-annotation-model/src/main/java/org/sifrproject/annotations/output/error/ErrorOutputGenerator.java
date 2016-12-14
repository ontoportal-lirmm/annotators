package org.sifrproject.annotations.output.error;


import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;


public class ErrorOutputGenerator implements OutputGenerator {

    @Override
    public AnnotatorOutput generate(Iterable<Annotation> annotations, String annotatorURI) {
        final StringBuilder stringBuilder = new StringBuilder();
        annotations.forEach(annotation -> stringBuilder.append(annotation.toString()));
        return new LIRMMAnnotatorOutput(stringBuilder.toString(), "application/json");
    }
}
