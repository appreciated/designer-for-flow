package com.github.appreciated.designer.application.component.designer;

import com.github.appreciated.designer.application.service.EventService;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class TextFieldEditorDetails extends LumoVariableHasValueEditorDetails<TextFieldEditorDetails> {

    public TextFieldEditorDetails(String title, EventService eventService, boolean editable) {
        super(title, new VerticalLayout(), eventService, editable);
    }

    @Override
    public HasValue getField() {
        return new TextField();
    }
}
