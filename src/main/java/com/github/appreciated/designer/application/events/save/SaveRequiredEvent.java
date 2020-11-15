package com.github.appreciated.designer.application.events.save;

import com.vaadin.flow.component.Component;
import org.springframework.context.ApplicationEvent;

public class SaveRequiredEvent extends ApplicationEvent {

    private final Component component;
    private boolean showNotification;

    public SaveRequiredEvent(Object source, Component component, boolean showNotification) {
        super(source);
        this.component = component;
        this.showNotification = showNotification;
    }

    public Component getComponent() {
        return component;
    }

    public boolean isShowNotification() {
        return showNotification;
    }
}
