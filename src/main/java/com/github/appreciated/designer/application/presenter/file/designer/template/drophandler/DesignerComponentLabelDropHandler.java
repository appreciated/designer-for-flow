package com.github.appreciated.designer.application.presenter.file.designer.template.drophandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.component.DropTargetDiv;
import com.github.appreciated.designer.component.designer.DesignerComponentLabel;
import com.github.appreciated.designer.helper.ComponentContainerHelper;
import com.vaadin.flow.component.Component;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.addComponent;
import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;

public class DesignerComponentLabelDropHandler extends DropHandler {

    public DesignerComponentLabelDropHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    @Override
    public boolean canHandleDropEvent(Component draggedComponent, Component targetComponent) {
        return draggedComponent instanceof DesignerComponentLabel;
    }

    @Override
    public void handleDropEvent(Component draggedComponent, Component targetComponent) {

        Component transformedComponent = getPresenter().transformDesignerComponentLabel(draggedComponent);
        // Anyways the element will be added to the drop source.
        if (targetComponent instanceof DesignerComponentWrapper || targetComponent instanceof DropTargetDiv) {
            targetComponent.getParent().ifPresent(component1 -> {
                if (isComponentContainer(component1)) {
                    ComponentContainerHelper.addComponentAtIndex(component1, ComponentContainerHelper.indexOf(component1, targetComponent), transformedComponent);
                }
            });
        } else if (isComponentContainer(targetComponent)) {
            addComponent(targetComponent, transformedComponent);
            transformedComponent.getElement().executeJs("focus()");
        }

    }
}
