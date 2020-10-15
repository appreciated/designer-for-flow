package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import java.util.stream.Stream;

public class FlexComponentRenderer extends AbstractPropertyRenderer<FlexComponent> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof FlexComponent;
    }

    @Override
    public Stream<RenderPair> render(FlexComponent component) {
        ComboBox<FlexComponent.Alignment> alignmentComponent = new ComboBox<>();
        alignmentComponent.setItems(FlexComponent.Alignment.values());
        setValueButNull(alignmentComponent, component.getAlignItems());
        alignmentComponent.addValueChangeListener(event -> component.setAlignItems(event.getValue()));

        ComboBox<FlexComponent.JustifyContentMode> justifyContentComponent = new ComboBox<>();
        justifyContentComponent.setItems(FlexComponent.JustifyContentMode.values());
        setValueButNull(justifyContentComponent, component.getJustifyContentMode());
        justifyContentComponent.addValueChangeListener(event -> component.setJustifyContentMode(event.getValue()));

        return Stream.of(
                new RenderPair("alignment", alignmentComponent),
                new RenderPair("justifyContent", justifyContentComponent)
        );
    }

    @Override
    public Stream<String> rendersCssStyle() {
        return super.rendersCssStyle();
    }

    @Override
    public void applyValue(FlexComponent propertyParent) {

    }
}
