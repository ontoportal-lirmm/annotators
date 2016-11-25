package org.sifrproject.scoring;

import org.sifrproject.util.JSON;
import org.sifrproject.util.JSONType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
     * Take the whole unchanged JSON annotations array and add a 'score' entry for each annotatedClass
     * Then the annotatedClass are sorted according to their {@code scores}
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
            putScore(annotation, scores, annotation.get("annotatedClass").getString("@id"));
            sortedAnnotations.add(annotation);
        }

        // Call the method to sort the JSON annotations according to their score
        sortedAnnotations = sortAnnotations(sortedAnnotations);

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

    /**
     * Sort the annotations JSON depending on the score of the main annotatedClass
     *
     */
    private JSON sortAnnotations(JSON unsortedJSON){
        ArrayList<JSON> sortedArray = new ArrayList<JSON>();
        int arrayIndex;
        boolean isAnnoAdded;
        double arrayScore;
        double annotationScore;

        for(JSON annotation : unsortedJSON.arrayContent()){
            if (sortedArray.isEmpty()){
                sortedArray.add(annotation);
            } else {
                arrayIndex = 0;
                isAnnoAdded = false;
                annotationScore = Double.parseDouble(annotation.getString("score"));
                for(JSON arrayAnno : sortedArray){
                    arrayScore = Double.parseDouble(arrayAnno.getString("score"));
                    if (annotationScore > arrayScore){
                        sortedArray.add(arrayIndex, annotation);
                        isAnnoAdded = true;
                        break;
                    }
                    arrayIndex += 1;
                }
                if (!isAnnoAdded) {
                    sortedArray.add(annotation);
                }
            }
        }

        JSON sortedAnnotations = new JSON(JSONType.ARRAY);
        for(JSON anno : sortedArray){
            sortedAnnotations.add(anno);
        }

        return sortedAnnotations;
    }
}