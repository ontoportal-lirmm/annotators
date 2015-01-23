package org.sifrproject.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sifrproject.util.JSON;

/**
 * Represent one annotation as process by annotators
 * 
 * @author Julien Diener
 */
public class Annotation {
    private JSON object;
    
    protected String id;
    protected ArrayList<Match> matches;
    protected HashMap<String, Long>   hierarchy;
    
    public Annotation(JSONObject object){
        // keep reference to the JSONObject
        this.object = new JSON(object);
        matches = new ArrayList<>();
        hierarchy = new HashMap<>();
        
        // extract id of this Annotation
        JSONObject annotatedClass = (JSONObject) object.get("annotatedClass");
        id = (String) annotatedClass.get("@id");
        
        // extract list of matched terms
        Object matchObject = object.get("annotations");
        if(matchObject!=null){
            for (Object obj : (JSONArray) matchObject){
                JSONObject match = (JSONObject) obj;
                String type = (String) match.get("matchType");
                String term = (String) match.get("text");
                matches.add(new Match(term, type));
            }
        }
    }
    /**
     * Extract annotation from the hierarchy component:
     *   - add an entry to this instance {@link hierarchy} field
     *   - Simplify the JSONObject keeping only @id and distance fields
     *   - add the extracted annotation to given {@code annotations}
     */
    public void extractHierarchy(Map<String,Annotation> annotations){
        // extract related (hierarchical) annotation
        
        JSON hierarchySet = object.get("hierarchy");
        JSON simplifiedSet = new JSON(new JSONArray());
        for (JSON hierarchyElement : hierarchySet.iterObject()) {
            
            if(!hierarchyElement.isObjectType())
                continue;  // TODO: throw some exception
            
            JSON annotatedCls = hierarchyElement.get("annotatedClass");
            
            // add entry to hierarchy
            String hid  = annotatedCls.get("@id",String.class);
            Long   dist = hierarchyElement.get("distance", Long.class);
            hierarchy.put(hid, dist);
            
            // simplify (replace) hierarchy JSONObject
            JSON simplifiedElement = new JSON(new JSONObject());
            simplifiedElement.put("@id", hid);
            simplifiedElement.put("distance", String.valueOf(dist));
            simplifiedSet.add(simplifiedElement);
            
            // create and add an Annotation to given annotations
            annotations.put(hid,new Annotation(hierarchyElement.getObject()));
        }
        object.put("hierarchy", simplifiedSet);
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
    
    
}
