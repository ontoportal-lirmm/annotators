package org.sifrproject.parameters;


import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RequestGenerator;

public class FormatParameterHandler implements ParameterHandler {
    @Override
    public void processParameter(RequestGenerator parameters, PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException {
        if (!parameters.containsKey("score")) {
            parameters.put("score", "cvalue");
        }
    }
}
