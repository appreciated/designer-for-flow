package com.github.appreciated.designer.view;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * sdfgsdfg
 */
public class BaseView extends VerticalLayout {
    public BaseView(String name) {
        add(new H3(name));
        setSizeFull();
        setMargin(false);
        setPadding(true);
    }
}
