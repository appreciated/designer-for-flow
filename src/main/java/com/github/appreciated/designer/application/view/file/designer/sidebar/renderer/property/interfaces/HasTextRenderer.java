package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces;

import com.github.appreciated.designer.application.component.SmallIconButton;
import com.github.appreciated.designer.application.component.properties.PropertyTextField;
import com.github.appreciated.designer.application.dialog.LocalisationKeySelectionDialog;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.stream.Stream;

public class HasTextRenderer extends AbstractComponentPropertyRenderer<HasText> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof HasText;
    }

    @Override
    public Stream<RenderPair> render(HasText component) {
        PropertyTextField textField = new PropertyTextField();
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
