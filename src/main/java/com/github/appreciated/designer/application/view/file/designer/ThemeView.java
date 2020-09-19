package com.github.appreciated.designer.application.view.file.designer;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.BaseView;
import com.github.appreciated.designer.application.view.file.designer.theme.ColorStyleView;
import com.github.appreciated.designer.application.view.file.designer.theme.OtherStyleView;
import com.github.appreciated.designer.application.view.file.designer.theme.SizeAndSpaceStyleView;
import com.github.appreciated.designer.application.view.file.designer.theme.TypographyStyleView;
import com.github.appreciated.designer.component.ironpages.IronPages;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class ThemeView extends BaseView {
	private static final long serialVersionUID = -3347903543781497112L;
	
	private Tabs tabs;
    private IronPages content;

    private final ProjectFileModel projectFileModel;
    
    public ThemeView(final ProjectFileModel projectFileModel) {
        super("theme");
        
        this.projectFileModel = projectFileModel;
        
        init();
    }
    
    private void init() {
    	if (projectFileModel.getInformation().getProject().hasThemeFile()) {
            ColorStyleView colorStyleView = new ColorStyleView(projectFileModel, projectFileModel.getEventService());
            TypographyStyleView typographyView = new TypographyStyleView(projectFileModel, projectFileModel.getEventService());
            SizeAndSpaceStyleView sizeAndSpaceStyleView = new SizeAndSpaceStyleView(projectFileModel, projectFileModel.getEventService());
            OtherStyleView otherStyleView = new OtherStyleView(projectFileModel, projectFileModel.getEventService());

            Tab colorTab = new Tab(VaadinIcon.PALETE.create(), new Label(getTranslation("color")));
            Tab typographyTab = new Tab(VaadinIcon.FONT.create(), new Label(getTranslation("typography")));
            Tab sizeTab = new Tab(VaadinIcon.BACKSPACE.create(), new Label(getTranslation("size.space")));
            Tab otherTab = new Tab(VaadinIcon.EDIT.create(), new Label(getTranslation("other")));

            tabs = new Tabs();
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
            setAlignItems(Alignment.CENTER);
            add(new Label(getTranslation("there.was.no.theme.file.found")));

            final Button createButton = new Button(getTranslation("create"), VaadinIcon.PLUS.create(), e -> {
                if (projectFileModel.getInformation().getProject().createThemeFile()) {
                    projectFileModel.getInformation().setProject(projectFileModel.getInformation().getProject());
                    removeAll();
                    init();
                } else {
                    e.getSource().setEnabled(false);
                }
            });
            
            createButton.setDisableOnClick(true);
            
            add(createButton);
        }
    }
}