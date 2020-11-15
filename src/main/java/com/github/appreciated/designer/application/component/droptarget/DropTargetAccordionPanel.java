package com.github.appreciated.designer.application.component.droptarget;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.accordion.AccordionPanel;

public class DropTargetAccordionPanel extends AccordionPanel implements HasStyle, DropTargetComponent {
    public DropTargetAccordionPanel() {
        setClassName("drop-target-div");
    }
}
