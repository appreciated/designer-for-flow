package com.github.appreciated.designer.reflection.renderer.property.interfaces;

import com.github.appreciated.designer.component.designer.StyleEditorDetails;
import com.github.appreciated.designer.reflection.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.reflection.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;

import java.util.stream.Stream;

public class HasStyleInterfaceRenderer extends AbstractPropertyRenderer<HasStyle> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof HasStyle;
    }

    @Override
    public Stream<RenderPair> render(HasStyle component) {
        return Stream.of(
                new RenderPair("styles", new StyleEditorDetails(component.getStyle()))
        );
    }

    @Override
    public void applyValue(HasStyle propertyParent) {

    }
}
