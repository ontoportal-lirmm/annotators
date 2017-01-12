package org.sifrproject.scoring;

import org.sifrproject.annotations.api.model.*;
import org.sifrproject.scoring.cvalue.CValueEvaluator;
import org.sifrproject.scoring.cvalue.CValueTerm;

import java.util.*;


public class CValueScore extends AbstractScorer {
    private final boolean propagate;

    /**
     * Make a {@link AbstractScorer} that compute cvalue or cvalueH
     *
     * @param propagate   If true, term cvalue are also used (i.e. propagated) for concepts
     *                    found in hierarchy and mappings, this is the default cvalue score.
     *                    If false, this AbstractScorer compute the cvalueH score
     */
    public CValueScore(boolean propagate) {
        super();
        this.propagate = propagate; // false => cvalueH
    }

    @Override
    public Map<String, ScoreableElement> compute(List<Annotation> annotations) {
        // Compute old score
        AbstractScorer oldScoreScorer = new OldScore();
        Map<String, ScoreableElement> scoreableElementMap = oldScoreScorer.compute(annotations);

        // compute cvalue
        Map<String, Double> termCValues = computeTermCValues(annotations);
        Map<String, Double> annoCValues = computeAnnotationCValues(termCValues, annotations);

        // compute cvalue score
        HashMap<String, Double> scores = new HashMap<>();
        double log2 = Math.log(2);
        for (String id : scoreableElementMap.keySet()) {
            Double score = Math.log(scoreableElementMap.get(id).getScore()) / log2;

            if (annoCValues.containsKey(id))
                score *= annoCValues.get(id);
            scoreableElementMap.get(id).setScore(score);
        }

        Collections.sort(annotations, new Comparator<Annotation>() {
            @Override
            public int compare(Annotation o1, Annotation o2) {
                return Double.compare(o1.getScore(),o2.getScore());
            }
        });
        return scoreableElementMap;
    }

    /**
     * Compute cvalue of all terms annotated in all annotation concepts
     */
    private Map<String, Double> computeTermCValues(List<Annotation> annotations) {
        // Retrieve all annotated terms
        ArrayList<String> terms = new ArrayList<>();
        for (Annotation annotation : annotations)
            for (AnnotationToken annotationToken : annotation.getAnnotations())
                terms.add(annotationToken.getText());

        // compute cvalues scores, for each term
        HashMap<String, Double> cvalues = new HashMap<>();
        for (CValueTerm candidat : new CValueEvaluator(terms).getTerms(true)) {
            addValue(cvalues, candidat.getTerm(), candidat.getCValue());
        }

        return cvalues;
    }

    /**
     * Compute cvalues of annotation concept using {@code cvalues} of terms
     */
    private Map<String, Double> computeAnnotationCValues(Map<String, Double> cvalues, List<Annotation> annotations) {
        HashMap<String, Double> annotationCValues = new HashMap<>();

        for (Annotation annotation : annotations) {
            Double annotationCValue = 0.0;

            String id = annotation.getAnnotatedClass().getId();

            // sum cvalues of all annotated terms
            HashSet<String> matchedTerm = new HashSet<>();
            for (AnnotationToken annotationToken : annotation.getAnnotations())
                matchedTerm.add(CValueEvaluator.normalizeTerm(annotationToken.getText()));
            for (String term : matchedTerm) {
                if (cvalues.containsKey(term)) {
                    annotationCValue += cvalues.get(term);
                }
            }


            if (annotationCValue != 0) {
                addValue(annotationCValues, id, annotationCValue);

                if (propagate) {
                    // propagate to all hierarchy
                    for (HierarchyElement hierarchyElement : annotation.getHierarchy())
                        addValue(annotationCValues, hierarchyElement.getAnnotatedClass().getId(), annotationCValue);

                    // propagate to all mapping
                    for (Mapping mapping : annotation.getMappings())
                        addValue(annotationCValues, mapping.getAnnotatedClass().getId(), annotationCValue);
                }
            }
        }

        return annotationCValues;
    }

    private void addValue(Map<String, Double> cvalues, String id, Double value) {
        if (cvalues.containsKey(id)) cvalues.put(id, cvalues.get(id) + value);
        else cvalues.put(id, value);
    }
}
