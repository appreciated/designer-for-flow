package com.github.appreciated.designer.events.designer.structure;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
@UIScope
public class StructureChangedEventListener implements ApplicationListener<StructureChangedEvent> {
    private final List<Consumer<StructureChangedEvent>> consumerList = new ArrayList<>();
    private final List<Consumer<StructureChangedEvent>> afterConsumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(StructureChangedEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
        afterConsumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<StructureChangedEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }

    public void addAfterEventConsumer(Consumer<StructureChangedEvent> eventConsumer) {
        this.afterConsumerList.add(eventConsumer);
    }
}