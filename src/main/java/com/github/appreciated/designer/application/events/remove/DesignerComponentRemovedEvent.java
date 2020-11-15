package com.github.appreciated.designer.application.events.remove;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import org.springframework.context.ApplicationEvent;

public class DesignerComponentRemovedEvent extends ApplicationEvent {

    private DesignerComponentWrapper draggedComponent;

    public DesignerComponentRemovedEvent(Object source) {
        super(source);
    }

    public DesignerComponentRemovedEvent(Object source, DesignerComponentWrapper draggedComponent) {
        super(source);
        this.draggedComponent = draggedComponent;
    }


    public DesignerComponentWrapper getRemovedComponent() {
        return draggedComponent;
    }

}
