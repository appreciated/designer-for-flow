package com.github.appreciated.designer.events.designer.focus;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;
import org.springframework.context.ApplicationEvent;

public class ElementFocusedEvent extends ApplicationEvent {

    private final DesignerComponentWrapper parent;
    private Component focus;

    public ElementFocusedEvent(Object source, Component focus, DesignerComponentWrapper parent) {
        super(source);
        this.focus = focus;
        this.parent = parent;
    }

    public ElementFocusedEvent(ElementFocusedEventPublisher elementFocusedEventPublisher, Component focus) {
        super(elementFocusedEventPublisher);
        this.focus = focus;
        parent = (DesignerComponentWrapper) focus.getParent().orElse(null);
    }

    public Component getFocus() {
        return focus;
    }

    public DesignerComponentWrapper getParent() {
        return parent;
    }
}
