package com.github.appreciated.designer.reflection.renderer.type;

import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPropertyRenderer {

    private ProjectService service;

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

    public void setProjectService(ProjectService service) {
        this.service = service;
    }

    public ProjectService getService() {
        return service;
    }
}
