package org.sifrproject.annotations.output.brat;

import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.ScoreableElement;
import org.sifrproject.annotations.api.model.context.ExperiencerContext;
import org.sifrproject.annotations.api.model.context.NegationContext;
import org.sifrproject.annotations.api.model.context.TemporalityContext;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;
import org.sifrproject.annotations.umls.UMLSGroup;

import java.util.*;

import static org.sifrproject.annotations.output.MimeTypes.APPLICATION_BRAT;

/**
 * Produces a BRAT output from a list of bioportal {@code {@link Annotation}} objects compatible with the
 * CLEF eHealth 2014-2016 Quaero Evaluation Corpus
 */
@SuppressWarnings({"HardcodedLineSeparator", "LawOfDemeter"})
public class BratOutputGenerator implements OutputGenerator {
    @Override
    public AnnotatorOutput generate(final Iterable<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final Map<AnnotationToken, List<Annotation>> perTokenAnnotations = AnnotationParser.perTokenAnnotations(annotations);
        @SuppressWarnings("LocalVariableOfConcreteClass") final BratCounter bratCounter = new BratCounter();
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Map.Entry<AnnotationToken, List<Annotation>> annotationTokenListEntry : perTokenAnnotations.entrySet()) {
            final List<Annotation> annotationsForToken = annotationTokenListEntry.getValue();
            annotationsForToken.sort(Comparator.comparingDouble(ScoreableElement::getScore));

            for (final Annotation annotation : annotationsForToken) {
                if (annotation != null) {
                    final AnnotatedClass annotatedClass = annotation.getAnnotatedClass();
                    final AnnotationToken token = annotationTokenListEntry.getKey();

                    final Set<UMLSGroup> semanticGroups = annotatedClass.getSemanticGroups();

                    final NegationContext negationContext = annotationTokenListEntry.getKey().getNegationContext();
                    final ExperiencerContext experiencerContext = annotationTokenListEntry.getKey().getExperiencerContext();
                    final TemporalityContext temporalityContext = annotationTokenListEntry.getKey().getTemporalityContext();

                    if (semanticGroups.isEmpty()) {
                        generateAnnotationForGroup(stringBuilder, sourceText, bratCounter,
                                annotatedClass, token, null, negationContext, experiencerContext,
                                temporalityContext);
                    } else {
                        for (final UMLSGroup group : semanticGroups) {
                            generateAnnotationForGroup(stringBuilder, sourceText, bratCounter,
                                    annotatedClass, token, group, negationContext, experiencerContext,
                                    temporalityContext);
                        }
                    }
                }
            }
        }

        return new LIRMMAnnotatorOutput(stringBuilder.toString().trim(), APPLICATION_BRAT);
    }

    @SuppressWarnings("MethodParameterOfConcreteClass")
    private int generateAnnotationForGroup(final StringBuilder stringBuilder, final String sourceText,
                                           final BratCounter bratCounter, final AnnotatedClass annotatedClass, final AnnotationToken token,
                                           final UMLSGroup umlsGroup, final NegationContext negationContext, final ExperiencerContext experiencerContext,
                                           final TemporalityContext temporalityContext) {
        final int tokenFrom = token.getFrom();
        final int tokenTo = token.getTo();

        final String surfaceForm = (sourceText.isEmpty()) ? token.getText().toLowerCase() : sourceText.substring(tokenFrom, tokenTo);
        final int tokenCount = bratCounter.getTokenBoundCounter();
        bratCounter.incrementTokenBoundCounter();
        final String typeAnnotation = (umlsGroup == null) ? "NonUMLS" : prepareEnumName(umlsGroup.name());
        stringBuilder.append(String.format("T%d\t%s %d %d\t%s", tokenCount, typeAnnotation,
                tokenFrom,
                tokenTo,
                surfaceForm
                )
        ).append("\n");

        stringBuilder.append(String.format("#%d\tAnnotatorNotes T%d\t%s", tokenCount, tokenCount, annotatedClass.getId())).append("\n");


        if (negationContext != null) {
            final String negationValue = "Negation-"+prepareEnumName(negationContext.name());
            if (negationValue.contains("Negated") || negationValue.contains("Possible")) {
                stringBuilder.append(String.format("A%d\t%s T%d\n", bratCounter.getAttributeCounter(), negationValue, tokenCount));
                bratCounter.incrementAttributeCounter();
            }
        }

        if (experiencerContext != null) {
            final String experiencerValue = "Experiencer-"+prepareEnumName(experiencerContext.name());
            if (experiencerValue.contains("Other")) {
                stringBuilder.append(String.format("A%d\t%s T%d\n", bratCounter.getAttributeCounter(), experiencerValue, tokenCount));
                bratCounter.incrementAttributeCounter();
            }
        }

        if (temporalityContext != null) {
            final String temporalityValue = "Temporality-"+prepareEnumName(temporalityContext.name());
            if (temporalityValue.contains("Historical") || temporalityValue.contains("Hypothetical")) {
                stringBuilder.append(String.format("A%d\t%s T%d\n", bratCounter.getAttributeCounter(), temporalityValue, tokenCount));
                bratCounter.incrementAttributeCounter();
            }
        }

        return 0;
    }

    private String prepareEnumName(final String name) {
        final String lowerCased = name.toLowerCase();
        return name.substring(0, 1) + lowerCased.substring(1);
    }

    @SuppressWarnings("All")
    private static final class BratCounter {
        private int tokenBoundCounter = 1;
        private int attributeCounter = 1;
        private int relationCounter = 1;
        private int eventCounter = 1;
        private int normalizationCounter = 1;

        void incrementTokenBoundCounter() {
            tokenBoundCounter++;
        }

        void incrementAttributeCounter() {
            attributeCounter++;
        }

        void incrementRelationCounter() {
            relationCounter++;
        }

        void incrementEventCounter() {
            eventCounter++;
        }

        void incrementNormalizationCounter() {
            normalizationCounter++;
        }

        int getTokenBoundCounter() {
            return tokenBoundCounter;
        }

        int getAttributeCounter() {
            return attributeCounter;
        }

        public int getEventCounter() {
            return eventCounter;
        }

        public int getNormalizationCounter() {
            return normalizationCounter;
        }

        public int getRelationCounter() {
            return relationCounter;
        }
    }

}
