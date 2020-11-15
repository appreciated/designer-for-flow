package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component;

import com.github.appreciated.designer.application.component.LanguageSelect;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.util.Locale;
import java.util.stream.Stream;

public class TimePickerRenderer extends AbstractComponentPropertyRenderer<TimePicker> {
    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof TimePicker;
    }

    @Override
    public Stream<RenderPair> render(TimePicker component) {
        LanguageSelect select = new LanguageSelect(TimePicker.getSupportedAvailableLocales());
        Locale property = component.getLocale();
        select.setValue(property);
        select.addValueChangeListener(event -> component.setLocale(event.getValue()));
        return Stream.of(new RenderPair("locale", select));
    }

    @Override
    public Stream<String> rendersProperty() {
        return Stream.of("src");
    }

}

