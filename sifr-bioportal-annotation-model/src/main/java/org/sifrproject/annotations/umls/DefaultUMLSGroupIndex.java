package org.sifrproject.annotations.umls;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * UMLS Group Index utility class allows to dereference UMLS semantic groups
 */
public final class DefaultUMLSGroupIndex implements UMLSGroupIndex {

    private final Map<String, UMLSGroup> groupNameIndex;
    private final Map<String, UMLSGroup> typeNameIndex;

    DefaultUMLSGroupIndex() {
        groupNameIndex = new HashMap<>();
        typeNameIndex = new HashMap<>();
    }

    void addGroupByName(final UMLSGroup group) {
        groupNameIndex.put(group.getName(), group);
    }

    void addGroupByType(final String type, final UMLSGroup group) {
        typeNameIndex.put(type, group);
    }

    @Override
    public UMLSGroup getGroupByName(final String name) {
        return groupNameIndex.get(name);
    }

    @Override
    public UMLSGroup getGroupByType(final String name) {
        final String cannonicalName;
        if(name.contains("http")){
            final String[] components = name.split("/");
            cannonicalName = components[components.length-1];
        } else {
            cannonicalName = name;
        }
        return typeNameIndex.get(cannonicalName);
    }

    @Override
    public Iterator<String> iterator() {
        return groupNameIndex.keySet().iterator();
    }
}
