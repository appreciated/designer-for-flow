package com.github.appreciated.designer.application.presenter.file.designer.template.draghandler;

import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;


public class AccordionPanelDragHandler extends DragHandler {

    public AccordionPanelDragHandler(DesignerPresenter presenter) {
        super(presenter);
    }

    public boolean canHandleDragTargetEvent(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        return draggedComponent.getActualComponent() instanceof AccordionPanel || getPresenter().unwrapDesignerComponent(targetComponent) instanceof Accordion;
    }

    @Override
    public boolean showDropTargetInComponent(DesignerComponentWrapper draggedComponent, Component possibleTargetComponent) {
        return draggedComponent.getActualComponent() instanceof AccordionPanel && getPresenter().unwrapDesignerComponent(possibleTargetComponent) instanceof Accordion;
    }
}