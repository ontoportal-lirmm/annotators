package org.sifrproject.parameters;

import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.ContextPostAnnotator;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.UrlParameters;


public class ContextParameterHandler implements ParameterHandler {

    private final String language;
    public ContextParameterHandler(String language) {
        this.language = language;
    }

    @Override
    public void processParameter(UrlParameters parameters, PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException {
        boolean negation = Boolean.valueOf(parameters.getFirst("negation", "false"));
        boolean temporality = Boolean.valueOf(parameters.getFirst("temporality", "false"));
        boolean experiencer = Boolean.valueOf(parameters.getFirst("experiencer", "false"));

        if (negation || temporality || experiencer) {
            postAnnotationRegistry.registerPostAnnotator(new ContextPostAnnotator(language, negation, experiencer, temporality));
        }
    }
}
