package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.ThemableLayout;

import java.util.stream.Stream;

public class ThemeableLayoutRenderer extends AbstractPropertyRenderer<ThemableLayout> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof ThemableLayout;
    }

    @Override
    public Stream<RenderPair> render(ThemableLayout component) {
        Checkbox margin = new Checkbox();
        Checkbox padding = new Checkbox();
        Checkbox spacing = new Checkbox();
        margin.setValue(component.isMargin());
        padding.setValue(component.isPadding());
        spacing.setValue(component.isSpacing());
        margin.addValueChangeListener(event -> component.setMargin(event.getValue()));
        padding.addValueChangeListener(event -> component.setPadding(event.getValue()));
        spacing.addValueChangeListener(event -> component.setSpacing(event.getValue()));
        return Stream.of(
                new RenderPair("margin", margin),
                new RenderPair("padding", padding),
                new RenderPair("spacing", spacing)
        );
    }

    @Override
    public Stream<String> rendersCssStyle() {
        return super.rendersCssStyle();
    }

    @Override
    public void applyValue(ThemableLayout propertyParent) {

    }
}
