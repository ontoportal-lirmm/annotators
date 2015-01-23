package org.sifrproject.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Provide practical interface to JSONObject **AND** JSONArray
 * 
 * @author Julien Diener
 */
public class JSON{
    protected JSONObject object;
    protected JSONArray  array;
    
    public JSON(String json){
        parseString(json);
    }
    public JSON(InputStream stream){
        StringBuilder builder = new StringBuilder();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            while((line=reader.readLine()) != null) builder.append(line);
            parseString(builder.toString());
            
        } catch (IOException e) {
            makeError(e);
        }
    }
    /** Construct a {@link JSON} instance wrapping a JSONObject or JSONArray */
    public JSON(Object obj){
        if (obj instanceof JSONObject)
            object = (JSONObject) obj;
        else if(obj instanceof JSONArray)
            array = (JSONArray) obj;
        else if(obj==null)
            throw new IllegalArgumentException("JSON(Object) constructor is for JSONObject or JSONArray, not null");
        else
            throw new IllegalArgumentException("JSON(Object) constructor is for JSONObject or JSONArray, not:"+obj.getClass());
    }

    /** Make a standardize object-type {@link JSON} referencing the given error */
    public JSON(Exception e){
        makeError(e);
    }
    
    private void parseString(String json){
        object = null;
        array = null;
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            if (obj instanceof JSONObject) {
                object = (JSONObject) obj;
            } else {
                array = (JSONArray) obj;
            }
        }catch(ParseException pe){
            makeError(pe);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void makeError(Exception e){
        StackTraceElement[] exceptionStack = e.getStackTrace();
        ArrayList<String> stack = new ArrayList<>(exceptionStack.length);
        
        boolean lastOk = true;
        for(StackTraceElement s : exceptionStack){
            String line = s.toString();
            if(!line.startsWith("org.sifrproject")){
                if(!lastOk)
                    continue;
                else{
                    line="...";
                    lastOk = false;
                }
            }else
                lastOk = true;
            
            stack.add(line);
        }
        
        JSONObject error = new JSONObject();
        error.put("message",e.getMessage());
        error.put("stack", stack);
        
        object = new JSONObject();
        object.put("error", error);
    }
    
    // test JSON type
    // --------------
    public boolean isObjectType(){
        return object!=null;
    }
    public boolean isArrayType(){
        return array!=null;
    }
    private void assertObject(String methodName){
        if(!isObjectType()) 
            throw new ClassCastException("Method '"+methodName+"' cannot be applied on a JSON instance of array-type");
    }
    private void assertArray(String methodName){
        if(!isArrayType()) 
            throw new ClassCastException("Method '"+methodName+"' cannot be applied on a JSON instance of object-type");
    }
    
    // Accessors
    // ---------
    /** Return the JSONArray stored by this {@link JSON} instance */
    public JSONArray getArray(){
        assertArray("getArray");
        return array;
    }
    
    /** Return the JSONObject stored by this {@link JSON} instance */
    public JSONObject getObject(){
        assertObject("getObject");
        return object;
    }

    // accessors for object-type
    /**
     * Append the given item to an object-type {@link JSON} instance
     * Throw an error if this instance is an array-type JSON
     */
    @SuppressWarnings("unchecked")
    public void put(String key, String value){
        assertObject("put");
        object.put(key, value);
    }
    @SuppressWarnings("unchecked")
    public void put(String key, JSON value){
        assertObject("put");
        object.put(key, value.isObjectType() ? value.getObject() : value.getArray());
    }

    /** Retrieve element for given {@code key} of this object-type {@link JSON} instance */
    public JSON get(String key){
        assertObject("get");
        return new JSON(object.get(key));
    }

    /** Retrieve element for given {@code key}and cast it using {@code cls} (for object-type {@link JSON} only) */
    public <T> T get(String key, Class<T> cls){
        assertObject("get");
        return cls.cast(object.get(key));
    }

    
    // accessors for array-type
    @SuppressWarnings("unchecked")
    public void add(Object object){
        assertArray("add");
        array.add(object);
    }
    
    /** Return all JSON contained in this array-type {@link JSON} instance */
    public List<JSON> iterObject(){
        assertArray("iterObject");
        List<JSON> content = new ArrayList<>();
        for(Object obj : array)
            content.add(new JSON(obj));
        return content;
    }

    
    // conversion
    // ----------
    public String toString(){
        if(isObjectType()) return object.toJSONString();
        if(isArrayType())  return array.toJSONString();
        return "{}";
    }
}

