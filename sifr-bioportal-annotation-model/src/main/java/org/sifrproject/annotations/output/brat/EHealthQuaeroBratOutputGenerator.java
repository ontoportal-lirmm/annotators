package org.sifrproject.annotations.output.brat;

import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.input.Token;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.ScoreableElement;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;
import org.sifrproject.annotations.output.MimeTypes;
import org.sifrproject.annotations.umls.UMLSGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
    public AnnotatorOutput generate(final Iterable<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final List<Token> tokens = AnnotationParser.perTokenAnnotations(annotations);
        int termCounter = 0;
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Token token : tokens) {
            termCounter++;
            final List<Annotation> annotationsForToken;
            if (disambiguate) {
                final List<Annotation> fullList = new ArrayList<>(token.getAnnotations());
                if (fullList.isEmpty()) {
                    annotationsForToken = fullList;
                } else {
                    final List<Annotation> disambiguatedList = new ArrayList<>();
                    disambiguatedList.add(fullList.get(0));
                    annotationsForToken = disambiguatedList;
                }
            } else {
                annotationsForToken = new ArrayList<>(token.getAnnotations());
                annotationsForToken.sort(Comparator.comparingDouble(ScoreableElement::getScore));
            }

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
                final Set<UMLSGroup> semanticGroups = annotatedClass.getSemanticGroups();
                String group = "";
                group += (selectSingleGroup && !semanticGroups.isEmpty()) ? semanticGroups.iterator().next().name() : buildGroupList(semanticGroups);

                if (!semanticGroups.isEmpty()) {
                    if (!ignoreAmbiguous || (semanticGroups.size() == 1)) {
                        stringBuilder.append(String.format("T%d\t%s %d %d\t%s", termCounter, group,
                                token.getAnnotationToken().getFrom(),
                                token.getAnnotationToken().getTo(),
                                token.getAnnotationToken().getText()
                                        .toLowerCase())).append("\n");
                        stringBuilder.append(String.format("#%d\tAnnotatorNotes T%d\t%s", termCounter, termCounter, buildCUILIst(annotatedClass.getCuis()))).append("\n");
                    }
                }
            }
        }

        return new LIRMMAnnotatorOutput(stringBuilder.toString(), MimeTypes.APPLICATION_BRAT);
    }

    private String buildCUILIst(final Iterable<String> set) {
        final StringBuilder stringBuilder = new StringBuilder("");
        boolean first = true;
        for (final String elem : set) {
            if (!first) {
                stringBuilder.append(",");
            }
            stringBuilder.append(elem);
            first = false;
        }
        return stringBuilder.toString();
    }

    private String buildGroupList(final Iterable<UMLSGroup> set) {
        final StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (final UMLSGroup elem : set) {
            if (!first) {
                stringBuilder.append(",");
            }
            stringBuilder.append(elem.name());
            first = false;
        }
        return stringBuilder.toString();
    }
}
