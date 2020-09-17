package com.github.appreciated.designer.application.view.file;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.file.designer.template.DesignerComponentsView;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import lombok.Getter;

@Getter
public class ProjectFileView extends SplitLayout {
	private static final long serialVersionUID = -456649093070497236L;
	
	private final SplitLayout componentsAndTemplate;

    public ProjectFileView() {
    	super();
        
    	componentsAndTemplate = new SplitLayout();
        componentsAndTemplate.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        componentsAndTemplate.setSizeFull();
        
        setOrientation(SplitLayout.Orientation.HORIZONTAL);
        setSplitterPosition(80);
        setSizeFull();
    }
    
    public void loadContent(final ProjectFileModel projectFileModel) {
    	DesignerComponentsView designerComponentView = new DesignerComponentsView(projectFileModel);
        
        componentsAndTemplate.addToPrimary(designerComponentView);
    }
}