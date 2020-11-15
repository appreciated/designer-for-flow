package com.github.appreciated.designer.application.component.ironpages;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Tag("iron-pages")
@NpmPackage(value = "@polymer/iron-pages", version = "3.0.1")
@JsModule("@polymer/iron-pages/iron-pages.js")
public class IronPages extends Component implements HasComponents, HasSize {

    public IronPages() {
        setSelected(0);
    }

    public void setSelected(int index) {
        getElement().setAttribute("selected", String.valueOf(index));
    }

    public void setSelected(Component component) {
        List<Component> children = getChildren().collect(Collectors.toList());
        IntStream.range(0, getElement().getChildCount())
                .filter(i -> component == children.get(i))
                .findFirst()
                .ifPresent(this::setSelected);
    }

}
