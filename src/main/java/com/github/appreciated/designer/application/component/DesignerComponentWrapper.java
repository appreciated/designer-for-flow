package com.github.appreciated.designer.application.component;

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
import dev.mett.vaadin.tooltip.Tooltips;
import dev.mett.vaadin.tooltip.config.TC_HIDE_ON_CLICK;
import dev.mett.vaadin.tooltip.config.TC_PLACEMENT;
import dev.mett.vaadin.tooltip.config.TooltipConfiguration;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;

@Tag("designer-component-wrapper")
@JsModule("./src/designer-component-wrapper.js")
public class DesignerComponentWrapper extends PolymerTemplate<TemplateModel> implements HasStyle, HasSize {

    @Id("wrapper")
    Div wrapper;
    private final Component actualComponent;
    private final boolean isProjectComponent;
    private Runnable consumer;
    private Runnable removeListener;

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

        TooltipConfiguration ttconfig = new TooltipConfiguration("<vaadin-button onclick=\"window.deleteButtonClickObserver()\" id=\"delete\" theme=\"icon tertiary\" style=\"padding:0;margin:0;\" aria-label=\"Delete item\">\n" +
                "<iron-icon icon=\"vaadin:trash\" slot=\"prefix\"></iron-icon>\n" +
                "</vaadin-button>");
        ttconfig.setAllowHTML(true);
        ttconfig.setDuration(null, 20);
        ttconfig.setTrigger("manual");
        ttconfig.setArrow(false);
        ttconfig.setOffset(0, 0);
        //ttconfig.setInteractive(true);
        ttconfig.setPlacement(TC_PLACEMENT.TOP_START);
        ttconfig.setHideOnClick(TC_HIDE_ON_CLICK.FALSE);
        ttconfig.setShowOnCreate(false);
        Tooltips.getCurrent().setTooltip(this, ttconfig);
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
            consumer.run();
        }
    }

    @EventHandler
    private void deleteClicked() {
        System.out.println("deleteClicked!");
        if (removeListener != null) {
            removeListener.run();
        }
    }

    public void setFocus(boolean focus) {
        if (focus) {
            wrapper.addClassName("focus");
            Tooltips.getCurrent().showTooltip(this);
        } else {
            wrapper.removeClassName("focus");
            Tooltips.getCurrent().hideTooltip(this);
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

    public void setNonNestedClickListener(Runnable consumer) {
        this.consumer = consumer;
    }

    public boolean isProjectComponent() {
        return isProjectComponent;
    }

    public void setRemoveListener(Runnable removeListener) {
        this.removeListener = removeListener;
    }
}
