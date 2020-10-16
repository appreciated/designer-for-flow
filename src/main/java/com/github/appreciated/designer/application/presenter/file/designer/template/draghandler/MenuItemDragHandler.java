package com.github.appreciated.designer.application.presenter.file.designer.template.draghandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;


public class MenuItemDragHandler extends DragHandler {

    public MenuItemDragHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    public boolean canHandleDragTargetEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        return draggedComponent.getActualComponent() instanceof MenuItem || getPresenter().unwrapDesignerComponent(targetComponent) instanceof MenuBar;
    }

    @Override
    public boolean showDropTargetInComponent(DesignerComponentWrapper draggedComponent, Component possibleTargetComponent) {
        return draggedComponent.getActualComponent() instanceof MenuItem && getPresenter().unwrapDesignerComponent(possibleTargetComponent) instanceof MenuBar;
    }
}