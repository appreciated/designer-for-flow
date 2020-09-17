package com.github.appreciated.designer.events.designer.dnd;

import com.vaadin.flow.component.Component;
import org.springframework.context.ApplicationEvent;

public class DesignerComponentDropEvent extends ApplicationEvent {

    private Component draggedComponent;
    private Component targetComponent;

    public DesignerComponentDropEvent(Object source) {
        super(source);
    }

    public DesignerComponentDropEvent(Object source, Component draggedComponent, Component targetComponent) {
        super(source);
        this.draggedComponent = draggedComponent;
        this.targetComponent = targetComponent;
    }

    public Component getDraggedComponent() {
        return draggedComponent;
    }

    public Component getTargetComponent() {
        return targetComponent;
    }
}
