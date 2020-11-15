package com.github.appreciated.designer.application.presenter.file.designer.template.draghandler;

import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.Scroller;


public class ScrollerDragHandler extends DragHandler {

    public ScrollerDragHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    public boolean canHandleDragTargetEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        return getPresenter().unwrapDesignerComponent(targetComponent) instanceof Scroller;
    }

    @Override
    public boolean showDropTargetInComponent(DesignerComponentWrapper draggedComponent, Component possibleTargetComponent) {
        boolean b = ((Scroller) getPresenter().unwrapDesignerComponent(possibleTargetComponent)).getContent() == null;
        return b;
    }
}