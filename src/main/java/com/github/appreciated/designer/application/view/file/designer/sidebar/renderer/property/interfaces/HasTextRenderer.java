package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.github.appreciated.designer.component.SmallIconButton;
import com.github.appreciated.designer.dialog.LocalisationKeySelectionDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.stream.Stream;

public class HasTextRenderer extends AbstractPropertyRenderer<HasText> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof HasText;
    }

    @Override
    public Stream<RenderPair> render(HasText component) {
        TextField textField = new TextField();
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        SmallIconButton clear = new SmallIconButton(VaadinIcon.CLOSE_SMALL.create(), event -> {
            textField.setValue("");
            textField.setReadOnly(false);
        });
        clear.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        SmallIconButton localizationButton = new SmallIconButton(VaadinIcon.FLAG.create(), event ->
                new LocalisationKeySelectionDialog((Component) component,
                        "text",
                        getProjectFileModel(),
                        result -> {
                            component.setText(getProjectFileModel().getInformation().getProject().getTranslationForKey(result));
                            textField.setValue(result);
                            textField.setReadOnly(true);
                        }).open()
        );
        HorizontalLayout buttons = new HorizontalLayout(clear, localizationButton);
        buttons.setSpacing(false);
        textField.setSuffixComponent(buttons);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        if (getProjectFileModel().getInformation().hasCompilationMetaInformation((Component) component) &&
                getProjectFileModel().getInformation().getCompilationMetaInformation((Component) component).hasPropertyReplacement("text")) {
            textField.setValue((String) getProjectFileModel().getInformation().getCompilationMetaInformation((Component) component).getPropertyReplacement("text"));
            textField.setReadOnly(true);
        } else {
            textField.setValue(component.getText());
        }
        textField.addValueChangeListener(event -> {
            if (getProjectFileModel().getInformation().hasCompilationMetaInformation((Component) component) &&
                    getProjectFileModel().getInformation().getCompilationMetaInformation((Component) component).hasPropertyReplacement("text")) {
                String key = (String) getProjectFileModel().getInformation().getCompilationMetaInformation((Component) component).getPropertyReplacement("text");
                component.setText(getProjectFileModel().getInformation().getProject().getTranslationForKey(key));
            } else {
                component.setText(event.getValue());
            }
        });
        return Stream.of(new RenderPair("text", textField));
    }

}
