package org.sifrproject.postannotation;


import org.context.implementation.ConText;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.AnnotationTokens;
import org.sifrproject.annotations.api.model.context.ExperiencerContext;
import org.sifrproject.annotations.api.model.context.NegationContext;
import org.sifrproject.annotations.api.model.context.TemporalityContext;
import org.sifrproject.postannotation.api.PostAnnotationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Adds negation, experiencer and temporality annotations with Context
 */
public class ContextPostAnnotationFilter implements PostAnnotationFilter {

    private static final Logger logger = LoggerFactory.getLogger(ContextPostAnnotationFilter.class);

    private final boolean includeNegation;
    private final boolean includeExperiencer;
    private final boolean includeTemporality;
    private final String language;

    public ContextPostAnnotationFilter(String language, boolean includeNegation, boolean includeExperiencer, boolean includeTemporality) {
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

    public void postAnnotate(final List<Annotation> annotations, final String text) {
        // Segment the text in sentences
        final String[] regex = text.split("\\.");
        final List<Sentence> sentences = new ArrayList<>();

        int i = 0;
        for (String r : regex) {

            sentences.add(new Sentence(r, i, i + r.length()));
            i += r.length();
        }
        // Run CoNTeXT on each concept annotation

        try {
            final ConText cxt =instantiateContext();

            for (final Annotation annotation : annotations) {
                final AnnotationTokens annotationTokens = annotation.getAnnotations();
                for (final AnnotationToken annotationToken : annotationTokens) {
                    final int indexFromConcept = annotationToken.getFrom();
                    final int indexToConcept = annotationToken.getTo();
                    final String concept = annotationToken.getText();
                    // Seek the sentence corresponding to the concept
                    i = 0;

                    while ((i < sentences.size()) && (sentences
                            .get(i)
                            .getIndexTo() < indexFromConcept)) {
                        i++;
                    }

                    try {
                        final List<String> results = cxt.applyContext(concept, sentences.get(i).getSentence());
                        if (includeNegation) {
                            annotationToken.setNegationContext(NegationContext.valueOf(results.get(2).toUpperCase()));
                        }
                        if (includeTemporality) {
                            annotationToken.setTemporalityContext(TemporalityContext.valueOf(results.get(3).toUpperCase()));
                        }
                        if (includeExperiencer) {
                            annotationToken.setExperiencerContext(ExperiencerContext.valueOf(results.get(4).toUpperCase()));
                        }
                    } catch (final Exception ex) {
                        logger.error(ex.getLocalizedMessage());
                    }


                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error("Cannot instantiate ConText, make sure context.language is set to one of the supported languages (French,English) in the properties file of the annotator proxy... :{}", e.getLocalizedMessage());
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
