package com.github.appreciated.designer.component.properties;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

public class PropertyTextField extends TextField {
    public PropertyTextField() {
        getStyle()
                .set("padding", "0px")
                .set("min-width", "100px")
                .set("--lumo-space-xs", "0");
        setHeight("30px");
        addThemeVariants(TextFieldVariant.LUMO_SMALL);
        setValueChangeMode(ValueChangeMode.EAGER);
    }
}
