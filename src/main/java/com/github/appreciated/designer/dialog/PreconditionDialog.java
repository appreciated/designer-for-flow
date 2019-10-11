package com.github.appreciated.designer.dialog;

import com.github.appreciated.designer.component.DesignerComponent;
import com.github.appreciated.designer.file.HasPreconditions;
import com.github.appreciated.designer.service.ComponentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.function.ValueProvider;

import javax.lang.model.SourceVersion;
import java.util.Map;
import java.util.function.Consumer;

public class PreconditionDialog extends Dialog {
    private final HasPreconditions preconditions;
    private final H2 header;
    private ComponentService componentService;
    private final Consumer onFulfileld;
    private final Binder<Map<String, Object>> binder;
    private final FormLayout form;

    public PreconditionDialog(HasPreconditions preconditions, Consumer onFullfilled) {
        this.preconditions = preconditions;
        this.componentService = new ComponentService();
        this.onFulfileld = onFullfilled;
        header = new H2("Please enter the following data");
        binder = new Binder<>();
        form = new FormLayout();
        if (preconditions.getPreconditions() != null) {
            preconditions.getPreconditionNames()
                    .entrySet()
                    .stream()
                    .filter(entry -> !preconditions.getPreconditions().containsKey(entry.getKey()))
                    .forEach(this::addFullfillment);
        } else {
            preconditions.getPreconditionNames()
                    .entrySet()
                    .forEach(this::addFullfillment);
        }
        binder.setBean(preconditions.getPreconditions());

        Button select = new Button("Select", event -> {
            if (binder.isValid()) {
                onFullfilled.accept(null);
                close();
            }
        });
        select.setThemeName(ButtonVariant.LUMO_PRIMARY.getVariantName());
        HorizontalLayout buttons = new HorizontalLayout(select);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setWidthFull();
        VerticalLayout layout = new VerticalLayout(header, form, buttons);
        layout.setPadding(false);
        add(layout);
    }

    private void addFullfillment(Map.Entry<String, Class> entry) {
        if (entry.getValue() == String.class) {
            TextField field = new TextField();
            field.setLabel(entry.getKey());
            binder.forField(field)
                    .asRequired()
                    .withValidator((s, valueContext) -> {
                        if (SourceVersion.isIdentifier(s) && !SourceVersion.isKeyword(s)) {
                            return ValidationResult.ok();
                        } else {
                            return ValidationResult.error("Please enter a valid Java ClassName");
                        }
                    })
                    .bind((ValueProvider<Map<String, Object>, String>) map -> (String) map.get(entry.getKey()),
                            (Setter<Map<String, Object>, String>) (stringObjectMap, value) -> stringObjectMap.put(entry.getKey(), value)
                    );
            form.add(field);
        } else if (entry.getValue() == Class.class) {
            ComboBox<Class> comboBox = new ComboBox<>();
            comboBox.setLabel(entry.getKey());
            comboBox.setItemLabelGenerator(Class::getSimpleName);
            comboBox.setItems(componentService.getAllComponents().map(DesignerComponent::getClassName));
            binder.forField(comboBox)
                    .asRequired()
                    .bind((ValueProvider<Map<String, Object>, Class>) map -> (Class) map.get(entry.getKey()),
                            (Setter<Map<String, Object>, Class>) (stringObjectMap, value) -> stringObjectMap.put(entry.getKey(), value)
                    );
            form.add(comboBox);
        }
    }

}
