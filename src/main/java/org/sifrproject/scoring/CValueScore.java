package org.sifrproject.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.sifrproject.util.JSON;

import Measure.C_Value.C_value;
import Object.CandidatTerm;

public class CValueScore extends Scorer {

    public CValueScore(JSON annotationArray){
        super(annotationArray);
    }
    
    @Override
    public Map<String, Double> compute() {
        // Compute old score
        Map<String, Double> oldScore = new OldScore(annotations).compute();

        // compute cvalue
        Map<String, Double> cvalues = computeAnnotationCValue();
        
        // compute cvalue score
        HashMap<String, Double> scores = new HashMap<>();
        for(String id : oldScore.keySet()){
            Double score = Math.log(oldScore.get(id));
            if(cvalues.containsKey(id)) score *= cvalues.get(id);
            scores.put(id, score);
        }
                
        return scores;
    }

    private Map<String, Double> computeAnnotationCValue(){
        // Retrieve all annotated terms
        ArrayList<String> terms = new ArrayList<>();
        for(Annotation annotation : annotations.values())
            for(Match match : annotation.getMatches())
                terms.add(match.term);
        
        // compute cvalues scores, for each term
        HashMap<String, Double> cvalues = new HashMap<>();
        for(CandidatTerm candidat : C_value.computePossibleTerms(terms)){
            String term = candidat.getTerm();
            if(cvalues.containsKey(term))
                cvalues.put(term, candidat.getImportance()+cvalues.get(term));
            else
                cvalues.put(term, candidat.getImportance());                
        }
        
        return addAnnotationCValue(cvalues);
    }
    protected Map<String, Double> addAnnotationCValue(HashMap<String, Double> cvalues){
        HashMap<String,Double> annotationCValues = new HashMap<>();
        for(String id : annotations.keySet()){
            Annotation annotation = annotations.get(id);
            Double annotationCValue = 0.0;
            
            // sum cvalues of all annotated terms
            for(Match match : annotation.getMatches()){
                String term = match.term;
                if(cvalues.containsKey(term))
                    annotationCValue += cvalues.get(term);
            }
            
            if(annotationCValue!=0)
                annotationCValues.put(id, annotationCValue);
        }
        
        return annotationCValues;
    }
}
