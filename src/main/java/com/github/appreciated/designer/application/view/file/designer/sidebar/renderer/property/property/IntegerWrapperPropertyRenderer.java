package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.github.appreciated.designer.application.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.application.component.properties.PropertyNumberField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.value.ValueChangeMode;

public class IntegerWrapperPropertyRenderer extends AbstractPropertyRenderer<Integer> {
    @Override
    public boolean canRender(Component propertyParent, String propertyName, CustomPropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Integer.class;
    }

    public Component render(String propertyName, CustomPropertyDescriptor propertyDescriptor, Component propertyParent) {
        PropertyNumberField numberField = new PropertyNumberField();
        numberField.setStep(1);
        numberField.setHasControls(true);
        numberField.setValueChangeMode(ValueChangeMode.EAGER);
        numberField.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> applyValue(propertyParent, propertyDescriptor, numberField.getValue().intValue()));
        return numberField;
    }
}
