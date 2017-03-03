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

import java.util.*;

/**
 * Produces a BRAT output from a list of bioportal {@code {@link Annotation}} objects compatible with the
 * CLEF eHealth 2014-2016 Quaero Evaluation Corpus
 */
public class BratOutputGenerator implements OutputGenerator {
    @Override
    public AnnotatorOutput generate(Iterable<Annotation> annotations, String annotatorURI) {
        Map<AnnotationToken, List<Annotation>> perTokenAnnotations = new HashMap<>();
        for (Annotation annotation : annotations) {
            for (AnnotationToken annotationToken : annotation.getAnnotations()) {
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
        StringBuilder stringBuilder = new StringBuilder();
        for (AnnotationToken token : perTokenAnnotations.keySet()) {
            termCounter++;
            List<Annotation> annotationsForToken = perTokenAnnotations.get(token);
            Collections.sort(annotationsForToken, new Comparator<Annotation>() {
                @Override
                public int compare(Annotation o1, Annotation o2) {
                    return Double.compare(o1.getScore(),o2.getScore());
                }
            });
            Annotation annotation = null;
            if (!annotationsForToken.isEmpty()) {
                Annotation current = annotationsForToken.get(0);
                int currentIndex = 1;
                while (current.getAnnotatedClass().getCuis().isEmpty() && currentIndex < annotationsForToken.size()) {
                    current = annotationsForToken.get(currentIndex);
                    currentIndex++;

                }
                annotation = current;
            }

            if (annotation != null) {
                AnnotatedClass annotatedClass = annotation.getAnnotatedClass();
                stringBuilder.append(String.format("T%d\t%s %d %d\t%s", termCounter, annotatedClass.getId(),
                        token.getFrom(),
                        token.getTo(),
                        token.getText()
                                .toLowerCase())).append("\n");
                NegationContext negationContext = token.getNegationContext();
                ExperiencerContext experiencerContext = token.getExperiencerContext();
                TemporalityContext temporalityContext = token.getTemporalityContext();
                if(negationContext!=null){
                    stringBuilder.append(String.format("A%d\t%s T%d",attributeCounter,negationContext.name(),termCounter));
                    attributeCounter++;
                }

                if(experiencerContext!=null){
                    stringBuilder.append(String.format("A%d\t%s T%d",attributeCounter,experiencerContext.name(),termCounter));
                    attributeCounter++;
                }

                if(temporalityContext!=null){
                    stringBuilder.append(String.format("A%d\t%s T%d",attributeCounter,temporalityContext.name(),termCounter));
                    attributeCounter++;
                }
//                stringBuilder.append(String.format("#%d\tAnnotatorNotes T%d\t%s", termCounter, termCounter, buildCUILIst(annotatedClass.getCuis()))).append("\n");
            }

        }

        return new LIRMMAnnotatorOutput(stringBuilder.toString(), "application/brat");
    }

}
