package org.sifrproject.scoring;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sifrproject.util.JSON;
import org.sifrproject.util.JSONType;


public abstract class Scorer {

    protected final Map<String,Annotation> annotations;

    protected JSON annotationsJSON;

    public Scorer(JSON annotationArray){
        annotationsJSON = annotationArray;
        annotations = new HashMap<>(annotationArray.size());
        for(JSON obj : annotationArray.arrayContent()){
            Annotation annotation = new Annotation(obj);
            annotations.put(annotation.getId(), annotation);
        }
    }
    
    protected Scorer(Map<String,Annotation> annotations){
        this.annotations = annotations;
    }


    abstract public Map<String,Double> compute();
    
    protected static void addScore(Map<String, Double> scores, String id, double value){
        Double score = scores.get(id);
        if (score==null) score  = value;
        else             score += value;
        scores.put(id, score);
    }

    /**
     * Create a JSON array with annotation items 
     *   a 'score' entry with respective score value is added to each annotatedClasses
     *   items are sorted by {@code scores}
     */
    public JSON getScoredAnnotations(Map<String, Double> scores){

        JSON sortedAnnotations = new JSON(JSONType.ARRAY);


        for(JSON annotation : annotationsJSON.arrayContent()){

            // score hierarchy
            JSON hierarchies = annotation.get("hierarchy");
            for(JSON hierarchy : hierarchies.arrayContent()){
                String hid = hierarchy.get("annotatedClass").getString("@id");
                putScore(hierarchy, scores, hid);
            }

            // score mapping
            JSON mappings = annotation.get("mappings");
            for(JSON mapping : mappings.arrayContent()){
                String mid = mapping.get("annotatedClass").getString("@id");
                putScore(mapping, scores, mid);
            }

            // score annotation object
            putScore(annotation, scores, annotation.get("annotatedClass").getString("@id").toString());
            sortedAnnotations.add(annotation);
        }


        return sortedAnnotations;
    }
    /**
     * Add "score" item in {@code entry} with value taken from item {@code id} in {@code scores}
     * if it is present. If not present, don't add "score" item
     */
    private void putScore(JSON entry, Map<String, Double> scores, String id){
        Double score = scores.get(id);
        if(score!=null)
            entry.put("score", score.toString());
    }
    
    public void printIds(String prefix){
        for(String key : annotations.keySet())
            System.out.println(prefix+": "+key);
    }
}