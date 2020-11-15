package com.github.appreciated.designer.application.designer.file;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
@UIScope
public class FileChangedEventListener implements ApplicationListener<FileChangedEvent> {
    private final List<Consumer<FileChangedEvent>> consumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(FileChangedEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<FileChangedEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }
}