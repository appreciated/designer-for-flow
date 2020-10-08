package com.github.appreciated.designer.helper;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentComparer {

    public static boolean compare(Component component1, Component component2) {
        if (component1 instanceof DesignerComponentWrapper) {
            component1 = ((DesignerComponentWrapper) component1).getActualComponent();
        }

        if (ComponentContainerHelper.isComponentContainer(component1, false) && ComponentContainerHelper.isComponentContainer(component2, false)) {
            if (component1.getClass() != component2.getClass()) {
                return false;
            }
            List<Component> children1 = ComponentContainerHelper.getChildren(component1).collect(Collectors.toList());
            List<Component> children2 = ComponentContainerHelper.getChildren(component2).collect(Collectors.toList());
            if (children1.size() == children2.size()) {
                for (int i = 0; i < children1.size(); i++) {
                    if (!compare(children1.get(i), children2.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        } else if (!ComponentContainerHelper.isComponentContainer(component1, false) && !ComponentContainerHelper.isComponentContainer(component2, false)) {
            return component1.getClass() == component2.getClass();
        }
        return false;
    }
}
