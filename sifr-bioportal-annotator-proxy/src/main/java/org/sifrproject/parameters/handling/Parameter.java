package org.sifrproject.parameters.handling;

public final class Parameter {
    private String name;
    private boolean isOptional;

    public Parameter(String name, boolean isOptional) {
        this.name = name;
        this.isOptional = isOptional;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return isOptional;
    }
}
