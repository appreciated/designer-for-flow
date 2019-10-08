package com.github.appreciated.designer.reflection.renderer;

import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

import java.util.stream.Stream;

public abstract class AbstractPropertyRenderer<T> {
    ProjectService service;

    public abstract boolean canRender(Component propertyParent);

    public abstract void applyValue(T propertyParent);

    public void setValueButNull(HasValue field, Object value) {
        if (value != null) {
            field.setValue(value);
        }
    }

    public ProjectService getService() {
        return service;
    }

    public void setService(ProjectService service) {
        this.service = service;
    }

    public Stream<String> rendersCssStyle() {
        return Stream.empty();
    }

    public Stream<String> rendersProperty() {
        return Stream.empty();
    }

    public abstract Stream<RenderPair> render(T propertyParent);

    public void setProjectService(ProjectService service) {
        this.service = service;
    }
}
