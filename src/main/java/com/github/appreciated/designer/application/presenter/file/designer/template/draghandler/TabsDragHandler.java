package com.github.appreciated.designer.application.presenter.file.designer.template.draghandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tabs;


public class TabsDragHandler extends DragHandler {

    public TabsDragHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    public boolean canHandleDragTargetEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        return getPresenter().unwrapDesignerComponent(targetComponent) instanceof Tabs;
    }

    @Override
    public boolean showDropTargetInComponent(DesignerComponentWrapper draggedComponent, Component possibleTargetComponent) {
        return getPresenter().unwrapDesignerComponent(possibleTargetComponent) instanceof Tabs;
    }
}