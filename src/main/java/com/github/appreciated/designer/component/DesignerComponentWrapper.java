package com.github.appreciated.designer.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.function.Consumer;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;

@Tag("designer-component-wrapper")
@JsModule("./src/designer-component-wrapper.js")
public class DesignerComponentWrapper extends PolymerTemplate<TemplateModel> implements HasStyle, HasSize {

    @Id("wrapper")
    Div wrapper;
    private final Component actualComponent;
    private final boolean isProjectComponent;
    private Consumer consumer;

    public DesignerComponentWrapper(Component component, boolean isProjectComponent) {
        this.actualComponent = component;
        this.isProjectComponent = isProjectComponent;
        getElement().appendChild(component.getElement());
        if (!isComponentContainer(component, isProjectComponent) || isProjectComponent) {
            wrapper.getElement().getClassList().add("no-pointer-events");
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

    public Component getActualComponent() {
        return actualComponent;
    }

    public void setNonNestedClickListener(Consumer consumer) {
        this.consumer = consumer;
    }

    public boolean isProjectComponent() {
        return isProjectComponent;
    }
}
