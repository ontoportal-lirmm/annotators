package org.sifrproject.annotations.output.json.lazy;


import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.output.OutputGenerator;

/*TODO: Adapt this later*/
public class JSONLazyOutputGenerator implements OutputGenerator {

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
            result.append(annotation.toString());
        }
        result.append("]");
        return result.toString();
    }
}
