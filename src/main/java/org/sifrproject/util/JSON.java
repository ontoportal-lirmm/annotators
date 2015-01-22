package org.sifrproject.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
        make(json);
    }
    public JSON(InputStream stream){
        StringBuilder builder = new StringBuilder();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            while((line=reader.readLine()) != null) builder.append(line);
            make(builder.toString());
            
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
        else
            throw new ClassCastException("JSON(Object) constructor is for JSONObject or JSONArray only");
    }

    /** Make a standardize object-type {@link JSON} referencing the given error */
    public JSON(Exception e){
        makeError(e);
    }
    
    private void make(String json){
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
    
    // Accessors
    // ---------
    /**
     * Append the given item to an object-type {@link JSON} instance
     * Throw an error if this instance is an array-type JSON
     */
    @SuppressWarnings("unchecked")
    public void put(String key, String value){
        if(isObjectType()) 
            object.put(key, value);
        else
            throw new ClassCastException("Method 'put' cannot be applied on a JSON instance of array-type");
    }
    
    @SuppressWarnings("unchecked")
    public void add(Object object){
        if(isObjectType()) 
            throw new ClassCastException("Method 'add' cannot be applied on a JSON instance of object-type");
        else
            array.add(object);
    }
    
    /** Return the JSONArray stored by this {@link JSON} instance */
    public JSONArray getArray(){
        if(isArrayType()) 
            return array;
        else
            throw new ClassCastException("'getArray' method is not valid for JSON instance of object-type");
    }
    
    /** Return the JSONObject stored by this {@link JSON} instance */
    public JSONObject getObject(){
        if(isObjectType()) 
            return object;
        else
            throw new ClassCastException("'getObject' method is not valid for JSON instance of array-type");
    }
    
    /**
     * return a json string 
     */
    public String toString(){
        if(isObjectType()) return object.toJSONString();
        if(isArrayType())  return array.toJSONString();
        return "{}";
    }
}

