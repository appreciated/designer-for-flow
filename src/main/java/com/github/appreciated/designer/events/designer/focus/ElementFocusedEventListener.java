package com.github.appreciated.designer.events.designer.focus;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class ElementFocusedEventListener implements ApplicationListener<ElementFocusedEvent> {
    private List<Consumer<ElementFocusedEvent>> consumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(ElementFocusedEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<ElementFocusedEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }
}