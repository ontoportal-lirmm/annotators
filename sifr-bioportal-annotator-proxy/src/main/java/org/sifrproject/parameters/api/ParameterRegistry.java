package org.sifrproject.parameters.api;


import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.util.RequestGenerator;

public interface ParameterRegistry {
    void registerParameterHandler(String name, ParameterHandler parameterHandler, boolean isOptional);

    void processParameters(RequestGenerator parameters) throws InvalidParameterException;
}
