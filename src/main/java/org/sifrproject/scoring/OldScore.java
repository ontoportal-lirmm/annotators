package org.sifrproject.scoring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sifrproject.util.JSON;

public class OldScore extends Scorer {

    public OldScore(JSON annotationArray){
        super(annotationArray);
    }
    
    public Map<String,Double> compute(){
        Map<String,Double> scores = new HashMap<>();

        for (Annotation annotation : annotations.values()){
            double score = 0;

            // add score for all annotatedTerms to this annotation
            List<Match> annotatedMatches = annotation.getMatches();
            for (Match match: annotatedMatches){
                switch(match.type){
                case PREF: score += 10;  break;
                case SYN:  score += 8;   break;
                }
            }
            addScore(scores, annotation.getId(), score);


            // add score to hierarchical concepts
            Map<String, Long> hierarchy = annotation.getHierarchy();
            for (String hid : hierarchy.keySet()){
                Long distance = hierarchy.get(hid);

                double factor = 1;
                if (distance <= 12) 
                    factor += 10 * Math.exp(-0.2 * distance);

                addScore(scores, hid, factor*annotatedMatches.size());
            }
            
            // add score to mappings
            Set<String> mappings = annotation.getMappings();
            for (String mid : mappings)
                addScore(scores, mid, 7);
        }
        return scores;
    }
    
}
