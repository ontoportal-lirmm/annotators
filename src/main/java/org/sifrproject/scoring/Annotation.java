package org.sifrproject.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represent one annotation as process by annotators
 * 
 * @author Julien Diener
 */
public class Annotation {
    protected JSONObject object;
    
    protected String id;
    protected ArrayList<Match> matches;
    protected HashMap<String, Integer>   hierarchy;
    
    public Annotation(JSONObject object){
        // keep reference to the JSONObject
        this.object = object;
        
        // extract id of this Annotation
        JSONObject annotatedClass = (JSONObject) object.get("annotatedClass");
        id = (String) annotatedClass.get("@id");
        
        // extract list of matched terms
        matches = new ArrayList<>();
        for (Object obj : (JSONArray) object.get("annotations")){
            JSONObject match = (JSONObject) obj;
            String type = (String) match.get("matchType");
            String term = (String) match.get("text");
            matches.add(new Match(term, type));
        }
        
        // extract related (hierarchical) annotation
        JSONArray hierarchySet = (JSONArray) object.get("hierarchy");
        hierarchy = new HashMap<>();
        for (Object h : hierarchySet) {
            JSONObject unehierarchie = (JSONObject) h;
            annotatedClass = (JSONObject) unehierarchie.get("annotatedClass");
            
            String hid  = (String) annotatedClass.get("@id");
            String dist = (String) unehierarchie.get("distance");

            hierarchy.put(hid, Integer.parseInt(dist));
        }
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
    public JSONObject getObject() {
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
    public HashMap<String, Integer> getHierarchy() {
        return hierarchy;
    }
    
    
}
