package com.github.appreciated.designer.component;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.function.Consumer;

@Tag("designer-component-wrapper")
@JsModule("./src/designer-component-wrapper.js")
public class DesignerComponentWrapper extends PolymerTemplate<TemplateModel> implements HasStyle, HasSize {

    @Id("wrapper")
    Div wrapper;
    private Component actualComponent;
    @Id("content")
    private Slot slot;
    private Consumer consumer;

    public DesignerComponentWrapper(Component component) {
        this.actualComponent = component;
        getElement().appendChild(component.getElement());
        if (!(component instanceof HasComponents)) {
            slot.getElement().getClassList().add("no-pointer-events");
        }
        if (actualComponent instanceof HasSize) {
            if (((HasSize) actualComponent).getWidth() != null) {
                this.setWidth(((HasSize) actualComponent).getWidth());
            }
            if (((HasSize) actualComponent).getHeight() != null) {
                this.setHeight(((HasSize) actualComponent).getHeight());
            }
            ((HasSize) actualComponent).setSizeUndefined();
        }
    }

    @Override
    public void setWidth(String width) {
        HasSize.super.setWidth(width);
        wrapper.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        HasSize.super.setHeight(height);
        wrapper.setHeight(height);
    }

    @EventHandler
    private void componentClicked() {
        if (consumer != null) {
            consumer.accept(null);
        }
    }

    public void setFocus(boolean focus) {
        if (focus) {
            wrapper.addClassName("focus");
        } else {
            wrapper.removeClassName("focus");
        }
    }

    @Override
    public void setSizeFull() {
        HasSize.super.setSizeFull();
        wrapper.setSizeFull();
    }

    @Override
    public void setWidthFull() {
        HasSize.super.setWidthFull();
        wrapper.setWidthFull();
    }

    @Override
    public void setHeightFull() {
        HasSize.super.setHeightFull();
        wrapper.setHeightFull();
    }

    public Slot getSlot() {
        return slot;
    }

    public Component getActualComponent() {
        return actualComponent;
    }

    public void setNonNestedClickListener(Consumer consumer) {
        this.consumer = consumer;
    }
}
