package com.github.appreciated.designer.application.presenter.file.designer.template.drophandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;
import lombok.Data;

@Data
public abstract class DropHandler {

    private DesignerPresenter presenter;

    public DropHandler(DesignerPresenter presenter) {
        this.presenter = presenter;
    }

    public abstract boolean canHandleDropEvent(DesignerComponentWrapper draggedComponent, Component targetComponent);

    public abstract void handleDropEvent(DesignerComponentWrapper draggedComponent, Component targetComponent);
}
