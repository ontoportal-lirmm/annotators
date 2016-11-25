package org.sifrproject.scoring;

import org.sifrproject.util.JSON;
import org.sifrproject.util.JSONType;

import java.util.*;

/**
 * Represent one annotation as process by servlet
 * 
 * @author Julien Diener
 */
public class Annotation {
    private JSON object;
    
    protected String id;
    protected ArrayList<Match> matches;
    protected HashMap<String, Long>   hierarchy;
    protected HashSet<String> mappings;
    
    public Annotation(JSON object){
        // keep reference to the JSONObject
        this.object = object;
        matches = new ArrayList<>();
        hierarchy = new HashMap<>();
        mappings  = new HashSet<>();
        
        // extract id of this Annotation
        JSON annotatedClass = object.get("annotatedClass");
        id = annotatedClass.getString("@id");
        
        // extract list of matched terms
        if(object.containsKey("annotations")){
            JSON matchObject = object.get("annotations");
            if(matchObject.getType()==JSONType.ARRAY){
                for (JSON match : matchObject.arrayContent()){
                    String type = match.getString("matchType");
                    String term = match.getString("text");
                    matches.add(new Match(term, type));
                }
            }
        }
        
        /*
         * Extract annotation from the hierarchy component:
         *   - add an entry to this instance {@link hierarchy} field
         *   - Simplify the JSONObject keeping only @id and distance fields
         *   - add the extracted annotation to given {@code annotations}
         */
        for (JSON hierarchyElement : object.get("hierarchy").arrayContent()) {
            if(hierarchyElement.getType()!=JSONType.OBJECT)
                continue;  // TODO: throw some exception?
            JSON annotatedCls = hierarchyElement.get("annotatedClass");
            
            // add entry to hierarchy
            String hid  = annotatedCls.getString("@id");
            Long   dist = hierarchyElement.getLong("distance");
            hierarchy.put(hid, dist);
        }
        
        /*
         * Extract annotation from the mappings component:
         *   - add an entry to this instance {@link mappings} set
         */
        for (JSON mappingsElement : object.get("mappings").arrayContent()) {
            if(mappingsElement.getType()!=JSONType.OBJECT)
                continue;  // TODO: throw some exception?
            JSON annotatedCls = mappingsElement.get("annotatedClass");
            String mid  = annotatedCls.getString("@id");
            mappings.add(mid);
        }
    }
    
    /**
     * Add a 'score' entry with respective score value is added to all annotatedClasses 
     * of the JSON object of this {@link Annotation} and return it.
     */
    public JSON getScoredAnnotation(Map<String, Double> scores){
        // add score to annotatedClasses in hierarchy 
        for (JSON hierarchyElement : object.get("hierarchy").arrayContent()) {
            if(hierarchyElement.getType()!=JSONType.OBJECT)
                continue;  // TODO: throw some exception?
            JSON annotatedCls = hierarchyElement.get("annotatedClass");
            
            // add entry to hierarchy
            String hid  = annotatedCls.getString("@id");
            annotatedCls.put("score", scores.get(hid).toString());
        }
        
        // TODO: add score to annotatedClasses in mappings
        
        // score annotation object
        object.put("score", scores.get(id).toString());
        
        return object;
    }


    
    // Getter
    // ------
    /**
     * @return the id of this Annotation
     */
    public String getId(){
        return id;
    }
    
    /**
     * @return the raw JSONObject of this Annotation
     */
    public JSON getObject() {
        return object;
    }

    /**
     * @return the set of <annotated term, respective MatchType> of this Annotation
     */
    public List<Match> getMatches() {
        return matches;
    }

    /**
     * @return the set of <id-of-hierarchical-annotation, respective distance> for this Annotation
     */
    public HashMap<String, Long> getHierarchy() {
        return hierarchy;
    }

    public HashSet<String> getMappings() {
        return mappings;
    }
    
    
}
