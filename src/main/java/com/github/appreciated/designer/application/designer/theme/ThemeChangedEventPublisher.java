package com.github.appreciated.designer.application.designer.theme;

import com.github.appreciated.designer.model.CssVariable;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class ThemeChangedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(CssVariable cssVariable) {
        applicationEventPublisher.publishEvent(new ThemeChangedEvent(this, cssVariable));
    }
}