package com.github.appreciated.designer.application.presenter.file.designer.template.draghandler;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;


public class TabsDragHandler extends DragHandler {

    public TabsDragHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    public boolean canHandleDragTargetEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        return draggedComponent.getActualComponent() instanceof Tab && getPresenter().unwrapDesignerComponent(targetComponent) instanceof Tabs;
    }

    @Override
    public boolean showDropTargetInComponent(DesignerComponentWrapper draggedComponent, Component possibleTargetComponent) {
        return getPresenter().unwrapDesignerComponent(possibleTargetComponent) instanceof Tabs;
    }
}