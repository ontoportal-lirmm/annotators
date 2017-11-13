package org.sifrproject.annotations.api.output;


import org.sifrproject.annotations.api.model.Annotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract class implements all the operations for output generator dispatchers, the only responsibility of subclasses
 * is to register generators with their respective trigger terms in their constructors
 */
public class AbstractOutputGeneratorDispatcher implements OutputGeneratorDispatcher {

    private final Map<String, OutputGenerator> dispatcherAssociation = new HashMap<>();

    protected final void registerGenerator(final String generatorTrigger, final OutputGenerator outputGenerator) {
        dispatcherAssociation.put(generatorTrigger, outputGenerator);
    }

    private boolean isSupported(final String generatorTrigger) {
        return dispatcherAssociation.containsKey(generatorTrigger);
    }

    private OutputGenerator retrieveGenerator(final String generatorTrigger) {
        return dispatcherAssociation.get(generatorTrigger);
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public final AnnotatorOutput generate(final String generatorTrigger, final List<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final OutputGenerator outputGenerator = retrieveGenerator(generatorTrigger);
        return isSupported(generatorTrigger) ? outputGenerator.generate(annotations, annotatorURI, sourceText) : generate(annotations, annotatorURI, sourceText);
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public final AnnotatorOutput generate(final List<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final OutputGenerator json = retrieveGenerator("json");
        return json.generate(annotations, annotatorURI,sourceText);
    }

}
