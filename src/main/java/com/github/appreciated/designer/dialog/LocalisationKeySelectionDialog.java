package com.github.appreciated.designer.dialog;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class LocalisationKeySelectionDialog extends Dialog {
    public LocalisationKeySelectionDialog(Component component, String property, ProjectFileModel model, Consumer<String> resultConsumer) {
        HorizontalLayout header = new HorizontalLayout(new H3(getTranslation("select.localization.key")));
        ComboBox<String> localisationKeys = new ComboBox<>();
        Button save = new Button(getTranslation("save"), event -> {
            model.getInformation().getOrCreateCompilationMetainformation(component).setPropertyReplacement(property, localisationKeys.getValue());
            resultConsumer.accept(localisationKeys.getValue());
            close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abort = new Button(getTranslation("cancel"), event -> close());
        HorizontalLayout footer = new HorizontalLayout(abort);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.setWidthFull();
        VerticalLayout layout = new VerticalLayout(header);
        if (model.getInformation().getProject().hasTranslations()) {
            ResourceBundle rb = model.getInformation().getProject().getTranslationsBundle();
            ArrayList<String> keys = Collections.list(rb.getKeys());
            localisationKeys.setItems(keys);
            localisationKeys.setWidthFull();
            localisationKeys.setAllowCustomValue(false);
            localisationKeys.addValueChangeListener(event -> save.setEnabled(event.getValue() != null));
            if (model.getInformation().hasComponentMetainfo(component) &&
                    model.getInformation().getComponentMetainfo(component).hasPropertyReplacement("text")) {
                localisationKeys.setValue((String) model.getInformation().getComponentMetainfo(component).getPropertyReplacement("text"));
            }
            layout.add(localisationKeys);
            footer.add(save);
        } else {
            layout.add(new Span("Project has not i18n Files configured"));
        }
        layout.add(footer);
        layout.setPadding(false);
        add(layout);
        setWidth("300px");
    }
}
