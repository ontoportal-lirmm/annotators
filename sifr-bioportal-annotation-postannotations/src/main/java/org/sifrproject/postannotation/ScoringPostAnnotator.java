package org.sifrproject.postannotation;

import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.postannotation.api.PostAnnotator;
import org.sifrproject.postannotation.util.ValueScale;
import org.sifrproject.scoring.api.Scorer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Adds score annotations
 */
public class ScoringPostAnnotator implements PostAnnotator {
    private static final double PERCENT_MAX = 100d;
    private final Scorer scorer;
    private final double score_threshold;
    private final double confidence_threshold;


    public ScoringPostAnnotator(final Scorer scorer, final double score_threshold, final double confidence_threshold) {
        this.scorer = scorer;
        this.score_threshold = score_threshold;
        this.confidence_threshold = confidence_threshold;
    }

    @Override
    public void postAnnotate(final List<Annotation> annotations, final String text) {
        scorer.compute(annotations);
        if(score_threshold >0) {
            final Collection<Annotation> toRemove = new ArrayList<>();
            for(final Annotation annotation: annotations){
                if(annotation.getScore() < score_threshold){
                    toRemove.add(annotation);
                }
            }
            annotations.removeAll(toRemove);
        }
        if(confidence_threshold >0){
            sortByDescendingScore(annotations);
            final double max = annotations.get(0).getScore();
            final double min = annotations.get(annotations.size()-1).getScore();

            final Collection<Annotation> selected = selectBelowDensityThreshold(annotations, max,min);
            final Collection<Annotation> toRemove = new ArrayList<>(annotations);
            toRemove.removeAll(selected);
            annotations.removeAll(toRemove);
        }
    }

    private void sortByDescendingScore(final List<Annotation> annotations){
        annotations.sort((o1, o2) -> Double.compare(o2.getScore(),o1.getScore()));
    }

//    private void sortByAscendingScore(final List<Annotation> annotations){
//        annotations.sort(Comparator.comparingDouble(ScoreableElement::getScore));
//    }

    private Collection<Annotation> selectBelowDensityThreshold(final List<Annotation> annotations, final double maxScore, final double minScore){
        int currentAnnotationIndex = 0;
        double currentScore = annotations.get(currentAnnotationIndex).getScore();
        currentAnnotationIndex++;
        final Collection<Annotation> selected = new ArrayList<>();
        final double threshold = ValueScale.scaleValue(confidence_threshold,0,100,minScore,maxScore);
        while ((currentScore > threshold) && (currentAnnotationIndex < annotations.size())) {
            final Annotation currentAnnotation = annotations.get(currentAnnotationIndex);
            currentScore = currentAnnotation.getScore();
            selected.add(currentAnnotation);
            currentAnnotationIndex++;
        }
        return selected;
    }


}
