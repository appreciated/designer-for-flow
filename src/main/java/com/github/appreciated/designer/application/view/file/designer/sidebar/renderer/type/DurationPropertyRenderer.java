package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.type;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

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
        TextField durationField = new TextField();
        durationField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
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
