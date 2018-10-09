package org.sifrproject.annotations.output.brat;

import org.apache.commons.lang3.tuple.Pair;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.ScoreableElement;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;
import org.sifrproject.annotations.output.MimeTypes;
import org.sifrproject.annotations.umls.UMLSGroup;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Produces a BRAT output from a list of bioportal {@code {@link Annotation}} objects compatible with the
 * CLEF eHealth 2014-2016 Quaero Evaluation Corpus
 */
@SuppressWarnings({"LawOfDemeter", "HardcodedLineSeparator"})
public class EHealthQuaeroBratOutputGenerator implements OutputGenerator {

    private final boolean disambiguate;
    private final boolean selectSingleGroup;
    private final boolean ignoreAmbiguous;

    public EHealthQuaeroBratOutputGenerator(final boolean disambiguate, final boolean selectSingleGroup, final boolean ignoreAmbiguous) {
        this.disambiguate = disambiguate;
        this.selectSingleGroup = selectSingleGroup;
        this.ignoreAmbiguous = ignoreAmbiguous;
    }

    @SuppressWarnings({"FeatureEnvy", "OverlyComplexMethod", "MethodWithMoreThanThreeNegations", "OverlyLongMethod"})
    @Override
    public AnnotatorOutput generate(final List<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final Map<AnnotationToken, List<Annotation>> perTokenAnnotations = AnnotationParser.perTokenAnnotations(annotations);
        final List<QuaeroAnnotation> filteredAnnotations = new ArrayList<>();

        final StringBuilder stringBuilder = new StringBuilder();
        for (final Map.Entry<AnnotationToken, List<Annotation>> annotationTokenListEntry : perTokenAnnotations.entrySet()) {
            List<Annotation> annotationsForToken = annotationTokenListEntry.getValue();
            annotationsForToken.sort(Comparator.comparingDouble(ScoreableElement::getScore));

            if (disambiguate) {
                final List<Annotation> fullList = annotationTokenListEntry.getValue();
                if (!fullList.isEmpty()) {
                    final List<Annotation> disambiguatedList = new ArrayList<>();
                    disambiguatedList.add(fullList.get(0));
                    annotationsForToken = disambiguatedList;
                }
            }

            Annotation annotation = null;
            if (!annotationsForToken.isEmpty()) {
                Annotation current = annotationsForToken.get(0);
                int currentIndex = 1;
                while (current
                        .getAnnotatedClass()
                        .getCuis()
                        .isEmpty() && (currentIndex < annotationsForToken.size())) {
                    current = annotationsForToken.get(currentIndex);
                    currentIndex++;

                }
                annotation = current;
            }

            if (annotation != null) {
                @SuppressWarnings("LocalVariableOfConcreteClass") final QuaeroAnnotation quaeroAnnotation = new QuaeroAnnotation(annotationTokenListEntry
                        .getKey()
                        .getFrom(), annotationTokenListEntry
                        .getKey()
                        .getTo(), annotationTokenListEntry
                        .getKey()
                        .getText());
                quaeroAnnotation.addUMLSSemanticGroups(annotation
                        .getAnnotatedClass()
                        .getSemanticGroups());
                quaeroAnnotation.addCUIs(annotation
                        .getAnnotatedClass()
                        .getCuis());
                filteredAnnotations.add(quaeroAnnotation);
            }
        }

        filteredAnnotations.sort(Comparator.comparingInt(QuaeroAnnotation::getFrom));
        final List<QuaeroAnnotation> merged = mergeDuplicateTokenAnnotations(filteredAnnotations);
        merged.sort(Comparator.comparingInt(QuaeroAnnotation::getFrom));

        int termCounter = 0;
        for (final QuaeroAnnotation annotation : merged) {
            termCounter++;

            final String group = annotation.semanticGroupLabel();
            if (!group
                    .trim()
                    .isEmpty()) {
                if (!ignoreAmbiguous || (annotation.semanticGroups.size() == 1)) {
                    stringBuilder
                            .append(String.format("T%d\t%s %d %d\t%s", termCounter, group,
                                    annotation.getFrom(),
                                    annotation.getTo(),
                                    annotation
                                            .getText()
                                            .toLowerCase()))
                            .append("\n");
                    stringBuilder
                            .append(String.format("#%d\tAnnotatorNotes T%d\t%s", termCounter, termCounter, annotation.cuiLabel()))
                            .append("\n");
                }
            }
        }


        return new LIRMMAnnotatorOutput(stringBuilder.toString(), MimeTypes.APPLICATION_BRAT);
    }

    private List<QuaeroAnnotation> mergeDuplicateTokenAnnotations(final Collection<QuaeroAnnotation> annotations) {
        return annotations
                .stream()
                .collect(Collectors.groupingBy(QuaeroAnnotation::getSpan))
                .values()
                .stream()
                .map(this::collapseList)
                .collect(Collectors.toList());
    }

    @SuppressWarnings({"MethodReturnOfConcreteClass", "LocalVariableOfConcreteClass"})
    private QuaeroAnnotation collapseList(final List<QuaeroAnnotation> annotations) {
        final QuaeroAnnotation rootAnnotation = annotations.get(0);
        final QuaeroAnnotation result = new QuaeroAnnotation(rootAnnotation.getFrom(), rootAnnotation.getTo(), rootAnnotation.text);
        for (final QuaeroAnnotation quaeroAnnotation : annotations) {
            result.addCUIs(quaeroAnnotation.cuis);
            result.addSemanticGroups(quaeroAnnotation.semanticGroups);
        }
        return result;
    }

    private static class QuaeroAnnotation {
        final int from;
        int to;
        final String text;
        final Set<String> semanticGroups;
        final Set<String> cuis;

        QuaeroAnnotation(final int from, final int to, final String text) {
            this.from = from;
            this.to = to;
            this.text = text;
            semanticGroups = new TreeSet<>();
            cuis = new TreeSet<>();
        }

        void addUMLSSemanticGroups(final Collection<UMLSGroup> name) {
            semanticGroups.addAll(name
                    .stream()
                    .map(UMLSGroup::getName)
                    .collect(Collectors.toList()));
        }

        void addSemanticGroups(final Collection<String> name) {
            semanticGroups.addAll(name);
        }

        void addCUIs(final Collection<String> cuis) {
            this.cuis.addAll(cuis);
        }

        String semanticGroupLabel() {
            return semanticGroups
                    .stream()
                    .collect(Collectors.joining(","));
        }

        String cuiLabel() {
            return cuis
                    .stream()
                    .collect(Collectors.joining(" "));
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public String getText() {
            return text;
        }

        Pair<Integer, Integer> getSpan() {
            return Pair.of(from, to);
        }
    }
}
