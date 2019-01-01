package org.sifrproject.parameters.api;


import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RequestGenerator;

import java.io.IOException;

public interface ParameterRegistry {
    void registerParameterHandler(String name, ParameterHandler parameterHandler, boolean isOptional);

    void processParameters(RequestGenerator parameters) throws InvalidParameterException, IOException;

    void setPostAnnotationRegistry(PostAnnotationRegistry postAnnotationRegistry);
}
