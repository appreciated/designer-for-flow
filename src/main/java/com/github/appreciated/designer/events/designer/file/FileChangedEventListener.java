package com.github.appreciated.designer.events.designer.file;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class FileChangedEventListener implements ApplicationListener<FileChangedEvent> {
    private List<Consumer<FileChangedEvent>> consumerList = new ArrayList<>();

    @Override
    public void onApplicationEvent(FileChangedEvent event) {
        consumerList.forEach(elementFocusedEventConsumer -> elementFocusedEventConsumer.accept(event));
    }

    public void addEventConsumer(Consumer<FileChangedEvent> eventConsumer) {
        this.consumerList.add(eventConsumer);
    }
}