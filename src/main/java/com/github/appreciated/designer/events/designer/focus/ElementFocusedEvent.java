package com.github.appreciated.designer.events.designer.focus;

import com.vaadin.flow.component.Component;
import org.springframework.context.ApplicationEvent;

public class ElementFocusedEvent extends ApplicationEvent {

    private final Component focus;

    public ElementFocusedEvent(ElementFocusedEventPublisher elementFocusedEventPublisher, Component focus) {
        super(elementFocusedEventPublisher);
        this.focus = focus;
    }

    public Component getFocus() {
        return focus;
    }

}
