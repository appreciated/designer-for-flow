package com.github.appreciated.designer.application.events.file;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;
import org.springframework.context.ApplicationEvent;

public class FileChangedEvent extends ApplicationEvent {

    private final DesignerComponentWrapper parent;
    private final Component focus;

    public FileChangedEvent(Object source, Component focus, DesignerComponentWrapper parent) {
        super(source);
        this.focus = focus;
        this.parent = parent;
    }

    public FileChangedEvent(FileChangedEventPublisher elementFocusedEventPublisher, Component focus) {
        super(elementFocusedEventPublisher);
        this.focus = focus;
        parent = (DesignerComponentWrapper) focus.getParent().orElse(null);
    }

    public Component getFocus() {
        return focus;
    }

    public DesignerComponentWrapper getParent() {
        return parent;
    }
}
