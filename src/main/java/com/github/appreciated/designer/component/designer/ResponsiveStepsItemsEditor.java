package com.github.appreciated.designer.component.designer;

import com.github.appreciated.designer.component.properties.PropertyNumberField;
import com.github.appreciated.designer.regex.CssRegex;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ResponsiveStepsItemsEditor extends AbstractItemsEditor<FormLayout.ResponsiveStep, ResponsiveStepsItemsEditor> {

    public ResponsiveStepsItemsEditor(String title, Stream<FormLayout.ResponsiveStep> items, Consumer<List<FormLayout.ResponsiveStep>> valueChangeListener) {
        super(title, items, valueChangeListener);
    }

    @Override
    FormLayout.ResponsiveStep getNewValue() {
        return new FormLayout.ResponsiveStep("", 0);
    }

    @Override
    Component getComponent(Binder<AtomicReference<FormLayout.ResponsiveStep>> binder) {
        PropertyNumberField numberField = new PropertyNumberField();
        numberField.setStep(1);
        numberField.setHasControls(true);
        numberField.setValueChangeMode(ValueChangeMode.EAGER);

        binder.forField(numberField).bind(reference -> {
                    try {
                        return Double.valueOf(FieldUtils.getField(FormLayout.ResponsiveStep.class, "columns", true).getInt(reference.get()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                },
                (atomicReference, aDouble) -> {
                    try {
                        FieldUtils.writeField(FieldUtils.getField(FormLayout.ResponsiveStep.class, "columns", true), atomicReference.get(), aDouble.intValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );

        TextField textField = new TextField();

        binder.forField(textField)
                .withValidator(new RegexpValidator("Not a valid css length", CssRegex.getLengthRegex()))
                .bind(reference -> {
                            try {
                                return (String) FieldUtils.getField(FormLayout.ResponsiveStep.class, "minWidth", true).get(reference.get());
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            return null;
                        },
                        (atomicReference, string) -> {
                            try {
                                FieldUtils.writeField(FieldUtils.getField(FormLayout.ResponsiveStep.class, "minWidth", true), atomicReference.get(), string);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                );

        return new HorizontalLayout(numberField, textField);
    }


}
