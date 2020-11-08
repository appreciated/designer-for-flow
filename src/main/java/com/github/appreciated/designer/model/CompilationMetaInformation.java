package com.github.appreciated.designer.model;

import java.util.HashMap;
import java.util.Map;

public class CompilationMetaInformation {
    boolean isProjectComponent;

    String className;

    Map<String, Object> propertyReplacement = new HashMap<>();

    private String packageDeclaration;

    String variableName;

    public Object getPropertyReplacement(String property) {
        return propertyReplacement.get(property);
    }

    public boolean hasPropertyReplacement(String property) {
        return propertyReplacement.containsKey(property) && propertyReplacement.get(property) != null;
    }

    public void setPropertyReplacement(String property, Object replacement) {
        propertyReplacement.put(property, replacement);
    }

    public boolean isProjectComponent() {
        return isProjectComponent;
    }

    public void setProjectComponent(boolean projectComponent) {
        isProjectComponent = projectComponent;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setPackage(String packageDeclaration) {
        this.packageDeclaration = packageDeclaration;
    }

    public String getPackageDeclaration() {
        return packageDeclaration;
    }

    public boolean hasVariableName() {
        return variableName != null;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void removePropertyReplacement(String key) {
        propertyReplacement.remove(key);
    }
}
