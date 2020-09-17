package com.github.appreciated.designer.events.designer.dnd;

import com.vaadin.flow.component.Component;
import org.springframework.context.ApplicationEvent;

public class DesignerComponentDragEvent extends ApplicationEvent {

    private Component draggedComponent;
    private boolean isStarted;

    public DesignerComponentDragEvent(Object source) {
        super(source);
    }

    public DesignerComponentDragEvent(Object source, Component draggedComponent, boolean isStarted) {
        super(source);
        this.draggedComponent = draggedComponent;
        this.isStarted = isStarted;
    }


    public Component getDraggedComponent() {
        return draggedComponent;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
