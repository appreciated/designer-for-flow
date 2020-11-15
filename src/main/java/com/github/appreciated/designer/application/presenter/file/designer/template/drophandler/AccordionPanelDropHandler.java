package com.github.appreciated.designer.application.presenter.file.designer.template.drophandler;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.helper.ComponentContainerHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.notification.Notification;

import java.util.Optional;

public class AccordionPanelDropHandler extends DropHandler {

    public AccordionPanelDropHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    @Override
    public boolean canHandleDropEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        return draggedComponent.getActualComponent() instanceof AccordionPanel;
    }

    @Override
    public void handleDropEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        if (targetComponent instanceof AccordionPanel) {
            Optional<Component> parent = targetComponent.getParent();
            parent.ifPresent(component -> ComponentContainerHelper.addComponent(component, draggedComponent.getActualComponent()));
        } else {
            Notification.show("Nope!");
        }
    }
}
