package com.github.appreciated.designer.component.designer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StringItemsEditor extends AbstractItemsEditor<String, StringItemsEditor> {

    public StringItemsEditor(String title, Stream<String> items, Consumer<List<String>> valueChangeListener) {
        super(title, items, valueChangeListener);
    }

    @Override
    String getNewValue() {
        return "";
    }

    @Override
    Component getComponent(Binder<AtomicReference<String>> binder) {
        TextField textField = new TextField();
        binder.forField(textField)
                .bind(AtomicReference::get, AtomicReference::set);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateValues());
        return textField;
    }


}
