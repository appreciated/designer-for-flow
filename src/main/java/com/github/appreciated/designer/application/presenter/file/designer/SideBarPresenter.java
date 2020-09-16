package com.github.appreciated.designer.application.presenter.file.designer;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.file.designer.SideBarView;
import com.github.appreciated.designer.application.view.file.designer.ThemeView;
import com.github.appreciated.designer.application.view.file.designer.sidebar.PropertiesView;
import com.github.appreciated.designer.application.view.file.designer.sidebar.StructureView;
import com.github.appreciated.mvp.Presenter;

public class SideBarPresenter extends Presenter<ProjectFileModel, SideBarView> {
    public SideBarPresenter(ProjectFileModel projectFileModel) {
        super(projectFileModel);
        PropertiesView propertiesView = new PropertiesView(projectFileModel);
        StructureView structureView = new StructureView(projectFileModel);
        getContent().getPropertiesAndStructure().addToPrimary(propertiesView);
        getContent().getPropertiesAndStructure().addToSecondary(structureView);
        ThemeView themeView = new ThemeView(projectFileModel);
        getContent().getContent().add(themeView);

        projectFileModel.getEventService().getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            if (getContent().getTabs().getSelectedTab() != getContent().getEditorTab()) {
                getContent().getTabs().setSelectedTab(getContent().getEditorTab());
            }
        });
    }
}
