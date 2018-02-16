package org.sifrproject.parameters;


import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.parameters.api.ParameterRegistry;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RequestGenerator;

import java.util.*;

public class LIRMMProxyParameterHandlerRegistry implements ParameterRegistry {


    private final List<Parameters> parameterss;
    private final Map<Parameters, ParameterHandler> parameterHandlers;

    private PostAnnotationRegistry postAnnotationRegistry;

    public LIRMMProxyParameterHandlerRegistry() {
        parameterss = new ArrayList<>();
        parameterHandlers = new HashMap<>();
    }

    @SuppressWarnings("LocalVariableOfConcreteClass")
    @Override
    public synchronized void registerParameterHandler(final String name, final ParameterHandler parameterHandler, final boolean isOptional) {
        final Parameters currentParameters = new Parameters(name, isOptional);
        parameterss.add(currentParameters);
        parameterHandlers.put(currentParameters, parameterHandler);
    }


    @Override
    public final synchronized void processParameters(final RequestGenerator parameters) throws InvalidParameterException {
        if (!parameters.containsKey("text")) {
            throw new InvalidParameterException("Mandatory parameter 'text' missing");
        }

        for (final Parameters parameter : parameterss) {
            if (!parameter.isOptional() && !parameters.containsKey(parameter.getName())) {
                throw new InvalidParameterException(String.format("Mandatory parameter missing -- %s", parameter.getName()));
            } else if (parameter.isAtLeastOneContained(parameters)) {
                parameterHandlers.get(parameter).processParameter(parameters, postAnnotationRegistry);
            }
        }
    }

    @Override
    public synchronized void setPostAnnotationRegistry(final PostAnnotationRegistry postAnnotationRegistry) {
        this.postAnnotationRegistry = postAnnotationRegistry;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    private static final class Parameters {
        private final List<String> names;
        private final boolean isOptional;

        Parameters(final String names, final boolean isOptional) {
            this.names = new ArrayList<>();
            if (names.contains("|")) {
                Collections.addAll(this.names, names.split("\\|"));
            } else {
                this.names.add(names);
            }
            this.isOptional = isOptional;
        }

        String getName() {
            return names.get(0);
        }

        boolean isAtLeastOneContained(final Map parameters) {
            boolean atLeastOne = false;
            for (final String name : names) {
                atLeastOne = parameters.containsKey(name);
                if (atLeastOne) break;
            }
            return atLeastOne;
        }

        boolean isOptional() {
            return isOptional;
        }
    }
}
