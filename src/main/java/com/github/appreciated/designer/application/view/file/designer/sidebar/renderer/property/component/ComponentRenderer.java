package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.github.appreciated.designer.model.CompilationMetaInformation;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.stream.Stream;

public class ComponentRenderer extends AbstractPropertyRenderer<Component> {
    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent != null;
    }

    @Override
    public Stream<RenderPair> render(Component component) {
        Binder<Component> componentBinder = new Binder<>();
        componentBinder.setBean(component);

        TextField id = new TextField();
        id.setValueChangeMode(ValueChangeMode.EAGER);
        componentBinder.forField(id)
                .bind(component1 -> component1.getId().orElse(null), Component::setId);
        componentBinder.setBean(component);

        TextField variableName = new TextField();
        variableName.setValueChangeMode(ValueChangeMode.EAGER);
        boolean hasInfo = getProjectFileModel().getInformation().hasCompilationMetaInformation(component);
        CompilationMetaInformation info = getProjectFileModel().getInformation().getCompilationMetaInformation(component);
        if (hasInfo && info.hasVariableName()) {
            variableName.setValue(info.getVariableName());
        }
        variableName.addValueChangeListener(event -> info.setVariableName(event.getValue()));
        return Stream.of(new RenderPair("id", id), new RenderPair("variableName", variableName));
    }

}
