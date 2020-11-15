package com.github.appreciated.designer.application.events.file;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class FileChangedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(com.vaadin.flow.component.Component root, DesignerComponentWrapper parent) {
        applicationEventPublisher.publishEvent(new FileChangedEvent(this, root, parent));
    }

    public void publish(com.vaadin.flow.component.Component component) {
        applicationEventPublisher.publishEvent(new FileChangedEvent(this, component));
    }
}