package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.github.appreciated.designer.component.properties.PropertyTextField;
import com.github.appreciated.designer.model.CompilationMetaInformation;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;

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
        boolean hasInfo = getProjectFileModel().getInformation().hasCompilationMetaInformation(component);
        CompilationMetaInformation info = getProjectFileModel().getInformation().getCompilationMetaInformation(component);
        if (hasInfo && info.hasVariableName()) {
            variableName.setValue(info.getVariableName());
        }
        variableName.addValueChangeListener(event -> info.setVariableName(event.getValue()));
        return Stream.of(new RenderPair("id", id), new RenderPair("variableName", variableName));
    }

}
