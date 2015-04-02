package org.sifrproject.util;

public class Debug {
    
    public static JSON makeDebugAnnotations(JSON annotations){
        JSON debugOutput = new JSON(JSONType.ARRAY);
        
        for(JSON annotation : annotations.arrayContent()){
            String id = annotation.get("annotatedClass").getString("@id");
            
            String terms = "";
            for(JSON term : annotation.get("annotations").arrayContent())
                terms += term.getString("text")+"("+term.getString("matchType")+"),";
            
            JSON hierarchy = new JSON(JSONType.OBJECT);
            int i=0;
            for(JSON hierarchyClass : annotation.get("hierarchy").arrayContent()){
                hierarchy.put("h"+i, hierarchyClass.get("annotatedClass").getString("@id"));
                i++;
            }
            
            JSON mappings = new JSON(JSONType.OBJECT);
            i=0;
            for(JSON mappedClass : annotation.get("mappings").arrayContent()){
                mappings.put("h"+i, mappedClass.get("annotatedClass").getString("@id"));
                i++;
            }
            
            // add to output
            JSON debugAnnotation = new JSON(JSONType.OBJECT);
            debugAnnotation.put("annotatedClass", id);
            debugAnnotation.put("terms", terms);
            debugAnnotation.put("hierarchy", hierarchy);
            debugAnnotation.put("mappings",  mappings);
            
            if(annotation.containsKey("score"))
                debugAnnotation.put("score", annotation.getString("score"));
            
            debugOutput.add(debugAnnotation);
            
        }
        return debugOutput;
    }

}
