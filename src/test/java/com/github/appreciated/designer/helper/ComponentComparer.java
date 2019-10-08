package com.github.appreciated.designer.helper;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentComparer {

    public static boolean compare(Component component1, Component component2) {
        if (component1 instanceof DesignerComponentWrapper) {
            component1 = ((DesignerComponentWrapper) component1).getActualComponent();
        }

        if (component1 instanceof HasComponents && component2 instanceof HasComponents) {
            if (component1.getClass() != component2.getClass()) {
                return false;
            }
            List<Component> children1 = component1.getChildren().collect(Collectors.toList());
            List<Component> children2 = component2.getChildren().collect(Collectors.toList());
            if (children1.size() == children2.size()) {
                for (int i = 0; i < children1.size(); i++) {
                    if (!compare(children1.get(i), children2.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        } else if (!(component1 instanceof HasComponents) && !(component2 instanceof HasComponents)) {
            return component1.getClass() == component2.getClass();
        }
        return false;
    }
}
