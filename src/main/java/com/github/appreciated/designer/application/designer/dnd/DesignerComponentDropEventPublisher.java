package com.github.appreciated.designer.application.designer.dnd;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class DesignerComponentDropEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(com.vaadin.flow.component.Component draggedComponent, com.vaadin.flow.component.Component targetComponent) {
        applicationEventPublisher.publishEvent(new DesignerComponentDropEvent(this, draggedComponent, targetComponent));
    }
}