package com.github.appreciated.designer.events.designer.structure;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class StructureChangedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(com.vaadin.flow.component.Component component) {
        applicationEventPublisher.publishEvent(new StructureChangedEvent(this, component, false));
    }

    public void publish(com.vaadin.flow.component.Component component, boolean forceReload) {
        applicationEventPublisher.publishEvent(new StructureChangedEvent(this, component, forceReload));
    }
}