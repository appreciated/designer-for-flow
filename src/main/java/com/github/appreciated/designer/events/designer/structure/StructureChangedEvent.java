package com.github.appreciated.designer.events.designer.structure;

import com.vaadin.flow.component.Component;
import org.springframework.context.ApplicationEvent;

public class StructureChangedEvent extends ApplicationEvent {

    private Component component;

    public StructureChangedEvent(Object source, Component component) {
        super(source);
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
