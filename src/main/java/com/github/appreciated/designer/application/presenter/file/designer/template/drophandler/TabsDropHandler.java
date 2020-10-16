package com.github.appreciated.designer.application.presenter.file.designer.template.drophandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.helper.ComponentContainerHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.Optional;

public class TabsDropHandler extends DropHandler {

    public TabsDropHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    @Override
    public boolean canHandleDropEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        return draggedComponent.getActualComponent() instanceof Tab;
    }

    @Override
    public void handleDropEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        if (targetComponent.getParent().get() instanceof Tabs) {
            Optional<Component> parent = targetComponent.getParent();
            parent.ifPresent(component -> ComponentContainerHelper.addComponent(component, draggedComponent.getActualComponent()));
        } else {
            Notification.show("Nope!");
        }
    }
}
