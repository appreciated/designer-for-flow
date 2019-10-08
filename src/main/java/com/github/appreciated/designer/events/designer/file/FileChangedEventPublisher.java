package com.github.appreciated.designer.events.designer.file;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
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