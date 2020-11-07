package com.github.appreciated.designer.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CustomSelect<T> extends Composite<HorizontalLayout> {

    private final HorizontalLayout layout;
    private final HorizontalLayout renderer;
    private final Select<T> select;
    private BiFunction<Optional<T>, Consumer<T>, ? extends Component> componentRenderer;
    private Consumer<T> listener;

    public CustomSelect() {
        layout = getContent();
        layout.getStyle().set("position", "relative");
        select = new Select<>();
        select.getStyle().set("--lumo-body-text-color", "transparent");
        layout.add(select);
        renderer = new HorizontalLayout();
        renderer.getStyle().set("position", "absolute");
        renderer.setWidthFull();
        renderer.getStyle()
                .set("left", "0px")
                .set("right", "35px");
        renderer.setWidth("unset");
        renderer.getStyle().set("overflow", "hidden");
        layout.add(renderer);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setMargin(false);
        select.addValueChangeListener(changeEvent -> {
            renderer.removeAll();
            renderer.add(componentRenderer.apply(Optional.ofNullable(changeEvent.getValue()), listener));
            listener.accept(changeEvent.getValue());
        });
    }

    public void setItemLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator) {
        select.setItemLabelGenerator(itemLabelGenerator);
    }

    public void setInputRenderer(BiFunction<Optional<T>, Consumer<T>, ? extends Component> componentRenderer) {
        this.componentRenderer = componentRenderer;
        renderer.add(componentRenderer.apply(Optional.ofNullable(select.getValue()), this.listener));
    }

    public void setItems(T... items) {
        select.setItems(items);
    }

    public void setValueChangeListener(Consumer<T> listener) {
        this.listener = listener;
    }
}
