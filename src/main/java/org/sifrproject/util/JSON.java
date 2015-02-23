package org.sifrproject.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/*
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
*/
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

/**
 * Provide practical interface to JSONObject **AND** JSONArray
 * 
 * @author Julien Diener
 */
public class JSON{
    protected JsonObject object;
    protected JsonArray  array;
    
    public JsonArray getArray() {
		return array;
	}
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
    public JSON(JSONType type){
        switch(type){
        case OBJECT: object = new JsonObject();  break;
        case ARRAY:  array  = new JsonArray();   break;
        }
    }
    
    /** Construct a {@link JSON} instance wrapping a JSONObject or JSONArray */
    private JSON(Object obj){
        if (obj instanceof JsonObject)
            object = (JsonObject) obj;
        else if(obj instanceof JsonArray)
            array = (JsonArray) obj;
        else if(obj==null)
            throw new IllegalArgumentException("JSON(Object) constructor is for JSONObject or JSONArray, not null");
        else
            throw new IllegalArgumentException("JSON(Object) constructor is for JSONObject or JSONArray, not: "+obj.getClass());
    }

    /** Make a standardize object-type {@link JSON} referencing the given error */
    public JSON(Exception e){
        makeError(e);
    }
    
    private void parseString(String json){
        object = null;
        array = null;
        try{
            JsonValue value = JsonValue.readFrom(json);
            if (value instanceof JsonObject) object = (JsonObject) value;
            else                             array  = (JsonArray)  value;
        }catch(RuntimeException e){
            makeError(e);
        }
    }
    
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
        
        JsonObject error = new JsonObject();
        error.add("message",e.getMessage());
        error.add("stack", stack.toString());
        
        object = new JsonObject();
        object.add("error", error);
    }
    
    // test JSON type
    // --------------
    public JSONType getType(){
        return object!=null ? JSONType.OBJECT : JSONType.ARRAY;
    }
    
    private final boolean isObjectType(){
        return object!=null;
    }
    private final boolean isArrayType(){
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
    /** Return the JSONObject or JSONArray stored by this {@link JSON} instance */
    private JsonValue getInternal(){
        return isObjectType() ? object : array;
    }
    
    public int size(){
        if(isObjectType()) return object.size();
        else               return array.size();
    }

    // accessors for object-type
    /**
     * Append the given item to an object-type {@link JSON} instance
     * Throw an error if this instance is an array-type JSON
     */
    public void put(String key, String value){
        assertObject("put");
        object.add(key, value);
    }
    public void put(String key, JSON value){
        assertObject("put");
        object.add(key, value.getInternal());
    }

    /** Retrieve element for given {@code key} of this object-type {@link JSON} instance */
    public JSON get(String key){
        assertObject("get");
        return new JSON(object.get(key));
    }
    public boolean containsKey(String key){
        assertObject("containsKey");
        return object.names().contains(key);
    }

    /** Retrieve String for given {@code key} */
    public String getString(String key){
        return object.get(key).asString();
    }
    /** Retrieve Integer for given {@code key} */
    public Integer getInt(String key){
        return object.get(key).asInt();
    }
    /** Retrieve Double for given {@code key} */
    public Double getDouble(String key){
        return object.get(key).asDouble();
    }
    /** Retrieve Long for given {@code key} */
    public Long getLong(String key){
        return object.get(key).asLong();
    }

    
    // accessors for array-type
    public void add(JSON json){
        assertArray("add");
        array.add(json.getInternal());
    }
    
    /** Return all JSON contained in this array-type {@link JSON} instance */
    public List<JSON> arrayContent(){
        assertArray("iterObject");
        List<JSON> content = new ArrayList<>();
        for(Object obj : array)
            content.add(new JSON(obj));
        return content;
    }

    
    // conversion
    // ----------
    public String toString(){
        if(isObjectType()) return object.toString();
        if(isArrayType())  return array.toString();
        return "{}";
    }
}

