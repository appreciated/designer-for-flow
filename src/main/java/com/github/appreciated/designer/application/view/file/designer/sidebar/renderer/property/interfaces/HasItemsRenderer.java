package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.data.provider.Query;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HasItemsRenderer extends AbstractPropertyRenderer<HasItems> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof HasItems;
    }

    @Override
    public void applyValue(HasItems propertyParent) {

    }

    @Override
    public Stream<RenderPair> render(HasItems propertyParent) {
        MultiselectComboBox<String> multiselectComboBox = new MultiselectComboBox();
        multiselectComboBox.setWidth("100%");
        multiselectComboBox.setPlaceholder("Add some items");
// custom values handler to process the added value(s)
        multiselectComboBox.addCustomValuesSetListener(e -> {
            LinkedHashSet<String> value = new LinkedHashSet<>(multiselectComboBox.getValue().stream().collect(Collectors.toSet()));
            value.add(e.getDetail());
            List<String> updatedItems = multiselectComboBox.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
            updatedItems.add(e.getDetail());
            multiselectComboBox.setItems(updatedItems);
            multiselectComboBox.setValue(value);
        });
        multiselectComboBox.addValueChangeListener(event -> propertyParent.setItems(event.getValue()));
        return Stream.of(new RenderPair("items", multiselectComboBox));
    }


}
