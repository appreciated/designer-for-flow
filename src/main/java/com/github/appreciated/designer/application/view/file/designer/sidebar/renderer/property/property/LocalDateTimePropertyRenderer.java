package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

public class LocalDateTimePropertyRenderer extends AbstractPropertyRenderer<LocalDateTime> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == LocalDateTime.class;
    }

    public Component render(String propertyName, PropertyDescriptor propertyDescriptor, Component propertyParent) {
        DateTimePicker dateTimePicker = new DateTimePicker();
        try {
            LocalDateTime property = (LocalDateTime) propertyDescriptor.getReadMethod().invoke(propertyParent);
            setValueButNull(dateTimePicker, property);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        dateTimePicker.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> applyValue(propertyParent, propertyDescriptor, dateTimePicker.getValue()));
        return dateTimePicker;
    }
}
