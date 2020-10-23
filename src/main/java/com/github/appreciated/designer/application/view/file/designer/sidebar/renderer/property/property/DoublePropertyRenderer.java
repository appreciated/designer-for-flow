package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.github.appreciated.designer.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.component.properties.PropertyNumberField;
import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;

public class DoublePropertyRenderer extends AbstractPropertyRenderer<Double> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, CustomPropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == double.class;
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
        numberField.addValueChangeListener(checkboxBooleanComponentValueChangeEvent ->
                applyValue(propertyParent, propertyDescriptor, numberField.getValue() != null ? numberField.getValue() : 0.0)
        );
        return numberField;
    }
}
