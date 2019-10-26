package com.github.appreciated.designer.application.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public class TestDesign2 extends HorizontalLayout {

    public TestDesign2() {
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
    }
}
