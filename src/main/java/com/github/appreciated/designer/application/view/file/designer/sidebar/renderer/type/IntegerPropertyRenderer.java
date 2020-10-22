package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.type;

import com.github.appreciated.designer.component.properties.PropertyNumberField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.beans.PropertyDescriptor;

public class IntegerPropertyRenderer extends AbstractPropertyRenderer<Integer> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Integer.class || propertyDescriptor.getPropertyType() == int.class;
    }

    public Component render(String propertyName, PropertyDescriptor propertyDescriptor, Component propertyParent) {
        PropertyNumberField numberField = new PropertyNumberField();
        numberField.setHasControls(true);
        numberField.setValueChangeMode(ValueChangeMode.EAGER);
        numberField.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> applyValue(propertyParent, propertyDescriptor, numberField.getValue().intValue()));
        return numberField;
    }
}
