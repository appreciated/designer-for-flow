package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.type;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.beans.PropertyDescriptor;

public class IntegerPropertyRenderer extends AbstractPropertyRenderer {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Integer.class || propertyDescriptor.getPropertyType().getName().equals("int");
    }

    public Component render(String propertyName, PropertyDescriptor propertyDescriptor, Component propertyParent) {
        NumberField numberField = new NumberField();
        numberField.setHasControls(true);
        numberField.setValueChangeMode(ValueChangeMode.EAGER);
        numberField.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> applyValue(propertyParent, propertyDescriptor, numberField.getValue()));
        return numberField;
    }
}
