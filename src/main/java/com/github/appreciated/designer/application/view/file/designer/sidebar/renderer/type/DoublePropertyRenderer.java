package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.type;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.beans.PropertyDescriptor;

public class DoublePropertyRenderer extends AbstractPropertyRenderer<Double> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Double.class || propertyDescriptor.getPropertyType() == double.class;
    }

    public Component render(String propertyName, PropertyDescriptor propertyDescriptor, Component propertyParent) {
        NumberField numberField = new NumberField();
        numberField.setValueChangeMode(ValueChangeMode.EAGER);
        numberField.setHasControls(true);
        numberField.setStep(0.01);
        numberField.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> applyValue(propertyParent, propertyDescriptor, numberField.getValue()));
        return numberField;
    }
}
