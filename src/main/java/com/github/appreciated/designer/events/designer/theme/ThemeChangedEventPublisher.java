package com.github.appreciated.designer.events.designer.theme;

import com.github.appreciated.designer.model.CssVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ThemeChangedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(CssVariable cssVariable) {
        applicationEventPublisher.publishEvent(new ThemeChangedEvent(this, cssVariable));
    }
}