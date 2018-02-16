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
    public CValueScore(final boolean propagate) {
        super();
        this.propagate = propagate; // false => cvalueH
    }

    @Override
    public Map<String, ScoreableElement> compute(final List<Annotation> annotations) {
        // Compute old score
        final AbstractScorer oldScoreScorer = new OldScore();
        final Map<String, ScoreableElement> scoreableElementMap = oldScoreScorer.compute(annotations);

        // compute cvalue
        final Map<String, Double> termCValues = computeTermCValues(annotations);
        final Map<String, Double> annoCValues = computeAnnotationCValues(termCValues, annotations);

        // compute cvalue score
        final HashMap<String, Double> scores = new HashMap<>();
        final double log2 = Math.log(2);
        for (final String id : scoreableElementMap.keySet()) {
            Double score = Math.log(scoreableElementMap.get(id).getScore()) / log2;

            if (annoCValues.containsKey(id))
                score *= annoCValues.get(id);
            scoreableElementMap.get(id).setScore(score);
        }

        Collections.sort(annotations, new Comparator<Annotation>() {
            @Override
            public int compare(final Annotation o1, final Annotation o2) {
                return Double.compare(o1.getScore(),o2.getScore());
            }
        });
        return scoreableElementMap;
    }

    /**
     * Compute cvalue of all terms annotated in all annotation concepts
     */
    private Map<String, Double> computeTermCValues(final List<Annotation> annotations) {
        // Retrieve all annotated terms
        final ArrayList<String> terms = new ArrayList<>();
        for (final Annotation annotation : annotations)
            for (final AnnotationToken annotationToken : annotation.getAnnotations())
                terms.add(annotationToken.getText());

        // compute cvalues scores, for each term
        final HashMap<String, Double> cvalues = new HashMap<>();
        for (final CValueTerm candidat : new CValueEvaluator(terms).getTerms(true)) {
            addValue(cvalues, candidat.getTerm(), candidat.getCValue());
        }

        return cvalues;
    }

    /**
     * Compute cvalues of annotation concept using {@code cvalues} of terms
     */
    private Map<String, Double> computeAnnotationCValues(final Map<String, Double> cvalues, final List<Annotation> annotations) {
        final HashMap<String, Double> annotationCValues = new HashMap<>();

        for (final Annotation annotation : annotations) {
            Double annotationCValue = 0.0;

            final String id = annotation.getAnnotatedClass().getId();

            // sum cvalues of all annotated terms
            final HashSet<String> matchedTerm = new HashSet<>();
            for (final AnnotationToken annotationToken : annotation.getAnnotations())
                matchedTerm.add(CValueEvaluator.normalizeTerm(annotationToken.getText()));
            for (final String term : matchedTerm) {
                if (cvalues.containsKey(term)) {
                    annotationCValue += cvalues.get(term);
                }
            }


            if (annotationCValue != 0) {
                addValue(annotationCValues, id, annotationCValue);

                if (propagate) {
                    // propagate to all hierarchy
                    for (final HierarchyElement hierarchyElement : annotation.getHierarchy())
                        addValue(annotationCValues, hierarchyElement.getAnnotatedClass().getId(), annotationCValue);

                    // propagate to all mapping
                    for (final Mapping mapping : annotation.getMappings())
                        addValue(annotationCValues, mapping.getAnnotatedClass().getId(), annotationCValue);
                }
            }
        }

        return annotationCValues;
    }

    private void addValue(final Map<String, Double> cvalues, final String id, final Double value) {
        if (cvalues.containsKey(id)) cvalues.put(id, cvalues.get(id) + value);
        else cvalues.put(id, value);
    }
}
