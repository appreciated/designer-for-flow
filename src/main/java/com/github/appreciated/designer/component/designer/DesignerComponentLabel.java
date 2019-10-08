package com.github.appreciated.designer.component.designer;

import com.github.appreciated.designer.component.DesignerComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;

public class DesignerComponentLabel extends Button {

    private DesignerComponent designerComponent;
    private String name;

    public DesignerComponentLabel(DesignerComponent designerComponent) {
        this.designerComponent = designerComponent;
        setText(designerComponent.getClassName().getSimpleName());
        name = designerComponent.getClassName().getSimpleName();
        setWidthFull();
        addClickListener(buttonClickEvent -> Notification.show("Drag and Drop me in the Design!"));
    }

    public String getName() {
        return name;
    }

    public DesignerComponent getDesignerComponent() {
        return designerComponent;
    }
}
