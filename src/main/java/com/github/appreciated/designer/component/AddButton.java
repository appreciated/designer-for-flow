package com.github.appreciated.designer.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;

public class AddButton extends Button {

    public AddButton(Icon icon) {
        this(icon, null);
    }

    public AddButton(Icon icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(icon);
        getStyle()
                .set("position", "absolute")
                .set("transition", "all 0.3s ease")
                .set("color", "var(--lumo-primary-contrast-color)")
                .set("box-shadow", "var(--lumo-box-shadow-m)")
                .set("background", "var(--lumo-primary-color)")
                .set("border-radius", "100%")
                .set("right", "30px")
                .set("bottom", "85px");
        setWidth("50px");
        setHeight("50px");
        if (clickListener != null) {
            addClickListener(clickListener);
        }
    }

    public void setBottom(String bottom) {
        getStyle().set("bottom", bottom);
    }
}