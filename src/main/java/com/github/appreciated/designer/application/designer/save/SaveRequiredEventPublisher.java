package com.github.appreciated.designer.application.designer.save;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class SaveRequiredEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(com.vaadin.flow.component.Component component, boolean showNotification) {
        applicationEventPublisher.publishEvent(new SaveRequiredEvent(this, component, showNotification));
    }
}