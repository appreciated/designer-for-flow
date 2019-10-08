package com.github.appreciated.designer.events.designer.structure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class StructureChangedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(com.vaadin.flow.component.Component component) {
        applicationEventPublisher.publishEvent(new StructureChangedEvent(this, component));
    }
}