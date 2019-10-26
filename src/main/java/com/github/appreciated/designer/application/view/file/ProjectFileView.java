package com.github.appreciated.designer.application.view.file;

import com.github.appreciated.designer.application.view.file.designer.template.DesignerComponentsView;
import com.vaadin.flow.component.splitlayout.SplitLayout;

public class ProjectFileView extends SplitLayout {
    private SplitLayout componentsAndTemplate = new SplitLayout();

    public ProjectFileView() {

        DesignerComponentsView designerComponentView = new DesignerComponentsView();
        componentsAndTemplate.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        componentsAndTemplate.setSizeFull();
        componentsAndTemplate.addToPrimary(designerComponentView);
        setOrientation(SplitLayout.Orientation.HORIZONTAL);
        setSizeFull();
        setSplitterPosition(80);
        setSizeFull();
    }

    public SplitLayout getComponentsAndTemplate() {
        return componentsAndTemplate;
    }

}
