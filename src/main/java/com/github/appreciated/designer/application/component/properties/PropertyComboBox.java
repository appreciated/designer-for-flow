package com.github.appreciated.designer.application.component.properties;

import com.vaadin.flow.component.combobox.ComboBox;

public class PropertyComboBox<T> extends ComboBox<T> {
    public PropertyComboBox() {
        setHeight("34px");
        getStyle()
                .set("padding", "0px")
                .set("min-width", "100px")
                .set("max-width", "200px")
                .set("margin-left", "auto")
                .set("--lumo-space-xs", "0");
    }
}
