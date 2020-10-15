package com.github.appreciated.designer.events.designer.dnd;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
@UIScope
public class DesignerComponentDragEventListener implements ApplicationListener<DesignerComponentDragEvent> {
    private final List<Consumer<DesignerComponentDragEvent>> consumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(DesignerComponentDragEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<DesignerComponentDragEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }
}