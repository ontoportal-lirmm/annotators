package org.sifrproject.parameters;


import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.postannotation.api.PostAnnotator;
import org.sifrproject.postannotation.ScoringPostAnnotator;
import org.sifrproject.scoring.CValueScore;
import org.sifrproject.scoring.OldScore;
import org.sifrproject.scoring.api.Scorer;
import org.sifrproject.util.UrlParameters;

public class ScoreParameterHandler implements ParameterHandler {
    @Override
    public void processParameter(UrlParameters parameters, PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException {

        if (parameters.containsKey("score")) {
            String score = parameters.get("score")[0];
            parameters.remove("score");

            Scorer scorer;
            switch (score) {
                case "old":
                    scorer = new OldScore();
                    break;
                case "cvalue":
                    scorer = new CValueScore(true);
                    break;
                case "cvalueh":
                    scorer = new CValueScore(false);
                    break;
                default:
                    throw new InvalidParameterException(String.format("Invalid value for score parameter -- %s", score));
            }

            PostAnnotator scorePostAnnotator = new ScoringPostAnnotator(scorer);
            postAnnotationRegistry.registerPostAnnotator(scorePostAnnotator);
        }
    }
}
