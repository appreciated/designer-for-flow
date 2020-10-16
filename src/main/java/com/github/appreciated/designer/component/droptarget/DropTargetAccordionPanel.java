package com.github.appreciated.designer.component.droptarget;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.accordion.AccordionPanel;

public class DropTargetAccordionPanel extends AccordionPanel implements HasStyle, DropTargetComponent {
    public DropTargetAccordionPanel() {
        setClassName("drop-target-div");
    }
}
