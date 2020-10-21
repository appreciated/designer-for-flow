package com.github.appreciated.designer.component.designer;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringItemsEditor extends HasValueEditorDetails<StringItemsEditor> {
    private final Consumer<List<String>> valueChangeListener;

    public StringItemsEditor(String title, Stream<String> items, Consumer<List<String>> valueChangeListener) {
        super(title, new VerticalLayout(), true);
        this.valueChangeListener = valueChangeListener;
        items.forEach(value -> withFormField((String) null, getFieldForValue(value), aBoolean -> addNewField(value)));
        setOnAddListener(aBoolean -> addNewField(null));
    }

    TextField getFieldForValue(String value) {
        TextField textField = new TextField();
        textField.setValue(value);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateValues());
        return textField;
    }

    void addNewField(String value) {
        withFormField(value, getFieldForValue(""), aBoolean -> updateValues());
        updateValues();
    }

    private void updateValues() {
        valueChangeListener.accept(getComponents().map(component -> (TextField) component).map(TextField::getValue).collect(Collectors.toList()));
    }
}
