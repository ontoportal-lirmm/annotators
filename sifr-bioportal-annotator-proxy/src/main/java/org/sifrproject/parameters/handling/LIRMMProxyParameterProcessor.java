package org.sifrproject.parameters.handling;


import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.parameters.handling.Parameter;
import org.sifrproject.parameters.handling.ParameterHandler;
import org.sifrproject.parameters.handling.ParameterProcessor;
import org.sifrproject.postannotation.PostAnnotationRegistry;
import org.sifrproject.util.UrlParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LIRMMProxyParameterProcessor implements ParameterProcessor {


    private final List<Parameter> parameters;
    private final Map<Parameter, ParameterHandler> parameterHandlers;

    private PostAnnotationRegistry postAnnotationRegistry;

    public LIRMMProxyParameterProcessor(PostAnnotationRegistry postAnnotationRegistry) {
        parameters = new ArrayList<>();
        parameterHandlers = new HashMap<>();
        this.postAnnotationRegistry = postAnnotationRegistry;
    }

    @Override
    public void registerParameterHandler(String name, ParameterHandler parameterHandler, boolean isOptional) {
        Parameter currentParameter = new Parameter(name, isOptional);
        parameters.add(currentParameter);
        parameterHandlers.put(currentParameter, parameterHandler);
    }


    @Override
    public final void processParameters(UrlParameters parameters) throws InvalidParameterException {
        if (!parameters.containsKey("text")) {
            throw new InvalidParameterException("Mandatory parameter 'text' missing");
        }

        for (Parameter parameter : this.parameters) {
            if (!parameter.isOptional() && !parameters.containsKey(parameter.getName())) {
                throw new InvalidParameterException(String.format("Mandatory parameter missing -- %s", parameter.getName()));
            } else if (parameters.containsKey(parameter.getName())) {
                parameterHandlers.get(parameter).processParameter(parameters, postAnnotationRegistry);
            }
        }
    }
}
