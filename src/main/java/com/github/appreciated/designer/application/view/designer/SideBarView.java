package com.github.appreciated.designer.application.view.designer;

import com.github.appreciated.designer.application.view.designer.sidebar.PropertiesView;
import com.github.appreciated.designer.application.view.designer.sidebar.StructureView;
import com.github.appreciated.designer.component.ironpages.IronPages;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class SideBarView extends VerticalLayout {
    private final Tabs tabs;
    private final IronPages content;
    private final PropertiesView propertiesView;
    private final StructureView structureView;
    private final ThemeView themeView;

    private SplitLayout propertiesAndStructure = new SplitLayout();

    public SideBarView(ProjectService projectService, EventService eventService) {

        propertiesView = new PropertiesView(projectService, eventService);
        structureView = new StructureView(projectService, eventService);

        propertiesAndStructure.setOrientation(SplitLayout.Orientation.VERTICAL);
        propertiesAndStructure.setSizeFull();

        propertiesAndStructure.addToPrimary(propertiesView);
        propertiesAndStructure.addToSecondary(structureView);

        themeView = new ThemeView(projectService, eventService);

        tabs = new Tabs();
        Tab editorTab = new Tab(VaadinIcon.EDIT.create(), new Label("Editor"));
        Tab themeTab = new Tab(VaadinIcon.PAINTBRUSH.create(), new Label("Theme"));
        tabs.add(editorTab, themeTab);
        add(tabs);

        content = new IronPages();
        content.setSizeFull();
        content.add(propertiesAndStructure, themeView);
        add(content);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(selectedChangeEvent -> content.setSelected(tabs.getSelectedIndex()));
        tabs.setSelectedTab(editorTab);

        setMargin(false);
        setPadding(false);
        setSpacing(false);
    }
}
