package com.github.appreciated.designer.events.designer.dnd;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class DesignerComponentDragEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(com.vaadin.flow.component.Component draggedComponent, boolean hasStarted) {
        applicationEventPublisher.publishEvent(new DesignerComponentDragEvent(this, draggedComponent, hasStarted));
    }
}