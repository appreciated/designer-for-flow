package com.github.appreciated.designer.events.designer.focus;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class ElementFocusedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(com.vaadin.flow.component.Component root, DesignerComponentWrapper parent) {
        applicationEventPublisher.publishEvent(new ElementFocusedEvent(this, root, parent));
    }

    public void publish(com.vaadin.flow.component.Component component) {
        applicationEventPublisher.publishEvent(new ElementFocusedEvent(this, component));
    }
}