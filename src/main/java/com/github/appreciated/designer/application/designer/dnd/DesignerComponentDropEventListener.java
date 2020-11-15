package com.github.appreciated.designer.application.designer.dnd;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
@UIScope
public class DesignerComponentDropEventListener implements ApplicationListener<DesignerComponentDropEvent> {
    private final List<Consumer<DesignerComponentDropEvent>> consumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(DesignerComponentDropEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<DesignerComponentDropEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }
}