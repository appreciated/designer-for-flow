package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.type;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class BooleanPropertyRenderer extends AbstractPropertyRenderer<Boolean> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Boolean.class || propertyDescriptor.getPropertyType() == boolean.class;
    }

    public Component render(String propertyName, PropertyDescriptor propertyDescriptor, Component propertyParent) {
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
