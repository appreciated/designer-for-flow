package com.github.appreciated.designer.helper;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TestComponents {
    public static VerticalLayout getTestView() {
        VerticalLayout testView = new VerticalLayout();

        Button component0 = new Button();
        Button component1 = new Button();
        Button component2 = new Button();
        Button component3 = new Button();

        testView.add(component0, component1, component2, component3);
        testView.setWidth("100%");
        testView.setAlignItems(FlexComponent.Alignment.STRETCH);
        testView.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        return testView;
    }
}
