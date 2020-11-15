package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.github.appreciated.designer.application.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.application.component.properties.PropertyTextField;
import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DurationPropertyRenderer extends AbstractPropertyRenderer<Duration> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, CustomPropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Duration.class;
    }

    public Component render(String propertyName, CustomPropertyDescriptor propertyDescriptor, Component propertyParent) {
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
