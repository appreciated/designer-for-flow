package com.github.appreciated.designer.component;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.Locale;
import java.util.stream.Stream;

@NpmPackage(value = "country-flag-icons", version = "1.2.5")
public class LanguageSelect extends Select<Locale> {

    public LanguageSelect(Stream<Locale> items) {
        super(items.toArray(Locale[]::new));
        setRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout layout = new HorizontalLayout();
            Image languageFlag = new Image("img/languageflags/" + item.getLanguage() + ".svg", "flag for " + item.getLanguage());
            languageFlag.setHeight("20px");
            layout.add(languageFlag);
            layout.add(new Span(item.getDisplayLanguage()));
            layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            return layout;
        }));
    }
}
