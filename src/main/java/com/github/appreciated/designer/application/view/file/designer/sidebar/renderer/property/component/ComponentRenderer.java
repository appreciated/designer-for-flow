package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.github.appreciated.designer.component.properties.PropertyTextField;
import com.github.appreciated.designer.model.CompilationMetaInformation;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ComponentRenderer extends AbstractComponentPropertyRenderer<Component> {
    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent != null;
    }

    @Override
    public Stream<RenderPair> render(Component component) {
        Binder<Component> componentBinder = new Binder<>();
        componentBinder.setBean(component);

        PropertyTextField id = new PropertyTextField();
        componentBinder.forField(id)
                .bind(component1 -> component1.getId().orElse(null), Component::setId);
        componentBinder.setBean(component);

        PropertyTextField variableName = new PropertyTextField();

        Binder<AtomicReference<String>> variableNameBinder = new Binder<>();
        variableNameBinder
                .forField(variableName)
                .withValidator((Validator<String>) (s, valueContext) -> {
                    if (getProjectFileModel().getInformation().isVariableNameValid(s, component)) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error(component.getTranslation("variable.name.invalid.or.already.used"));
                    }
                })
                .bind(AtomicReference::get, AtomicReference::set);

        variableNameBinder.addValueChangeListener(valueChangeEvent -> {
            if (variableNameBinder.isValid() && valueChangeEvent.getValue() != null) {
                getProjectFileModel().getInformation()
                        .getOrCreateCompilationMetaInformation(component)
                        .setVariableName((String) valueChangeEvent.getValue());
            }
        });

        boolean hasInfo = getProjectFileModel().getInformation().hasCompilationMetaInformation(component);
        CompilationMetaInformation info = getProjectFileModel().getInformation().getCompilationMetaInformation(component);
        if (hasInfo && info.hasVariableName()) {
            variableName.setValue(info.getVariableName());
        }
        return Stream.of(new RenderPair("id", id), new RenderPair("fieldName", variableName));
    }

}
