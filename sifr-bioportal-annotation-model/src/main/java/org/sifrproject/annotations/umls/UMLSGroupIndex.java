package org.sifrproject.annotations.umls;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class UMLSGroupIndex {
    private final static Logger logger = LoggerFactory.getLogger(UMLSGroupIndex.class);

    private Map<String, UMLSGroup> groupNameIndex;
    private Map<String, UMLSGroup> typeNameIndex;

    UMLSGroupIndex() {
        groupNameIndex = new HashMap<>();
        typeNameIndex = new HashMap<>();
    }

    void addGroupByName(UMLSGroup group) {
        groupNameIndex.put(group.name(), group);
    }

    void addGroupByType(String type, UMLSGroup group) {
        typeNameIndex.put(type, group);
    }

    public UMLSGroup getGroupByName(String name) {
        return groupNameIndex.get(name);
    }

    public UMLSGroup getGroupByType(String name) {
        return typeNameIndex.get(name);
    }
}
