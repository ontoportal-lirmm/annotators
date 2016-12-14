package org.sifrproject.postannotation;

import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.postannotation.api.PostAnnotator;
import org.sifrproject.scoring.api.Scorer;

import java.util.List;

public class ScoringPostAnnotator implements PostAnnotator {
    private final Scorer scorer;

    public ScoringPostAnnotator(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public void postAnnotate(List<Annotation> annotations, String text) {
        scorer.compute(annotations);
    }
}
