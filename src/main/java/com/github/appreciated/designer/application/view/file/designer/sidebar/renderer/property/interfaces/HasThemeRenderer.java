package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces;

import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.treegrid.TreeGrid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class HasThemeRenderer extends AbstractPropertyRenderer<HasTheme> {

    private final HashMap<Object, String[]> map;

    public HasThemeRenderer() {
        map = new HashMap<>();
        map.put(Button.class, Arrays.stream(ButtonVariant.values()).map(ButtonVariant::getVariantName).toArray(String[]::new));
        map.put(TextField.class, Arrays.stream(TextFieldVariant.values()).map(TextFieldVariant::getVariantName).toArray(String[]::new));
        map.put(EmailField.class, Arrays.stream(TextFieldVariant.values()).map(TextFieldVariant::getVariantName).toArray(String[]::new));
        map.put(NumberField.class, Arrays.stream(TextFieldVariant.values()).map(TextFieldVariant::getVariantName).toArray(String[]::new));
        map.put(PasswordField.class, Arrays.stream(TextFieldVariant.values()).map(TextFieldVariant::getVariantName).toArray(String[]::new));
        map.put(RadioButtonGroup.class, Arrays.stream(RadioGroupVariant.values()).map(RadioGroupVariant::getVariantName).toArray(String[]::new));
        map.put(CheckboxGroup.class, Arrays.stream(CheckboxGroupVariant.values()).map(CheckboxGroupVariant::getVariantName).toArray(String[]::new));
        map.put(ProgressBar.class, Arrays.stream(ProgressBarVariant.values()).map(ProgressBarVariant::getVariantName).toArray(String[]::new));
        map.put(Grid.class, Arrays.stream(GridVariant.values()).map(GridVariant::getVariantName).toArray(String[]::new));
        map.put(TreeGrid.class, Arrays.stream(GridVariant.values()).map(GridVariant::getVariantName).toArray(String[]::new));
        /*
          TODO {@link com.vaadin.flow.component.select.Select}
         */
    }

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof HasTheme;
    }

    @Override
    public Stream<RenderPair> render(HasTheme component) {
        ComboBox<String> combobox = new ComboBox<>();
        combobox.setClearButtonVisible(true);
        if (map.containsKey(component.getClass())) {
            combobox.setItems(map.get(component.getClass()));
        }
        setValueButNull(combobox, component.getThemeName());
        combobox.addValueChangeListener(event -> {
            if (event.getOldValue() != null) {
                component.setThemeName(event.getOldValue(), false);
            }
            if (event.getValue() != null) {
                component.setThemeName(event.getValue());
            } else {
                component.removeThemeName(event.getOldValue());
            }
        });
        return Stream.of(new RenderPair("themes", combobox));
    }

    @Override
    public void applyValue(HasTheme propertyParent) {

    }
}
