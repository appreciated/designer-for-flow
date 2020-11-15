package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.github.appreciated.designer.application.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.application.component.properties.PropertyTextField;
import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;

public class StringPropertyRenderer extends AbstractPropertyRenderer<String> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, CustomPropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == String.class;
    }

    public Component render(String propertyName, CustomPropertyDescriptor propertyDescriptor, Component propertyParent) {
        PropertyTextField textField = new PropertyTextField();
        try {
            String value = (String) propertyDescriptor.getReadMethod().invoke(propertyParent);
            if (value != null) {
                textField.setValue(value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        textField.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> applyValue(propertyParent, propertyDescriptor, textField.getValue()));
        return textField;
    }
}

