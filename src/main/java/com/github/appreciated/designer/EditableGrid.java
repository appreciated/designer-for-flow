package com.github.appreciated.designer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

public class EditableGrid<T> extends Grid<T> {

    private final Binder<T> binder;
    private final Editor<T> editor;

    public EditableGrid() {
        binder = new Binder<>();
        editor = getEditor();
        editor.setBinder(binder);

        /*emailField.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().closeEditor())
                .setFilter("event.key === 'Tab' && !event.shiftKey");*/

        addItemDoubleClickListener(event -> {
            getEditor().editItem(event.getItem());
            //field.focus();
        });

        binder.addValueChangeListener(event -> {
            getEditor().refresh();
        });

        addItemClickListener(event -> {
            if (binder.getBean() != null) {
                /*message.setText(binder.getBean().getfirstName() + ", "
                        + binder.getBean().isSubscriber() + ", "
                        + binder.getBean().getEmail());*/
            }
        });
    }

    public <V> void addEditorToColumn(Column<T> nameColumn, HasValue textField, ValueProvider<T, V> getter, Setter<T, V> setter) {
        binder.bind(textField, getter, setter);
        nameColumn.setEditorComponent((Component) textField);
    }

    public Registration addValueChangeListener(HasValue.ValueChangeListener<? super HasValue.ValueChangeEvent<?>> listener) {
        return binder.addValueChangeListener(listener);
    }

}
