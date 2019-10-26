package com.github.appreciated.designer.application.presenter.file.designer;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.presenter.file.designer.template.DesignerPresenter;
import com.github.appreciated.designer.application.view.file.designer.DesignerWrapperView;
import com.github.appreciated.mvp.Presenter;

public class DesignerWrapperPresenter extends Presenter<ProjectFileModel, DesignerWrapperView> {
    private final DesignerPresenter designerPresenter;

    public DesignerWrapperPresenter(ProjectFileModel projectFileModel) {
        super(projectFileModel);
        designerPresenter = new DesignerPresenter(projectFileModel);
        getContent().add(designerPresenter);
    }

    public DesignerPresenter getDesignerPresenter() {
        return designerPresenter;
    }
}
