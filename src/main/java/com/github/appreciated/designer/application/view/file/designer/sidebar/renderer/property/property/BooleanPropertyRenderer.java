package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.github.appreciated.designer.component.CustomPropertyDescriptor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;

import java.lang.reflect.InvocationTargetException;

public class BooleanPropertyRenderer extends AbstractPropertyRenderer<Boolean> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, CustomPropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Boolean.class || propertyDescriptor.getPropertyType() == boolean.class;
    }

    public Component render(String propertyName, CustomPropertyDescriptor propertyDescriptor, Component propertyParent) {
        Checkbox checkbox = new Checkbox();
        try {
            Boolean property = (Boolean) propertyDescriptor.getReadMethod().invoke(propertyParent);
            setValueButNull(checkbox, property);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        checkbox.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> applyValue(propertyParent, propertyDescriptor, checkbox.getValue()));
        return checkbox;
    }
}
