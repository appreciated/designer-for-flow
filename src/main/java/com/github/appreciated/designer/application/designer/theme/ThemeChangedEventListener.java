package com.github.appreciated.designer.application.designer.theme;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
@UIScope
public class ThemeChangedEventListener implements ApplicationListener<ThemeChangedEvent> {
    private final List<Consumer<ThemeChangedEvent>> consumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(ThemeChangedEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<ThemeChangedEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }
}