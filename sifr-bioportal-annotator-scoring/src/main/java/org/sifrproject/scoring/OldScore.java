package org.sifrproject.scoring;

import org.sifrproject.annotations.api.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldScore extends AbstractScorer {

    public OldScore() {
        super();
    }

    @Override
    public Map<String, ScoreableElement> compute(final List<Annotation> annotations) {
        final Map<String, ScoreableElement> scores = new HashMap<>();

        for (final Annotation annotation : annotations) {
            double score = 0;

            // add score for all annotatedTerms to this annotation
            final AnnotationTokens annotatedTokens = annotation.getAnnotations();
            int number = 0;
            for (final AnnotationToken token : annotatedTokens) {
                switch (token.getMatchType()) {
                    case "PREF":
                        score += 10;
                        break;
                    case "SYN":
                        score += 8;
                        break;
                }
                number++;
            }
            addScore(annotation, score);
            scores.put(annotation.getAnnotatedClass().getId(), annotation);


            // add score to hierarchical concepts
            final Hierarchy hierarchy = annotation.getHierarchy();
            for (final HierarchyElement hierarchyElement : hierarchy) {
                final int distance = hierarchyElement.getDistance();

                double factor = 1;
                if (distance <= 12)
                    factor += 10 * Math.exp(-0.2 * distance);

                addScore(hierarchyElement, factor * number);
                scores.put(hierarchyElement.getAnnotatedClass().getId(), hierarchyElement);
            }

            // add score to mappings
            final Mappings mappings = annotation.getMappings();
            for (final Mapping mapping : mappings) {
                addScore(mapping, 7);
                scores.put(mapping.getAnnotatedClass().getId(), mapping);
            }
        }
        return scores;
    }

}
