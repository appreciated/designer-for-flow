package com.github.appreciated.designer.application.designer.remove;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class DesignerComponentRemoveEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(DesignerComponentWrapper draggedComponent) {
        applicationEventPublisher.publishEvent(new DesignerComponentRemovedEvent(this, draggedComponent));
    }
}