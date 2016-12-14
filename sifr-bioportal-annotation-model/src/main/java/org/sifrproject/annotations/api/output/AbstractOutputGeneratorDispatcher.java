package org.sifrproject.annotations.api.output;


import org.sifrproject.annotations.api.model.Annotation;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOutputGeneratorDispatcher implements OutputGeneratorDispatcher {

    private Map<String, OutputGenerator> dispatcherAssociation = new HashMap<>();

    protected final void registerGenerator(String generatorTrigger, OutputGenerator outputGenerator) {
        dispatcherAssociation.put(generatorTrigger, outputGenerator);
    }

    private boolean isSupported(String generatorTrigger) {
        return dispatcherAssociation.containsKey(generatorTrigger);
    }

    private OutputGenerator retrieveGenerator(String generatorTrigger) {
        return dispatcherAssociation.get(generatorTrigger);
    }

    @Override
    public AnnotatorOutput generate(String generatorTrigger, Iterable<Annotation> annotations, String annotatorURI) {
        if (isSupported(generatorTrigger)) {
            return retrieveGenerator(generatorTrigger).generate(annotations, annotatorURI);
        } else {
            return generate(annotations, annotatorURI);
        }
    }

    @Override
    public AnnotatorOutput generate(Iterable<Annotation> annotations, String annotatorURI) {
        return retrieveGenerator("json").generate(annotations, annotatorURI);
    }

}
