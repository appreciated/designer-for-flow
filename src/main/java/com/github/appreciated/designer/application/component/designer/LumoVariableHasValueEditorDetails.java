package com.github.appreciated.designer.application.component.designer;

import com.github.appreciated.designer.application.service.EventService;
import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.model.LumoVariables;
import com.github.appreciated.designer.theme.css.Theme;
import com.github.juchar.colorpicker.ColorPickerFieldRaw;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public abstract class LumoVariableHasValueEditorDetails<T extends LumoVariableHasValueEditorDetails> extends HasValueEditorDetails<T> {

    private final EventService eventService;

    public LumoVariableHasValueEditorDetails(String title, HasOrderedComponents components, EventService eventService, boolean editable) {
        super(title, components, editable);
        this.eventService = eventService;
    }

    public T withFormField(LumoVariables variable, Theme parser) {
        return withFormField(variable.getVariableName().substring(7), variable, parser);
    }

    public T withFormField(String caption, LumoVariables variable, Theme theme) {
        HasValue<HasValue.ValueChangeEvent<String>, String> field = getField();
        if (((Component) field) instanceof TextField) {
            TextField tf = (TextField) (Component) field;
            tf.setPlaceholder(variable.getDefaultValue());
            tf.setClearButtonVisible(true);
        }
        if (field instanceof ColorPickerFieldRaw) {
            ColorPickerFieldRaw tf = (ColorPickerFieldRaw) field;
            tf.getTextField().setPlaceholder(variable.getDefaultValue());
        }

        getBinderForCssVariable(variable, theme)
                .forField(field)
                .bind(CssVariable::getValue, CssVariable::setValue);
        return super.withFormField(caption, (Component) field);
    }

    public abstract HasValue getField();

    public Binder<CssVariable> getBinderForCssVariable(LumoVariables variables, Theme theme) {
        if (!theme.getStyles().containsKey(variables)) {
            theme.getStyles().put(variables, new CssVariable(variables, null));
        }
        Binder<CssVariable> binder = new Binder<>();
        binder.setBean(theme.getStyles().get(variables));
        binder.addValueChangeListener(valueChangeEvent -> {
            getEventService().getThemeChangedEventPublisher().publish(binder.getBean());
            theme.save();
        });
        return binder;
    }

    public EventService getEventService() {
        return eventService;
    }
}
