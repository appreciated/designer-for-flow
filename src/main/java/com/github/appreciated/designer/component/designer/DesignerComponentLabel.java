package com.github.appreciated.designer.component.designer;

import com.github.appreciated.designer.component.DesignerComponent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DesignerComponentLabel extends HorizontalLayout {

    private final DesignerComponent designerComponent;
    private final String name;

    public DesignerComponentLabel(DesignerComponent designerComponent) {
        setAlignItems(Alignment.CENTER);
        Span dot = new Span();
        dot.getStyle().set("background", "var(--lumo-primary-color)")
                .set("width", "7px")
                .set("height", "7px")
                .set("border-radius", "100%")
                .set("margin-left", "26px")
                .set("margin-right", "-4px")
                .set("flex-shrink", "0")
                .set("box-shadow", "var(--lumo-primary-color) 0px 1px 5px 0px");
        Span label = new Span(designerComponent.getClassNameAsString());
        add(dot, label);
        this.designerComponent = designerComponent;
        name = designerComponent.getClassNameAsString();
        setWidthFull();
        getStyle().set("cursor", "grab");
    }

    public String getName() {
        return name;
    }

    public DesignerComponent getDesignerComponent() {
        return designerComponent;
    }
}
