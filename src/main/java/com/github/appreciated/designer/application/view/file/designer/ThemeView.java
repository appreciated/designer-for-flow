package com.github.appreciated.designer.application.view.file.designer;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.BaseView;
import com.github.appreciated.designer.application.view.file.designer.theme.ColorStyleView;
import com.github.appreciated.designer.application.view.file.designer.theme.OtherStyleView;
import com.github.appreciated.designer.application.view.file.designer.theme.SizeAndSpaceStyleView;
import com.github.appreciated.designer.application.view.file.designer.theme.TypographyStyleView;
import com.github.appreciated.designer.component.ironpages.IronPages;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class ThemeView extends BaseView {

    private Tabs tabs;
    private IronPages content;

    public ThemeView(ProjectFileModel projectFileModel) {
        super("Theme");
        if (projectFileModel.getInformation().getProject().hasThemeFile()) {
            ColorStyleView colorStyleView = new ColorStyleView(projectFileModel, projectFileModel.getEventService());
            TypographyStyleView typographyView = new TypographyStyleView(projectFileModel, projectFileModel.getEventService());
            SizeAndSpaceStyleView sizeAndSpaceStyleView = new SizeAndSpaceStyleView(projectFileModel, projectFileModel.getEventService());
            OtherStyleView otherStyleView = new OtherStyleView(projectFileModel, projectFileModel.getEventService());

            tabs = new Tabs();
            Tab colorTab = new Tab(VaadinIcon.PALETE.create(), new Label("Color"));
            Tab typographyTab = new Tab(VaadinIcon.FONT.create(), new Label("Typography"));
            Tab sizeTab = new Tab(VaadinIcon.BACKSPACE.create(), new Label("Size & Space"));
            Tab otherTab = new Tab(VaadinIcon.EDIT.create(), new Label("Other"));
            tabs.add(colorTab, typographyTab, sizeTab, otherTab);
            add(tabs);
            content = new IronPages();
            content.setSizeFull();
            content.add(colorStyleView, typographyView, sizeAndSpaceStyleView, otherStyleView);
            add(content);
            tabs.setWidthFull();
            tabs.addSelectedChangeListener(selectedChangeEvent -> content.setSelected(tabs.getSelectedIndex()));
            tabs.setSelectedTab(colorTab);
        } else {
            add(new Label("There was no theme file found!"));
        }
    }
}
