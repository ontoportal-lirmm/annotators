package org.sifrproject.scoring;

import org.sifrproject.annotations.api.model.ScoreableElement;
import org.sifrproject.scoring.api.Scorer;


abstract class AbstractScorer implements Scorer {

    AbstractScorer() {
    }


    final void addScore(ScoreableElement scoreableElement, double value) {
        double initialScore = scoreableElement.getScore();
        if (initialScore < 0) {
            scoreableElement.setScore(value);
        } else {
            scoreableElement.setScore(initialScore + value);
        }
    }
}