package org.sifrproject.annotations.output.json;


import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.output.OutputGenerator;

public class JSONOutputGenerator implements OutputGenerator {

    @Override
    public String generate(Iterable<Annotation> annotations) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        boolean isFirst = true;
        for (Annotation annotation : annotations) {
            if (!isFirst) {
                result.append(",");
            } else {
                isFirst = false;
            }
            annotation.accept(new JSONAnnotationVisitor(result));
        }
        result.append("]");
        return result.toString();
    }
}
