package com.github.appreciated.designer.component.properties;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class PropertyNumberField extends NumberField {
    public PropertyNumberField() {
        getStyle()
                .set("padding", "0px")
                .set("min-width", "100px")
                .set("--lumo-space-xs", "0");
        setHeight("34px");
        setValueChangeMode(ValueChangeMode.EAGER);
        setHasControls(true);
        setStep(0.01);
    }
}
