package com.github.appreciated.designer.application.presenter;

import com.github.appreciated.designer.Shortcuts;
import com.github.appreciated.designer.application.model.ProjectModel;
import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.presenter.file.ProjectFilePresenter;
import com.github.appreciated.designer.application.view.ProjectView;
import com.github.appreciated.designer.dialog.CreateOrOpenDesignTabDialog;
import com.github.appreciated.designer.dialog.ErrorViewDialog;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ExceptionService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.mvp.Presenter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.ui.Transport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("project")
@StyleSheet("./styles/theme.css")
@Push(transport = Transport.LONG_POLLING)
public class ProjectPresenter extends Presenter<ProjectModel, ProjectView> implements HasUrlParameter<String> {

    private CreateOrOpenDesignTabDialog files;
    private final EventService eventService;
    private final ProjectService projectService;
    private final Map<Tab, ProjectFileModel> fileMap = new HashMap<>();

    public ProjectPresenter(@Autowired EventService eventService, @Autowired ProjectService projectService, @Autowired ExceptionService exceptionService) {
        UI ui = UI.getCurrent();
        ui.getSession().setErrorHandler(event -> {
            if (projectService.getConfig().getDeveloperMode()) {
                event.getThrowable().printStackTrace();
            }
            exceptionService.setError(event.getThrowable());
            event.getThrowable().printStackTrace();
            ui.access(() -> new ErrorViewDialog(exceptionService).open());
        });
        this.eventService = eventService;
        this.projectService = projectService;

        getContent().getTabs().addSelectedChangeListener(selectedChangeEvent -> {
            projectService.setCurrentProjectFileModel(fileMap.get(selectedChangeEvent.getSelectedTab()));
            getContent().getContent().setSelected(getContent().getTabs().getSelectedIndex());
        });

        getContent().getDial().addClickListener(event -> {
            files = new CreateOrOpenDesignTabDialog(projectService.getProject(), projectService.getProject().getSourceFolder(), this::addTab);
            files.open();
        });
    }

    public void addTab(File file) {
        DesignCompilerInformation info = projectService.create(file);
        ProjectFileModel model = new ProjectFileModel(info, eventService);
        ProjectFilePresenter presenter = new ProjectFilePresenter(model);
        Tab fileTab = getContent().createTab(info.getDesign().getName(), tab -> {
            getContent().getContent().remove(presenter);
            getContent().getTabs().remove(tab);
        });
        fileMap.put(fileTab, model);
        getContent().addTab(fileTab);
        getContent().getContent().add(presenter);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        Shortcuts.register(attachEvent.getUI(), projectService, eventService);
        if (projectService.getConfig().getDeveloperMode()) {
            File defaultFile = new File("C:\\Users\\Johannes\\IdeaProjects\\designer-test-project\\src\\main\\java\\com\\github\\appreciated\\designer\\view\\TestDesign.java");
            if (defaultFile.exists()) {
                addTab(defaultFile);
            }
        }
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        String projectPath = URLDecoder.decode(parametersMap.get("path").get(0));
        projectService.initProject(new File(projectPath));
    }

}
