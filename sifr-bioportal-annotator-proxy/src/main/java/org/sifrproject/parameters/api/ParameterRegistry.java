package org.sifrproject.parameters.api;


import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.util.UrlParameters;

public interface ParameterRegistry {
    void registerParameterHandler(String name, ParameterHandler parameterHandler, boolean isOptional);

    void processParameters(UrlParameters parameters) throws InvalidParameterException;
}
