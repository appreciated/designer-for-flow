package com.github.appreciated.designer.application.component;

import com.vaadin.flow.component.Component;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.util.*;
import java.util.stream.Collectors;

public class ComponentPropertyParser {
    private Map<String, CustomPropertyDescriptor> properties = new HashMap<>();

    public ComponentPropertyParser(Component bean) {
        try {
            MethodDescriptor[] scannedProperties = Introspector.getBeanInfo(bean.getClass(), Object.class).getMethodDescriptors();

            List<MethodDescriptor> readMethods = Arrays.stream(scannedProperties)
                    .filter(propertyDescriptor -> propertyDescriptor.getMethod().getName().startsWith("get"))
                    .collect(Collectors.toList());

            List<MethodDescriptor> writeMethods = Arrays.stream(scannedProperties)
                    .filter(propertyDescriptor -> propertyDescriptor.getMethod().getName().startsWith("set"))
                    .collect(Collectors.toList());

            properties = readMethods.stream()
                    .filter(readMethodDescriptor -> hasFittingCounterPartMethod(writeMethods, readMethodDescriptor))
                    .map(readMethodDescriptor -> new CustomPropertyDescriptor(readMethodDescriptor, getFittingCounterPartMethod(writeMethods, readMethodDescriptor)))
                    .collect(Collectors.toMap(CustomPropertyDescriptor::getName, descriptor -> descriptor));
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    boolean hasFittingCounterPartMethod(List<MethodDescriptor> writeMethods, MethodDescriptor readMethodDescriptor) {
        return getFittingCounterPartMethod(writeMethods, readMethodDescriptor) != null;
    }

    MethodDescriptor getFittingCounterPartMethod(List<MethodDescriptor> writeMethods, MethodDescriptor readMethodDescriptor) {
        Optional<MethodDescriptor> optional = writeMethods.stream()
                .filter(methodDescriptor1 -> readMethodDescriptor.getName().substring(3).equals(methodDescriptor1.getName().substring(3)))
                .findFirst();
        return optional.orElse(null);
    }

    public Map<String, CustomPropertyDescriptor> getProperties() {
        return properties;
    }
}
