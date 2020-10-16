package com.github.appreciated.designer.application.presenter.file.designer.template.draghandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;
import lombok.Data;

@Data
public abstract class DragHandler {

    private DesignerPresenter presenter;

    public DragHandler(DesignerPresenter presenter) {
        this.presenter = presenter;
    }

    public abstract boolean canHandleDragTargetEvent(DesignerComponentWrapper draggedComponent, Component targetComponent);

    public abstract boolean showDropTargetInComponent(DesignerComponentWrapper draggedComponent, Component possibleTargetComponent);

}
