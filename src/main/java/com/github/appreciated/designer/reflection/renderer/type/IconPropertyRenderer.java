package com.github.appreciated.designer.reflection.renderer.type;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.beans.PropertyDescriptor;

public class IconPropertyRenderer extends AbstractPropertyRenderer {

    @Override
    public boolean canRender(Component propertyParent, String propertyName, PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getPropertyType() == Component.class && propertyName.equals("icon");
    }

    public Component render(String propertyName, PropertyDescriptor propertyDescriptor, Component propertyParent) {
        ComboBox<VaadinIcon> combobox = new ComboBox<>();
        combobox.setRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout container = new HorizontalLayout();
            container.add(item.create(), new Label(item.name()));
            return container;
        }));
        combobox.setItems(VaadinIcon.values());
        combobox.addValueChangeListener(comboBoxVaadinIconComponentValueChangeEvent -> {
            applyValue(propertyParent, propertyDescriptor, comboBoxVaadinIconComponentValueChangeEvent.getValue().create());
        });
        return combobox;
    }


}
