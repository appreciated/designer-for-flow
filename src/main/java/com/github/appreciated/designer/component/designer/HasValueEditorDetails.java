package com.github.appreciated.designer.component.designer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class HasValueEditorDetails<T extends HasValueEditorDetails> extends EditorDetails implements HasSize {

    private final HasOrderedComponents components;
    private final List<Component> fields = new ArrayList<>();
    private final boolean editable;
    private Button addButton = null;

    public HasValueEditorDetails(String title, HasOrderedComponents components, boolean editable) {
        super(title);
        this.components = components;
        setSummaryText(title);
        VerticalLayout wrapper = new VerticalLayout((Component) components);
        setContent(wrapper);
        getContent().findFirst().ifPresent(component ->
                component.getElement().getStyle()
                        .set("--lumo-size-m", "22px")
                        .set("--lumo-font-size-m", "12px")
                        .set("--lumo-space-m", "0px")
                        .set("--lumo-space-xs", "0px")
        );
        setWidthFull();
        this.editable = editable;
        if (editable) {
            addButton = new Button("Add", VaadinIcon.PLUS.create());
            addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addButton.setWidthFull();
            wrapper.add(addButton);
        }
    }

    public T withFormField(String caption, Component field) {
        return withFormField(caption, field, null);
    }

    public T withFormField(String caption, Component field, Consumer<Boolean> onRemoveListener) {
        Label label = null;
        if (caption != null) {
            label = new Label(caption);
            label.getStyle()
                    .set("flex-basis", "125px")
                    .set("font-size", "13px")
                    .set("white-space", "nowrap")
                    .set("text-overflow", "ellipsis")
                    .set("overflow", "hidden")
                    .set("text-align", "right");
        }
        return withFormField(label, field, onRemoveListener);
    }

    public T withFormField(Component label, Component field, Consumer<Boolean> onRemoveListener) {
        fields.add(field);
        Button deleteButton = new Button();
        deleteButton.addClickListener(buttonClickEvent -> {
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        deleteButton.getStyle().set("--lumo-primary-text-color", "var(--lumo-body-text-color)");
        deleteButton.setIcon(VaadinIcon.CLOSE_SMALL.create());
        HorizontalLayout formWrapper = new HorizontalLayout();
        if (label != null) {
            formWrapper.add(label);
        }
        formWrapper.add(field);
        formWrapper.getElement().getStyle().set("--lumo-space-m", "5px");
        if (editable) {
            formWrapper.add(deleteButton);
            deleteButton.addClickListener(event -> {
                onRemoveListener.accept(true);
                components.remove(formWrapper);
                fields.remove(field);
            });
        }
        formWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        formWrapper.setFlexGrow(1, field);
        formWrapper.setWidthFull();
        add(formWrapper);
        return (T) this;
    }

    public void add(Component component) {
        components.add(component);
    }

    public void setOnAddListener(Consumer<Boolean> onAddListener) {
        addButton.addClickListener(event -> onAddListener.accept(true));
    }

    public Stream<Component> getComponents() {
        return fields.stream();
    }
}
