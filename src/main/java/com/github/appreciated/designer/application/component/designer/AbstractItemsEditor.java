package com.github.appreciated.designer.application.component.designer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractItemsEditor<T, V extends HasValueEditorDetails> extends HasValueEditorDetails<V> {
    private final Consumer<List<T>> valueChangeListener;
    HashMap<Component, Binder<AtomicReference<T>>> binderMap = new HashMap<>();

    public AbstractItemsEditor(String title, Stream<T> items, Consumer<List<T>> valueChangeListener) {
        super(title, new VerticalLayout(), true);
        this.valueChangeListener = valueChangeListener;
        items.forEach(value -> withFormField(getFieldForValue(value), aBoolean -> addNewField(value)));
        setOnAddListener(aBoolean -> addNewField(getNewValue()));
    }

    Component getFieldForValue(T value) {
        AtomicReference<T> reference = new AtomicReference<>(value);
        Binder<AtomicReference<T>> binder = new Binder<>();
        binder.setBean(reference);
        Component component = getComponent(binder);
        binderMap.put(component, binder);
        return component;
    }

    void addNewField(T value) {
        withFormField(getFieldForValue(value), aBoolean -> updateValues());
        updateValues();
    }

    abstract Component getComponent(Binder<AtomicReference<T>> binder);

    public void updateValues() {
        valueChangeListener.accept(getComponents()
                .map(component -> binderMap.get(component))
                .map(atomicReferenceBinder -> atomicReferenceBinder.getBean().get())
                .collect(Collectors.toList())
        );
    }

    abstract T getNewValue();
}