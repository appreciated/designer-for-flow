package com.github.appreciated.designer.component.designer;

import com.github.appreciated.designer.theme.css.style.CssStyle;
import com.github.appreciated.designer.theme.css.style.CssStyles;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StyleEditorDetails extends HasValueEditorDetails<StyleEditorDetails> {
    private final Set<CssStyle> styles = new HashSet<>();
    private final Style style;

    public StyleEditorDetails(Style style) {
        super("styles", new VerticalLayout(), true);
        this.style = style;
        updateRenderedStyles();
        setOnAddListener(aBoolean -> {
            CssStyle newStyle = new CssStyle();
            addStyleField(newStyle, aBoolean1 -> style.remove(newStyle.getName()));
        });
    }

    public void updateRenderedStyles() {
        List<String> names = style.getNames().collect(Collectors.toList());
        names.forEach(s -> styles.add(new CssStyle(s, style.get(s))));
        styles.forEach(styleValue -> addStyleField(styleValue, aBoolean -> style.remove(styleValue.getName())));
    }

    private void addStyleField(CssStyle styleValue, Consumer<Boolean> onRemoveListener) {
        Binder<CssStyle> binder = new Binder<>();
        binder.setBean(styleValue);
        // propertyName
        ComboBox<String> propertyName = new ComboBox<>();
        propertyName.getStyle().set("min-width", "80px");
        propertyName.addValueChangeListener(event -> {
            if (event.getValue() != null && event.getValue().length() != 0) {
                propertyName.getStyle().set("--lumo-contrast-10pct", "transparent");
            } else {
                propertyName.getStyle().remove("--lumo-contrast-10pct");
            }
            if (event.getOldValue() != null && !event.getOldValue().equals(event.getValue())) {
                style.remove(event.getOldValue());
            }
            style.set(event.getValue(), styleValue.getValue());
        });
        propertyName.setItems(CssStyles.getInstance().getStyles().keySet());
        propertyName.setAllowCustomValue(true);
        propertyName.getElement().setAttribute("theme", TextFieldVariant.LUMO_ALIGN_RIGHT.getVariantName());
        propertyName.setRenderer(new ComponentRenderer<>(item -> {
            Label artist = new Label(item);
            artist.getStyle().set("fontSize", "smaller");
            return artist;
        }));
        // propertyValue
        TextField propertyValue = new TextField();
        propertyValue.setValueChangeMode(ValueChangeMode.EAGER);
        propertyValue.addValueChangeListener(event -> style.set(styleValue.getName(), event.getValue()));
        propertyValue.getStyle().set("min-width", "50px");
        //
        binder.forField(propertyName).bind(CssStyle::getName, CssStyle::setName);
        binder.forField(propertyValue).bind(CssStyle::getValue, CssStyle::setValue);
        //
        withFormField(propertyName, propertyValue, onRemoveListener);
    }

    public void removeRenderedStyles(Stream<String> styleNames) {
        styleNames.forEach(s -> styles.stream()
                .filter(styleEntry -> styleEntry.getName().equals(s))
                .findFirst()
                .ifPresent(styleEntry -> styles.remove(styleEntry))
        );
    }

}
