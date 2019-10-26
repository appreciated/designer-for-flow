package com.github.appreciated.designer.application.view;

import com.github.appreciated.designer.application.view.designer.DesignerView;
import com.github.appreciated.designer.application.view.designer.SideBarView;
import com.github.appreciated.designer.application.view.designer.ThemeView;
import com.github.appreciated.designer.application.view.designer.template.DesignerComponentsView;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.component.splitlayout.SplitLayout;

public class DividerView extends SplitLayout {
    private final DesignerComponentsView designerComponentView;
    private final DesignerView designerView;
    private SplitLayout componentsAndTemplate = new SplitLayout();
    private ThemeView themeView;

    public DividerView(ProjectService projectService, EventService eventService) {
        componentsAndTemplate.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        componentsAndTemplate.setSizeFull();
        setOrientation(SplitLayout.Orientation.HORIZONTAL);
        setSizeFull();

        designerComponentView = new DesignerComponentsView(projectService);
        designerView = new DesignerView(projectService, eventService);

        componentsAndTemplate.addToPrimary(designerComponentView);
        componentsAndTemplate.addToSecondary(designerView);
        componentsAndTemplate.setSplitterPosition(25);


        componentsAndTemplate.addToPrimary(designerComponentView);
        componentsAndTemplate.addToSecondary(designerView);

        addToPrimary(componentsAndTemplate);
        addToSecondary(new SideBarView(projectService, eventService));

        setSplitterPosition(80);
        setSizeFull();
    }

    public DesignerView getDesignerView() {
        return designerView;
    }
}
