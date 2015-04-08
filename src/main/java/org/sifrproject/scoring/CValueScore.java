package org.sifrproject.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.sifrproject.cvalue.CValueEvaluator;
import org.sifrproject.cvalue.CValueTerm;
import org.sifrproject.util.Debug;
import org.sifrproject.util.JSON;


public class CValueScore extends Scorer {
    final boolean propagate;

    /**
     * Make a {@link Scorer} that compute cvalue or cvalueH
     * 
     * @param propagated
     *      If true, term cvalue are also used (i.e. propagated) for concepts
     *         found in hierarchy and mappings, this is the default cvalue score.
     *      If false, this Scorer compute the cvalueH score
     */
    public CValueScore(JSON annotationArray, boolean propagate){
        super(annotationArray);
        this.propagate = propagate; // false => cvalueH
    }
    
    @Override
    public Map<String, Double> compute() {
        // Compute old score
        Map<String, Double> oldScore = new OldScore(annotations).compute();

        // compute cvalue
        Map<String, Double> termCValues = computeTermCValues();
        Map<String, Double> annoCValues = computeAnnotationCValues(termCValues);
        
        // compute cvalue score
        HashMap<String, Double> scores = new HashMap<>();
        for(String id : oldScore.keySet()){
            Double score = Math.log(oldScore.get(id));
            
            if(annoCValues.containsKey(id))
                score *= annoCValues.get(id);
            scores.put(id, score);
        }
                
        return scores;
    }

    /**
     * Compute cvalue of all terms annotated in all annotation concepts
     */
    private Map<String, Double> computeTermCValues(){
        // Retrieve all annotated terms
        ArrayList<String> terms = new ArrayList<>();
        for(Annotation annotation : annotations.values())
            for(Match match : annotation.getMatches())
                terms.add(match.term);
        
        // compute cvalues scores, for each term
        HashMap<String, Double> cvalues = new HashMap<>();
        for(CValueTerm candidat : new CValueEvaluator(terms).getTerms(true))
            addValue(cvalues,candidat.getTerm(), candidat.getCValue());
        
        // add to Debug
        for(String term : cvalues.keySet()){
            Debug.put("CVALUE(term="+term+")", cvalues.get(term));
            System.out.println("term="+term+" cvalue="+cvalues.get(term));
        }
        return cvalues;
    }
    
    /**
     * Compute cvalues of annotation concept using {@code cvalues} of terms
     */
    protected Map<String, Double> computeAnnotationCValues(Map<String, Double> cvalues){
        HashMap<String,Double> annotationCValues = new HashMap<>();

        for(String id : annotations.keySet()){
            Annotation annotation = annotations.get(id);
            Double annotationCValue = 0.0;
            
            // sum cvalues of all annotated terms
            for(Match match : annotation.getMatches()){
                String term = match.term.toLowerCase();    // todo: cvalue computation has lowered given term, is that all?
                if(cvalues.containsKey(term))
                    annotationCValue += cvalues.get(term);
            }
            
            if(annotationCValue!=0){
                addValue(annotationCValues,id, annotationCValue);

                if(propagate){
                    // propagate to all hierarchy
                    for(String hid: annotation.getHierarchy().keySet())
                        addValue(annotationCValues,hid, annotationCValue);
                    
                    // propagate to all mapping
                    for(String mid: annotation.getMappings())
                        addValue(annotationCValues,mid, annotationCValue);
                }
            }
        }

        return annotationCValues;
    }
    protected final void addValue(Map<String, Double> cvalues, String id, Double value){
        if(cvalues.containsKey(id)) cvalues.put(id, cvalues.get(id)+value);
        else                        cvalues.put(id, value);
    }
}
