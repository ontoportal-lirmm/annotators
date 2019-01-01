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
        final boolean negation = Boolean.valueOf(parameters.getFirst("negation", "false"));
        final boolean temporality = Boolean.valueOf(parameters.getFirst("temporality", "false"));
        final boolean experiencer = Boolean.valueOf(parameters.getFirst("experiencer", "false"));

        if (negation || temporality || experiencer) {
            postAnnotationRegistry.registerPostAnnotator(new ContextPostAnnotationFilter(language, negation, experiencer, temporality));
        }
    }
}
