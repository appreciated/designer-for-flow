package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.github.appreciated.designer.component.properties.PropertyNumberField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Optional;
import java.util.stream.Stream;

public class FormLayoutChildRenderer extends AbstractComponentPropertyRenderer<Component> {
    @Override
    public boolean canRender(Component propertyParent) {
        return getFormLayoutParent(propertyParent).isPresent();
    }

    private Optional<FormLayout> getFormLayoutParent(Component propertyParent) {
        if (propertyParent.getParent().isPresent() &&
                propertyParent.getParent().get().getParent().isPresent() &&
                propertyParent.getParent().get().getParent().get() instanceof FormLayout) {
            return Optional.of((FormLayout) propertyParent.getParent().get().getParent().get());
        }
        return Optional.empty();
    }

    @Override
    public Stream<RenderPair> render(Component component) {
        PropertyNumberField numberField = new PropertyNumberField();
        numberField.setStep(1);
        numberField.setHasControls(true);
        numberField.setValueChangeMode(ValueChangeMode.EAGER);
        numberField.addValueChangeListener(changeEvent ->
                getFormLayoutParent(component).get().setColspan(component, changeEvent.getValue().intValue())
        );
        return Stream.of(new RenderPair("colspan", numberField));
    }

    @Override
    public Stream<String> rendersProperty() {
        return Stream.of("src");
    }

}

