package com.github.appreciated.designer.application.events.dnd;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class DesignerComponentDragEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(DesignerComponentWrapper draggedComponent, boolean hasStarted) {
        applicationEventPublisher.publishEvent(new DesignerComponentDragEvent(this, draggedComponent, hasStarted));
    }
}