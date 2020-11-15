package com.github.appreciated.designer.application.view.file.designer;

import com.github.appreciated.designer.application.component.ironpages.IronPages;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class SideBarView extends VerticalLayout {
    private final Tabs tabs;
    private final IronPages content;
    private final Tab editorTab;
    private final Tab themeTab;

    private final SplitLayout propertiesAndStructure = new SplitLayout();

    public SideBarView() {
        propertiesAndStructure.setOrientation(SplitLayout.Orientation.VERTICAL);
        propertiesAndStructure.setSizeFull();

        tabs = new Tabs();
        editorTab = new Tab(VaadinIcon.EDIT.create(), new Span(getTranslation("editor")));
        themeTab = new Tab(VaadinIcon.PAINTBRUSH.create(), new Span(getTranslation("theme")));
        tabs.add(editorTab, themeTab);
        add(tabs);

        content = new IronPages();
        content.setSizeFull();
        content.add(propertiesAndStructure);
        add(content);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(selectedChangeEvent -> content.setSelected(tabs.getSelectedIndex()));
        tabs.setSelectedTab(editorTab);

        setMargin(false);
        setPadding(false);
        setSpacing(false);
    }

    public Tabs getTabs() {
        return tabs;
    }

    public Tab getEditorTab() {
        return editorTab;
    }

    public Tab getThemeTab() {
        return themeTab;
    }

    public SplitLayout getPropertiesAndStructure() {
        return propertiesAndStructure;
    }

    public IronPages getContent() {
        return content;
    }
}
