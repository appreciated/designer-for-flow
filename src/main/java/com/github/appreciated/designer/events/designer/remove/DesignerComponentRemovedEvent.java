package com.github.appreciated.designer.events.designer.remove;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
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
