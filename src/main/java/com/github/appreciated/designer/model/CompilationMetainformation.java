package com.github.appreciated.designer.model;

import java.util.HashMap;
import java.util.Map;

public class CompilationMetainformation {
    Map<String, Object> propertyReplacement = new HashMap<>();

    public Object getPropertyReplacement(String property) {
        return propertyReplacement.get(property);
    }

    public boolean hasPropertyReplacement(String property) {
        return propertyReplacement.containsKey(property);
    }

    public void setPropertyReplacement(String property, Object replacement) {
        propertyReplacement.put(property, replacement);
    }

}
