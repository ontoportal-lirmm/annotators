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
    protected JSONObject map;
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

    /**
     * Make a standardize {@cite JsonObject} referencing the given error
     */
    public JSON(Exception e){
        map = new JSONObject();
        makeError(e);
    }
    
    private void make(String json){
        map = null;
        array = null;
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            if (obj instanceof JSONObject) {
                map = (JSONObject) obj;
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
        for(StackTraceElement s : exceptionStack)
            stack.add(s.toString());
        
        JSONObject error = new JSONObject();
        error.put("message",e.getMessage());
        error.put("stack", stack);
        
        map = new JSONObject();
        map.put("error", error);
    }
    
    public boolean isObjectType(){
        return map!=null;
    }
    public boolean isArrayType(){
        return array!=null;
    }
    
    /**
     * Append the given item to this {@cite JSON} instance:
     *   if it is a dictionary type: add key,value item
     *   if is it an array type: add a JSONObject entry with given key,value item
     */
    @SuppressWarnings("unchecked")
    public void put(String key, String value){
        if(isObjectType()) map.put(key, value);
        else{
            // throw new ClassClastException("Use 'put' method on a JSON instance of array type");
            JSONObject item = new JSONObject();
            item.put(key,value);
            array.add(item);
        }
    }
    
    public String toString(){
        if(isObjectType()) return map.toJSONString();
        if(isArrayType())  return array.toJSONString();
        return "{}";
    }
}

