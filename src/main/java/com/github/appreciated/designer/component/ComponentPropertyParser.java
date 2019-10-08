package com.github.appreciated.designer.component;

import com.vaadin.flow.component.Component;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ComponentPropertyParser {
    private Map<String, PropertyDescriptor> properties = new HashMap<>();

    public ComponentPropertyParser(Component bean) {
        try {
            PropertyDescriptor[] scannedProperties = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
            PropertyDescriptor[] baseProperties = Introspector.getBeanInfo(Component.class, Object.class).getPropertyDescriptors();
            properties = Arrays.stream(scannedProperties)
                    .filter(propertyDescriptor -> propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getReadMethod() != null)
                    .collect(Collectors.toMap(FeatureDescriptor::getName, propertyDescriptor -> propertyDescriptor));
            //Arrays.stream(baseProperties).forEach(propertyDescriptor -> properties.remove(propertyDescriptor.getName()));
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        System.out.println("");
    }

    public Map<String, PropertyDescriptor> getProperties() {
        return properties;
    }
}
