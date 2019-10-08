package com.designer.test;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public class TestDesign extends VerticalLayout {

    public TestDesign() {
        this.add(component0, component1, component2, component3);
        this.setWidth("100%");
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
    }

    Button component0 = new Button();

    public Button getComponent0() {
        return component0;
    }

    public void setComponent0(Button component0) {
        this.component0 = component0;
    }

    Button component1 = new Button();

    public Button getComponent1() {
        return component1;
    }

    public void setComponent1(Button component1) {
        this.component1 = component1;
    }

    Button component2 = new Button();

    public Button getComponent2() {
        return component2;
    }

    public void setComponent2(Button component2) {
        this.component2 = component2;
    }

    Button component3 = new Button();

    public Button getComponent3() {
        return component3;
    }

    public void setComponent3(Button component3) {
        this.component3 = component3;
    }
}
