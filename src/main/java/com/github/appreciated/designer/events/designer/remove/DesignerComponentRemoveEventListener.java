package com.github.appreciated.designer.events.designer.remove;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
@UIScope
public class DesignerComponentRemoveEventListener implements ApplicationListener<DesignerComponentRemovedEvent> {
    private final List<Consumer<DesignerComponentRemovedEvent>> consumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(DesignerComponentRemovedEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<DesignerComponentRemovedEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }
}