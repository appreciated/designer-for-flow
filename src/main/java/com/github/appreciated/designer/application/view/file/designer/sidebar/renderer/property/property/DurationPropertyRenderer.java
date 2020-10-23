package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.github.appreciated.designer.component.properties.PropertyTextField;
import com.vaadin.flow.component.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DurationPropertyRenderer extends AbstractPropertyRenderer<Duration> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Duration.class;
    }

    public Component render(String propertyName, PropertyDescriptor propertyDescriptor, Component propertyParent) {
        PropertyTextField durationField = new PropertyTextField();
        try {
            String property = ((Duration) propertyDescriptor.getReadMethod().invoke(propertyParent)).toString();
            setValueButNull(durationField, property);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        durationField.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> {
            try {
                applyValue(propertyParent, propertyDescriptor, Duration.parse(durationField.getValue()));
            } catch (DateTimeParseException e) {

            }
        });
        return durationField;
    }
}
