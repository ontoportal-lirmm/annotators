package org.sifrproject.scoring;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sifrproject.util.JSON;

public class CValueHScore extends CValueScore {

    public CValueHScore(JSON annotationArray){
        super(annotationArray);
    }
    
    protected Map<String, Double> addAnnotationCValue(HashMap<String, Double> cvalues){
        HashMap<String,Double> annotationCValues = new HashMap<>();
        for(String id : annotations.keySet()){
            Annotation annotation = annotations.get(id);
            Double annotationCValue = 0.0;
            
            // retrieve term which appears in any of this annotation hierarchy annotations
            Set<String> hierarchyTerms = new HashSet<>();
            for(String hierarchyId : annotation.getHierarchy().keySet()){
                if(annotations.containsKey(hierarchyId)){
                    Annotation hierarchyAnnotation = annotations.get(hierarchyId);
                    for(Match match : hierarchyAnnotation.getMatches()){
                        hierarchyTerms.add(match.term);
                    }
                }
            }
            
            // sum cvalues of all annotated terms, if not in hierarchyTerms
            for(Match match : annotation.getMatches()){
                String term = match.term;
                if(cvalues.containsKey(term) && !hierarchyTerms.contains(term))
                    annotationCValue += cvalues.get(term);
            }
            if(annotationCValue!=0)
                annotationCValues.put(id, annotationCValue);
        }
        
        return annotationCValues;
    }

}
