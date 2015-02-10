package org.sifrproject.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sifrproject.util.JSON;
import org.sifrproject.util.JSONType;

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
    
    public Annotation(JSON object){
        // keep reference to the JSONObject
        this.object = object;
        matches = new ArrayList<>();
        hierarchy = new HashMap<>();
        
        // extract id of this Annotation
        JSON annotatedClass = object.get("annotatedClass");
        id = annotatedClass.getString("@id");
        
        // extract list of matched terms
        JSON matchObject = object.get("annotations");
        if(matchObject!=null && matchObject.getType()==JSONType.ARRAY){
            for (JSON match : matchObject.arrayContent()){
                String type = match.getString("matchType");
                String term = match.getString("text");
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
        JSON simplifiedSet = new JSON(JSONType.ARRAY);
        for (JSON hierarchyElement : hierarchySet.arrayContent()) {
            
            if(hierarchyElement.getType()!=JSONType.OBJECT)
                continue;  // TODO: throw some exception
            
            JSON annotatedCls = hierarchyElement.get("annotatedClass");
            
            // add entry to hierarchy
            String hid  = annotatedCls.getString("@id");
            Long   dist = hierarchyElement.getLong("distance");
            hierarchy.put(hid, dist);
            
            // simplify (replace) hierarchy JSONObject
            JSON simplifiedElement = new JSON(JSONType.OBJECT);
            simplifiedElement.put("@id", hid);
            simplifiedElement.put("distance", String.valueOf(dist));
            simplifiedSet.add(simplifiedElement);
            
            // create and add an Annotation to given annotations
            annotations.put(hid,new Annotation(hierarchyElement));
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
