package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer;

import com.vaadin.flow.component.Component;

public class RenderPair {
    String propertyName;
    Component propertyComponent;

    public RenderPair(String propertyName, Component propertyComponent) {
        this.propertyName = propertyName;
        this.propertyComponent = propertyComponent;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Component getPropertyComponent() {
        return propertyComponent;
    }

    public void setPropertyComponent(Component propertyComponent) {
        this.propertyComponent = propertyComponent;
    }
}
