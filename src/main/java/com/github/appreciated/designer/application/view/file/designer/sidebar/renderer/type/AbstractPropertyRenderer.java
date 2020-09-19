package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.type;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPropertyRenderer {

    private ProjectFileModel projectFileModel;

    public abstract boolean canRender(Component propertyParent, String propertyName, PropertyDescriptor propertyDescriptor);

    public abstract Component render(String propertyName, PropertyDescriptor propertyDescriptor, Component propertyParent);


    public void applyValue(Component propertyParent, PropertyDescriptor propertyDescriptor, Object icon) {
        try {
            propertyDescriptor.getWriteMethod().invoke(propertyParent, icon);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setValueButNull(HasValue field, Object value) {
        if (value != null) {
            field.setValue(value);
        }
    }

    public void setProjectFileModel(ProjectFileModel projectFileModel) {
        this.projectFileModel = projectFileModel;
    }

    public ProjectFileModel getProjectFileModel() {
        return projectFileModel;
    }
}
