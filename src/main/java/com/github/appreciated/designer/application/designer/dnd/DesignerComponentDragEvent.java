package com.github.appreciated.designer.application.designer.dnd;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import org.springframework.context.ApplicationEvent;

public class DesignerComponentDragEvent extends ApplicationEvent {

    private DesignerComponentWrapper draggedComponent;
    private boolean isStarted;

    public DesignerComponentDragEvent(Object source) {
        super(source);
    }

    public DesignerComponentDragEvent(Object source, DesignerComponentWrapper draggedComponent, boolean isStarted) {
        super(source);
        this.draggedComponent = draggedComponent;
        this.isStarted = isStarted;
    }


    public DesignerComponentWrapper getDraggedComponent() {
        return draggedComponent;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
