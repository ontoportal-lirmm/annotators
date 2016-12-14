package org.sifrproject.annotations.output.json;


import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;

public class JSONOutputGenerator implements OutputGenerator {

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
