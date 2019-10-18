package com.github.appreciated.designer.backup;

import com.github.appreciated.designer.controller.AdvancedTestDesignController;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class AdvancedTestDesign extends HorizontalLayout {

    AdvancedTestDesignController controller = new AdvancedTestDesignController();

    public AdvancedTestDesign() {
        this.setAlignItems(Alignment.STRETCH);
        this.setJustifyContentMode(JustifyContentMode.START);
        //controller.setView(this);
    }
}
