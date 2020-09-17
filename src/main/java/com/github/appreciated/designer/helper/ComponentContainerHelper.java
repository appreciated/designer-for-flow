package com.github.appreciated.designer.helper;

import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class ComponentContainerHelper {

    public static boolean isComponentContainer(Component component) {
        Class classInstance = component.getClass();
        for (Method method : classInstance.getMethods()) {
            if (method.getName().equals("add") || method.getName().equals("addContent")) {
                return true;
            }
        }
        return false;
    }

    public static void addComponent(Component parent, Component child) {
        Class classInstance = parent.getClass();
        for (Method method : classInstance.getMethods()) {
            if (method.getName().equals("add")) {
                try {
                    method.invoke(parent, new Object[]{new Component[]{child}});
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        throw new IllegalStateException("Could not find add Method for Class: " + parent.getClass().getSimpleName());
    }

    public static void removeChild(Component parent, Component child) {
        parent.getElement().removeChild(child.getElement());
    }

    public static boolean containsChild(Component parent, Component child) {
        return parent.getElement().indexOfChild(child.getElement()) != -1;
    }

    public static void addComponentAtIndex(Component parent, int index, Component child) {
        Objects.requireNonNull(parent, "Parent should not be null");
        Objects.requireNonNull(child, "Component should not be null");
        if (index < 0) {
            throw new IllegalArgumentException(
                    "Cannot add a component with a negative index");
        }
        // The case when the index is bigger than the children count is handled
        // inside the method below
        parent.getElement().insertChild(index, child.getElement());
    }

    public static int indexOf(Component parent, Component child) {
        if (child == null) {
            throw new IllegalArgumentException(
                    "The 'component' parameter cannot be null");
        }
        Iterator<Component> it = getChildren(parent).sequential()
                .iterator();
        int index = 0;
        while (it.hasNext()) {
            Component next = it.next();
            if (child.equals(next)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static Stream<Component> getChildren(Component parent) {
        return parent.getChildren();
    }

    public static void removeAll(Component component) {
        component.getElement().removeAllChildren();
    }
}
