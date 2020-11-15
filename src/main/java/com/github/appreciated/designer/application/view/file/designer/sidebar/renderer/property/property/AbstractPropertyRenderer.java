package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property;

import com.github.appreciated.designer.application.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPropertyRenderer<T> {

    private ProjectFileModel projectFileModel;

    public abstract boolean canRender(Component propertyParent, String propertyName, CustomPropertyDescriptor propertyDescriptor);

    public abstract Component render(String propertyName, CustomPropertyDescriptor propertyDescriptor, Component propertyParent);


    public void applyValue(Component propertyParent, CustomPropertyDescriptor propertyDescriptor, T object) {
        try {
            propertyDescriptor.getWriteMethod().invoke(propertyParent, object);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setValueButNull(HasValue field, Object value) {
        if (value != null) {
            field.setValue(value);
        }
    }

    public ProjectFileModel getProjectFileModel() {
        return projectFileModel;
    }

    public void setProjectFileModel(ProjectFileModel projectFileModel) {
        this.projectFileModel = projectFileModel;
    }
}
