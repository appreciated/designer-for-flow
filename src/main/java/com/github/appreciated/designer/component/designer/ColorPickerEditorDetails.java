package com.github.appreciated.designer.component.designer;

import com.github.appreciated.designer.service.EventService;
import com.github.juchar.colorpicker.ColorPickerFieldRaw;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ColorPickerEditorDetails extends LumoVariableHasValueEditorDetails<ColorPickerEditorDetails> {

    public ColorPickerEditorDetails(String title, EventService eventService, boolean editable) {
        super(title, new VerticalLayout(), eventService, editable);

    }

    @Override
    public HasValue getField() {
        return new ColorPickerFieldRaw();
    }
}
