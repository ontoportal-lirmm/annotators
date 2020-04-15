package org.sifrproject.parameters;

import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.ContextPostAnnotationFilter;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RequestGenerator;

import java.io.IOException;


public class ContextParameterHandler implements ParameterHandler {

    private final String language;

    public ContextParameterHandler(final String language) {
        this.language = language;
    }

    @SuppressWarnings("FeatureEnvy")
    @Override
    public void processParameter(@SuppressWarnings("MethodParameterOfConcreteClass") final RequestGenerator parameters, final PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException, IOException {
        final boolean fastContext = Boolean.parseBoolean(parameters.getFirst("fast_context", "false"));
        final boolean negation = Boolean.parseBoolean(parameters.getFirst("negation", "false"));
        final boolean experiencer = Boolean.parseBoolean(parameters.getFirst("experiencer", "false"));
        final boolean temporality = Boolean.parseBoolean(parameters.getFirst("temporality", "false"));
        final boolean certainty = Boolean.parseBoolean(parameters.getFirst("certainty", "false"));
        if (fastContext || negation || experiencer || certainty || temporality) {
            postAnnotationRegistry.registerPostAnnotator(new ContextPostAnnotationFilter(language, fastContext, negation, experiencer, temporality, certainty));
        }
    }
}
