package org.sifrproject.annotations.umls;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * UMLS Group Index utility class allows to dereference UMLS semantic groups
 */
public final class UMLSGroupIndex implements Iterable<String> {

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

    @Override
    public Iterator<String> iterator() {
        return groupNameIndex.keySet().iterator();
    }
}
