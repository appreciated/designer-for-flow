package com.github.appreciated.designer.component.designer;

import com.github.appreciated.designer.component.DesignerComponent;
import com.vaadin.flow.component.html.Span;

public class DesignerComponentLabel extends Span {

    private DesignerComponent designerComponent;
    private String name;

    public DesignerComponentLabel(DesignerComponent designerComponent) {
        super(designerComponent.getClassNameAsString());
        this.designerComponent = designerComponent;
        name = designerComponent.getClassNameAsString();
        setWidthFull();
        getStyle().set("cursor", "grab")
                .set("padding", "5px")
                .set("box-sizing", "border-box")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("box-shadow", "var(--lumo-box-shadow-xs)")
                .set("background", "var(--lumo-primary-contrast-color)");
    }

    public String getName() {
        return name;
    }

    public DesignerComponent getDesignerComponent() {
        return designerComponent;
    }
}
