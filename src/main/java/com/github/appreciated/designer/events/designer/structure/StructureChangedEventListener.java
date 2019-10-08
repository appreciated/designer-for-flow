package com.github.appreciated.designer.events.designer.structure;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class StructureChangedEventListener implements ApplicationListener<StructureChangedEvent> {
    private List<Consumer<StructureChangedEvent>> consumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(StructureChangedEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<StructureChangedEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }
}