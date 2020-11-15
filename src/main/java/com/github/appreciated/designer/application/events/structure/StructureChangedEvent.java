package com.github.appreciated.designer.application.events.structure;

import com.vaadin.flow.component.Component;
import org.springframework.context.ApplicationEvent;

public class StructureChangedEvent extends ApplicationEvent {

    private final Component component;
    private boolean isForceReload;

    public StructureChangedEvent(Object source, Component component, boolean isForceReload) {
        super(source);
        this.component = component;
        this.isForceReload = isForceReload;
    }

    public Component getComponent() {
        return component;
    }

    public boolean isForceReload() {
        return isForceReload;
    }
}
