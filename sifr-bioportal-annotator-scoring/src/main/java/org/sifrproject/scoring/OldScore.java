package org.sifrproject.scoring;

import org.sifrproject.annotations.api.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldScore extends AbstractScorer {

    public OldScore(List<Annotation> annotations) {
        super(annotations);
    }

    public Map<String, ScoreableElement> compute() {
        Map<String, ScoreableElement> scores = new HashMap<>();

        for (Annotation annotation : getAnnotations()) {
            double score = 0;

            // add score for all annotatedTerms to this annotation
            AnnotationTokens annotatedTokens = annotation.getAnnotations();
            int number = 0;
            for (AnnotationToken token : annotatedTokens) {
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
            Hierarchy hierarchy = annotation.getHierarchy();
            for (HierarchyElement hierarchyElement : hierarchy) {
                int distance = hierarchyElement.getDistance();

                double factor = 1;
                if (distance <= 12)
                    factor += 10 * Math.exp(-0.2 * distance);

                addScore(hierarchyElement, factor * number);
                scores.put(hierarchyElement.getAnnotatedClass().getId(), hierarchyElement);
            }

            // add score to mappings
            Mappings mappings = annotation.getMappings();
            for (Mapping mapping : mappings) {
                addScore(mapping, 7);
                scores.put(mapping.getAnnotatedClass().getId(), mapping);
            }
        }
        return scores;
    }

}
