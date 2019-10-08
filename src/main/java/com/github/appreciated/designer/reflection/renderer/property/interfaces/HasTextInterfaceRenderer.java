package com.github.appreciated.designer.reflection.renderer.property.interfaces;

import com.github.appreciated.designer.reflection.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.reflection.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.stream.Stream;

public class HasTextInterfaceRenderer extends AbstractPropertyRenderer<HasText> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof HasText;
    }

    @Override
    public Stream<RenderPair> render(HasText component) {
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        Binder<HasText> textBinder = new Binder<>();
        textBinder.setBean(component);
        textBinder.forField(textField).bind(HasText::getText, HasText::setText);
        return Stream.of(new RenderPair("text", textField));
    }

    @Override
    public void applyValue(HasText propertyParent) {

    }
}
