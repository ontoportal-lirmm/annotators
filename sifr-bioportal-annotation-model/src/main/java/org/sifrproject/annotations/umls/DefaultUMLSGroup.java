package org.sifrproject.annotations.umls;


import java.util.*;
import java.util.function.Consumer;

/**
 * Data class representing and UMLS group
 * Iterating over the UMLSGroup will give the semantic types it is composed of
 */
public class DefaultUMLSGroup implements UMLSGroup {
    private final String name;
    private final Collection<String> types = new ArrayList<>();
    private String typeString = "";
    private double weight;

    DefaultUMLSGroup(final String name) {
        this.name = name;
        weight = 0d;
    }

    /**
     * Get the getName of the semantic group
     * @return the getName of the semantic group
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(final double weight) {
        this.weight = weight;
    }

    void addType(final String type) {
        types.add(type);
        if (!typeString.isEmpty()) {
            typeString += ",";
        }
        typeString += type;
    }

    @Override
    public Iterator<String> iterator() {
        return types.iterator();
    }

    @Override
    public void forEach(final Consumer<? super String> action) {
        types.forEach(action);
    }

    @Override
    public Spliterator<String> spliterator() {
        return types.spliterator();
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUMLSGroup strings = (DefaultUMLSGroup) o;
        return Objects.equals(getName(), strings.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
