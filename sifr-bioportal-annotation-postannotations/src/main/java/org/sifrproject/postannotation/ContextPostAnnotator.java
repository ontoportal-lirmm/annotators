package org.sifrproject.postannotation;


import org.context.implementation.ConText;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.AnnotationTokens;
import org.sifrproject.annotations.api.model.context.ExperiencerContext;
import org.sifrproject.annotations.api.model.context.NegationContext;
import org.sifrproject.annotations.api.model.context.TemporalityContext;
import org.sifrproject.postannotation.api.PostAnnotator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Adds negation, experiencer and temporality annotations with Context
 */
public class ContextPostAnnotator implements PostAnnotator {

    private static final Logger logger = LoggerFactory.getLogger(ContextPostAnnotator.class);

    private final boolean includeNegation;
    private final boolean includeExperiencer;
    private final boolean includeTemporality;
    private final String language;

    public ContextPostAnnotator(String language, boolean includeNegation, boolean includeExperiencer, boolean includeTemporality) {
        this.includeNegation = includeNegation;
        this.includeExperiencer = includeExperiencer;
        this.includeTemporality = includeTemporality;
        this.language = language;
    }

    private ConText instantiateContext() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String langCapitalized = language.substring(0, 1).toUpperCase() + language.substring(1).toLowerCase();
        String className = "org.context.implementation.ConText" + langCapitalized;
        Class<? extends ConText> builderClass = (Class<? extends ConText>) Class.forName(className);
        Constructor<? extends ConText> builderClassConstructor = builderClass.getConstructor();
        return builderClassConstructor.newInstance();
    }

    public void postAnnotate(List<Annotation> annotations, String text) {
        // Segment the text in sentences
        String[] regex = text.split("\\.");
        ArrayList<Sentence> sentences = new ArrayList<>();
        Sentence s;
        int i = 0;
        for (String r : regex) {
            s = new Sentence(r, i, i + r.length());
            sentences.add(s);
            i = i + r.length();
        }
        // Run CoNTeXT on each concept annotation
        ConText cxt = null;
        try {
            cxt = instantiateContext();

            for (Annotation annotation : annotations) {
                AnnotationTokens annotationTokens = annotation.getAnnotations();
                for (AnnotationToken annotationToken : annotationTokens) {
                    int indexFromConcept = annotationToken.getFrom();
                    int indexToConcept = annotationToken.getTo();
                    String concept = annotationToken.getText();
                    // Seek the sentence corresponding to the concept
                    i = 0;

                    while (i < sentences.size() && sentences.get(i).getIndexTo() < indexFromConcept) {
                        i++;
                    }

                    try {
                        List<String> results = cxt.applyContext(concept, sentences.get(i).getSentence());
                        if (includeNegation) {
                            annotationToken.setNegationContext(NegationContext.valueOf(results.get(2)));
                        }
                        if (includeTemporality) {
                            annotationToken.setTemporalityContext(TemporalityContext.valueOf(results.get(3)));
                        }
                        if (includeExperiencer) {
                            annotationToken.setExperiencerContext(ExperiencerContext.valueOf(results.get(4)));
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getLocalizedMessage());
                    }


                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error("Cannot instantiate ConText, make sure context.language is set to one of the " +
                    "supported languages (French,English) in the properties file of the annotator proxy...");
        }

    }

    private class Sentence {

        private String sentence;
        private int indexFrom;
        private int indexTo;

        Sentence(String s, int indFr, int indTo) {
            sentence = s;
            indexFrom = indFr;
            indexTo = indTo;
        }

        String getSentence() {
            return sentence;
        }

        int getIndexFrom() {
            return indexFrom;
        }

        @SuppressWarnings("unused")
        public int getIndexTo() {
            return indexTo;
        }
    }
}
