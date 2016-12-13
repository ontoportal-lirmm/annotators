package org.sifrproject.parameters.handling;


import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.util.UrlParameters;

public interface ParameterProcessor {
    void registerParameterHandler(String name, ParameterHandler parameterHandler, boolean isOptiona);

    void processParameters(UrlParameters parameters) throws InvalidParameterException;
}
