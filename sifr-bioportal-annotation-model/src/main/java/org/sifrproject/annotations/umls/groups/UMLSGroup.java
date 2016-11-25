package org.sifrproject.annotations.umls.groups;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UMLSGroup {
    private String name;
    private List<String> types = new ArrayList<>();
    private String typeString = "";

    UMLSGroup(String name) {
        this.name = name;
    }

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

    public List<String> types() {
        return Collections.unmodifiableList(types);
    }
}
