package com.github.appreciated.designer.application.presenter.file.designer.template.drophandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.component.designer.DesignerComponentLabel;
import com.github.appreciated.designer.helper.ComponentContainerHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DropEvent;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.addComponent;
import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;

public class DesignerComponentLabelDropHandler extends DropHandler {

    public DesignerComponentLabelDropHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    @Override
    public boolean canHandleDropEvent(DropEvent<Component> event) {
        return event.getDragSourceComponent().isPresent() && event.getDragSourceComponent().get() instanceof DesignerComponentLabel;
    }

    @Override
    public void handleDropEvent(DropEvent<Component> event) {
        event.getDragSourceComponent().ifPresent(component -> {
            Component transformedComponent = getPresenter().transformDesignerComponentLabel(component);
            // Anyways the element will be added to the drop source.
            if (event.getComponent() instanceof DesignerComponentWrapper) {
                event.getComponent().getParent().ifPresent(component1 -> {
                    if (isComponentContainer(component1)) {
                        ComponentContainerHelper.addComponentAtIndex(component1, ComponentContainerHelper.indexOf(component1, event.getComponent()), transformedComponent);
                    }
                });
            } else if (isComponentContainer(event.getComponent())) {
                addComponent(event.getComponent(), transformedComponent);
                transformedComponent.getElement().executeJs("focus()");
            }
        });
    }
}
