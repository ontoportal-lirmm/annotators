package org.sifrproject.annotations.umls;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data class representing and UMLS group
 */
public class UMLSGroup {
    private String name;
    private List<String> types = new ArrayList<>();
    private String typeString = "";

    UMLSGroup(String name) {
        this.name = name;
    }

    /**
     * Get the name of the semantic group
     * @return the name of the semantic group
     */
    public String name() {
        return name;
    }

    void addType(String type) {
        types.add(type);
        if (!typeString.isEmpty()) {
            typeString += ",";
        }
        typeString += type;
    }

    /**
     * Get the list of types associated to the semantic group
     * @return the list of types associated to the semantic group
     */
    public List<String> types() {
        return Collections.unmodifiableList(types);
    }
}
