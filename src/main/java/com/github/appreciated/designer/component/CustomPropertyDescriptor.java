package com.github.appreciated.designer.component;

import java.beans.MethodDescriptor;
import java.lang.reflect.Method;

public class CustomPropertyDescriptor {
    private final String name;
    private final Method readMethod;
    private final Method writeMethod;
    private final Class<?> propertyType;

    public CustomPropertyDescriptor(MethodDescriptor readMethod, MethodDescriptor writeMethod) {
        this.readMethod = readMethod.getMethod();
        this.writeMethod = writeMethod.getMethod();
        name = readMethod.getName().substring(3);
        propertyType = readMethod.getMethod().getReturnType();
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public String getName() {
        return name;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }
}
