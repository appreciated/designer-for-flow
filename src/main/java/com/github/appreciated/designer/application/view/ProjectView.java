package com.github.appreciated.designer.application.view;

import com.github.appreciated.designer.component.AddButton;
import com.github.appreciated.designer.component.IconButton;
import com.github.appreciated.designer.component.ironpages.IronPages;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

@StyleSheet("frontend://styles/styles.css")
public class ProjectView extends VerticalLayout {

    private final Tabs tabs;
    private final IronPages content;
    private final AddButton dial;
    HorizontalLayout appBar = new HorizontalLayout();

    public ProjectView() {
        tabs = new Tabs();
        dial = new AddButton(VaadinIcon.PLUS.create());
        dial.setBottom("30px");
        Image logo = new Image("./frontend/styles/images/logo-floating-low.png", "logo");
        logo.getStyle().set("padding", "4px");
        logo.setHeight("48px");
        Label label = new Label("Designer for Flow");
        label.getStyle()
                .set("white-space", "nowrap")
                .set("line-height", "56px");
        appBar.add(logo, label, tabs);
        appBar.getStyle()
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("z-index", "1");
        appBar.setWidthFull();
        tabs.getStyle().set("--lumo-size-s", "var(--lumo-size-xl)");
        add(appBar);
        content = new IronPages();
        content.setSizeFull();
        add(content);
        tabs.setWidthFull();
        setMargin(false);
        setPadding(false);
        setSpacing(false);
        add(dial);
        setSizeFull();
    }

    public Tab addTab(String name, ComponentEventListener<ClickEvent<Button>> clickListener) {
        if (name != null) {
            Tab tab = new Tab();
            tab.add(new Label(name),
                    new IconButton(VaadinIcon.CLOSE_SMALL.create(), clickListener)
            );
            tabs.add(tab);
            tabs.setSelectedTab(tab);
            return tab;
        }
        return null;
    }

    public Tabs getTabs() {
        return tabs;
    }

    public AddButton getDial() {
        return dial;
    }

    public IronPages getContent() {
        return content;
    }
}
