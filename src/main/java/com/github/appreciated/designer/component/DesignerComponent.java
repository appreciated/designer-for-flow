package com.github.appreciated.designer.component;

import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;

public class DesignerComponent {
    String name;
    Component component;
    private Class<? extends Component> className;

    public DesignerComponent(String name, Class<? extends Component> className) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        this.name = name;
        this.className = className;
        className.getDeclaredConstructor().newInstance();
    }

    public String getTagName() {
        return "<" + getName() + ">";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Component getComponent() {
        return component;
    }

    public DesignerComponentWrapper generateComponent() {
        try {
            return new DesignerComponentWrapper(className.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public Class<? extends Component> getClassName() {
        return className;
    }
}


