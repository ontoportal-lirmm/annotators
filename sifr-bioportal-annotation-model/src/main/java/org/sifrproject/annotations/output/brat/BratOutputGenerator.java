package org.sifrproject.annotations.output.brat;

import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
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
        final Map<AnnotationToken, List<Annotation>> perTokenAnnotations = new HashMap<>();
        for (final Annotation annotation : annotations) {
            for (final AnnotationToken annotationToken : annotation.getAnnotations()) {
                if (annotationToken != null) {
                    if (!perTokenAnnotations.containsKey(annotationToken)) {
                        perTokenAnnotations.put(annotationToken, new ArrayList<Annotation>());
                    }
                    perTokenAnnotations.get(annotationToken).add(annotation);
                }
            }
        }
        int termCounter = 0;
        int attributeCounter = 1;
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Map.Entry<AnnotationToken, List<Annotation>> annotationTokenListEntry : perTokenAnnotations.entrySet()) {
            termCounter++;
            final List<Annotation> annotationsForToken = annotationTokenListEntry.getValue();
            Collections.sort(annotationsForToken, new Comparator<Annotation>() {
                @Override
                public int compare(Annotation o1, Annotation o2) {
                    return Double.compare(o1.getScore(), o2.getScore());
                }
            });
            Annotation annotation = null;
            if (!annotationsForToken.isEmpty()) {
                Annotation current = annotationsForToken.get(0);
                int currentIndex = 1;
                while (current.getAnnotatedClass().getCuis().isEmpty() && (currentIndex < annotationsForToken.size())) {
                    current = annotationsForToken.get(currentIndex);
                    currentIndex++;

                }
                annotation = current;
            }

            if (annotation != null) {
                final AnnotatedClass annotatedClass = annotation.getAnnotatedClass();
                final AnnotationToken token = annotationTokenListEntry.getKey();

                final Set<UMLSGroup> semanticGroups = annotatedClass.getSemanticGroups();

                final int tokenFrom = token.getFrom();
                final int tokenTo = token.getTo();
                final String surfaceForm = (sourceText.isEmpty())?token.getText().toLowerCase():sourceText.substring(tokenFrom, tokenTo);
                for(final UMLSGroup group: semanticGroups) {
                    stringBuilder.append(String.format("T%d\t%s %d %d\t%s", termCounter, prepareEnumName(group.name()),
                            tokenFrom,
                            tokenTo,
                            surfaceForm
                            )
                    ).append("\n");

                    stringBuilder.append(String.format("#%d\tAnnotatorNotes T%d\t%s", termCounter, termCounter, annotatedClass.getId())).append("\n");


                    final NegationContext negationContext = annotationTokenListEntry.getKey().getNegationContext();
                    final ExperiencerContext experiencerContext = annotationTokenListEntry.getKey().getExperiencerContext();
                    final TemporalityContext temporalityContext = annotationTokenListEntry.getKey().getTemporalityContext();
                    if (negationContext != null) {
                        final String negationValue = prepareEnumName(negationContext.name());
                        if (negationValue.equals("Negated") || negationValue.equals("Possible")) {
                            stringBuilder.append(String.format("A%d\t%s T%d\n", attributeCounter, negationValue, termCounter));
                            attributeCounter++;
                        }
                    }

                    if (experiencerContext != null) {
                        final String experiencerValue = prepareEnumName(experiencerContext.name());
                        if (experiencerValue.equals("Other")) {
                            stringBuilder.append(String.format("A%d\t%s T%d\n", attributeCounter, experiencerValue, termCounter));
                            attributeCounter++;
                        }
                    }

                    if (temporalityContext != null) {
                        final String temporalityValue = prepareEnumName(temporalityContext.name());
                        if (temporalityValue.equals("Historical") || temporalityValue.equals("Hypothetical")) {
                            stringBuilder.append(String.format("A%d\t%s T%d\n", attributeCounter, temporalityValue, termCounter));
                            attributeCounter++;
                        }
                    }
                }
            }

        }

        return new LIRMMAnnotatorOutput(stringBuilder.toString(), APPLICATION_BRAT);
    }

    private String prepareEnumName(final String name) {
        final String lowerCased = name.toLowerCase();
        return name.substring(0, 1) + lowerCased.substring(1);
    }

}
