package com.github.appreciated.designer.application.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public class TestDesign extends VerticalLayout {

    public TestDesign() {
        component0.setWidth("");
        component0.setHeight("");
        component2.setWidth("");
        component2.setHeight("");
        component3.setWidth("");
        component3.setHeight("");
        component4.setWidth("");
        component4.setHeight("");
        component5.setWidth("");
        component5.setHeight("");
        this.add(component0, component1, component2, component3, component4, component5);
        this.setWidth("100%");
        this.setHeight("100%");
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

    TextField component1 = new TextField();

    public TextField getComponent1() {
        return component1;
    }

    public void setComponent1(TextField component1) {
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

    Button component4 = new Button();

    public Button getComponent4() {
        return component4;
    }

    public void setComponent4(Button component4) {
        this.component4 = component4;
    }

    Button component5 = new Button();

    public Button getComponent5() {
        return component5;
    }

    public void setComponent5(Button component5) {
        this.component5 = component5;
    }
}
