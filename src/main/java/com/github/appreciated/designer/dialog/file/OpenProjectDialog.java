package com.github.appreciated.designer.dialog.file;


import com.github.appreciated.designer.helper.UrlHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

public class OpenProjectDialog extends Dialog {

    private final HorizontalLayout container;
    private final Tab mavenTab;
    private final MavenProjectChooserView mavenProjectChooserView;

    public OpenProjectDialog(Consumer<File> fileConsumer) {
        super();
        setMinWidth("50%");
        setMinHeight("50%");
        setWidth("500px");
        Tabs projectTypeTabs = new Tabs();
        projectTypeTabs.setWidthFull();
        mavenTab = new Tab();
        mavenTab.setLabel("Maven");
        projectTypeTabs.add(mavenTab);
        projectTypeTabs.setSelectedTab(mavenTab);
        VerticalLayout wrapper = new VerticalLayout();
        H2 header = new H2(getTranslation("select.project.folder"));
        wrapper.add(header);
        wrapper.add(projectTypeTabs);
        wrapper.setMargin(false);
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        container = new HorizontalLayout();
        container.setWidthFull();
        wrapper.add(container);
        mavenProjectChooserView = new MavenProjectChooserView(Arrays.stream(File.listRoots()).findFirst().get(), fileConsumer, true, true, o -> close());
        setTab(mavenTab);
        projectTypeTabs.addSelectedChangeListener(event -> setTab(event.getSelectedTab()));
        add(wrapper);
        wrapper.setWidthFull();

        Button create = new Button(getTranslation("create.new.project"), event -> UrlHelper.openUrl("https://start.vaadin.com/?preset=lts"));
        mavenProjectChooserView.getButtons().addComponentAtIndex(0, create);
    }

    private void setTab(Tab selectedTab) {
        container.removeAll();
        if (selectedTab == mavenTab) {
            container.add(mavenProjectChooserView);
        }
    }

}
