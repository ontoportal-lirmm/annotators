package org.sifrproject.annotations.umls;

public interface UMLSGroupIndex extends Iterable<String> {
    UMLSGroup getGroupByName(String name);

    UMLSGroup getGroupByType(String name);
}
