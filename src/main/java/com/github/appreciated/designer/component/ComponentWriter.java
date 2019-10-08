package com.github.appreciated.designer.component;

import com.vaadin.flow.component.Component;

import java.io.File;

public class ComponentWriter {
    private Component component;

    public ComponentWriter(File designFile, Component component) {
        this.component = component;
        iterateComponent(component);
    }

    private void iterateComponent(Component component) {
        component.getChildren().forEach(component1 -> {

        });
    }

}
