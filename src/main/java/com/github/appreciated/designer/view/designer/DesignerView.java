package com.github.appreciated.designer.view.designer;

import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.view.designer.template.DesignView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DesignerView extends VerticalLayout {
    private final DesignView designView;

    public DesignerView(ProjectService projectService, EventService eventService) {
        designView = new DesignView(projectService, eventService);
        add(designView);
        setMargin(false);
        setPadding(false);
        setSizeFull();
    }

    public DesignView getDesignView() {
        return designView;
    }

    public Component getRootComponent() {
        return designView.getRootComponent();
    }
}
