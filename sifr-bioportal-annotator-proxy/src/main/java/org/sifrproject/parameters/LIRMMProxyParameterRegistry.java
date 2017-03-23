package org.sifrproject.parameters;


import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.parameters.api.ParameterRegistry;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RequestGenerator;

import java.util.*;

public class LIRMMProxyParameterRegistry implements ParameterRegistry {


    private final List<Parameters> parameterss;
    private final Map<Parameters, ParameterHandler> parameterHandlers;

    private PostAnnotationRegistry postAnnotationRegistry;

    public LIRMMProxyParameterRegistry() {
        parameterss = new ArrayList<>();
        parameterHandlers = new HashMap<>();
    }

    @Override
    public synchronized void registerParameterHandler(String name, ParameterHandler parameterHandler, boolean isOptional) {
        Parameters currentParameters = new Parameters(name, isOptional);
        parameterss.add(currentParameters);
        parameterHandlers.put(currentParameters, parameterHandler);
    }


    @Override
    public synchronized final void processParameters(RequestGenerator requestGenerator) throws InvalidParameterException {
        if (!requestGenerator.containsKey("text")) {
            throw new InvalidParameterException("Mandatory parameter 'text' missing");
        }

        for (Parameters parameter : this.parameterss) {
            if (!parameter.isOptional() && !requestGenerator.containsKey(parameter.getName())) {
                throw new InvalidParameterException(String.format("Mandatory parameter missing -- %s", parameter.getName()));
            } else if (parameter.atLeastOneContained(requestGenerator)) {
                parameterHandlers.get(parameter).processParameter(requestGenerator, postAnnotationRegistry);
            }
        }
    }

    @Override
    public synchronized void setPostAnnotationRegistry(PostAnnotationRegistry postAnnotationRegistry) {
        this.postAnnotationRegistry = postAnnotationRegistry;
    }

    private final class Parameters {
        private List<String> names;
        private boolean isOptional;

        Parameters(String names, boolean isOptional) {
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

        List<String> getNames() {
            return names;
        }

        boolean atLeastOneContained(RequestGenerator parameters) {
            boolean atLeastOne = false;
            for (String name : names) {
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
