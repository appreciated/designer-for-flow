package com.github.appreciated.designer.application.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class SmallIconButton extends IconButton {

    public SmallIconButton(Component icon) {
        super(icon);
        addThemeVariants(ButtonVariant.LUMO_SMALL);
    }

    public SmallIconButton(Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(icon, clickListener);
        addThemeVariants(ButtonVariant.LUMO_SMALL);
    }
}
