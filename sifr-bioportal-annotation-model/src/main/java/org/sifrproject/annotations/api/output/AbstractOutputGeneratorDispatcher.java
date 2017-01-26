package org.sifrproject.annotations.api.output;


import org.sifrproject.annotations.api.model.Annotation;

import java.util.HashMap;
import java.util.Map;

/**
 * This abstract class implements all the operations for output generator dispatchers, the only responsibility of subclasses
 * is to register generators with their respective trigger terms in their constructors
 */
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
    public final AnnotatorOutput generate(String generatorTrigger, Iterable<Annotation> annotations, String annotatorURI) {
        if (isSupported(generatorTrigger)) {
            return retrieveGenerator(generatorTrigger).generate(annotations, annotatorURI);
        } else {
            return generate(annotations, annotatorURI);
        }
    }

    @Override
    public final AnnotatorOutput generate(Iterable<Annotation> annotations, String annotatorURI) {
        return retrieveGenerator("json").generate(annotations, annotatorURI);
    }

}
