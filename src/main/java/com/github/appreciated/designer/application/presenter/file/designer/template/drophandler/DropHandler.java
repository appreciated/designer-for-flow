package com.github.appreciated.designer.application.presenter.file.designer.template.drophandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.vaadin.flow.component.Component;
import lombok.Data;

@Data
public abstract class DropHandler {

    private DesignerPresenter presenter;

    public DropHandler(DesignerPresenter presenter) {
        this.presenter = presenter;
    }

    public abstract boolean canHandleDropEvent(Component draggedComponent, Component targetComponent);

    public abstract void handleDropEvent(Component draggedComponent, Component targetComponent);
}
