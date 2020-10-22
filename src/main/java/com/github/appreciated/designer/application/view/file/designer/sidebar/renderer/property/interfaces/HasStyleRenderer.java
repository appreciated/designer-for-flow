package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.github.appreciated.designer.component.designer.StyleEditor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;

import java.util.stream.Stream;

public class HasStyleRenderer extends AbstractComponentPropertyRenderer<HasStyle> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof HasStyle;
    }

    @Override
    public Stream<RenderPair> render(HasStyle component) {
        return Stream.of(
                new RenderPair("styles", new StyleEditor(component.getStyle()))
        );
    }

}
