package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.github.appreciated.designer.application.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.application.component.properties.PropertyNumberField;
import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;

public class DoubleWrapperPropertyRenderer extends AbstractPropertyRenderer<Double> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, CustomPropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Double.class;
    }

    public Component render(String propertyName, CustomPropertyDescriptor propertyDescriptor, Component propertyParent) {
        PropertyNumberField numberField = new PropertyNumberField();
        try {
            Double value = (Double) propertyDescriptor.getReadMethod().invoke(propertyParent);
            if (value != null) {
                numberField.setValue(value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        numberField.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> {
            applyValue(propertyParent, propertyDescriptor, numberField.getValue());
        });
        return numberField;
    }
}
