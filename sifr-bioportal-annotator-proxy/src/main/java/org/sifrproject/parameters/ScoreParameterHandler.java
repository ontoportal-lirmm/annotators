package org.sifrproject.parameters;


import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.postannotation.api.PostAnnotator;
import org.sifrproject.postannotation.ScoringPostAnnotator;
import org.sifrproject.scoring.CValueScore;
import org.sifrproject.scoring.OldScore;
import org.sifrproject.scoring.api.Scorer;
import org.sifrproject.util.RequestGenerator;

public class ScoreParameterHandler implements ParameterHandler {
    @Override
    public void processParameter(final RequestGenerator parameters, final PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException {

        if (parameters.containsKey("score")) {
            final String score = parameters.get("score");
            parameters.remove("score");

            final Scorer scorer;
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

            final PostAnnotator scorePostAnnotator;
            if(parameters.containsKey("threshold")){
                final String threshold = parameters.get("threshold");
                parameters.remove("threshold");
                scorePostAnnotator = new ScoringPostAnnotator(scorer, Double.valueOf(threshold));
            } else {
                scorePostAnnotator = new ScoringPostAnnotator(scorer);
            }


            postAnnotationRegistry.registerPostAnnotator(scorePostAnnotator);
        } else if(parameters.containsKey("threshold")){
            throw new InvalidParameterException("The threshold parameter requires the score parameter");
        }
    }
}
