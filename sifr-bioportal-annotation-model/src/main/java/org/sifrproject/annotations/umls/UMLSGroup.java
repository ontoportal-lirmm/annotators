package org.sifrproject.annotations.umls;

public interface UMLSGroup extends Iterable<String> {
    String getName();
    double getWeight();
    void setWeight(double weight);
}
