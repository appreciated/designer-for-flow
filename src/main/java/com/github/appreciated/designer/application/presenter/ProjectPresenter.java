package com.github.appreciated.designer.application.presenter;

import com.github.appreciated.designer.Shortcuts;
import com.github.appreciated.designer.application.dialog.ErrorViewDialog;
import com.github.appreciated.designer.application.dialog.file.CreateOrOpenDesignTabDialog;
import com.github.appreciated.designer.application.model.ProjectModel;
import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.presenter.file.ProjectFilePresenter;
import com.github.appreciated.designer.application.service.EventService;
import com.github.appreciated.designer.application.service.ExceptionService;
import com.github.appreciated.designer.application.service.ProjectService;
import com.github.appreciated.designer.application.view.ProjectView;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.mvp.Presenter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.shared.ui.Transport;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("project")
@StyleSheet("./styles/theme.css")
@Push(transport = Transport.LONG_POLLING)
public class ProjectPresenter extends Presenter<ProjectModel, ProjectView> implements HasUrlParameter<String> {

    private final EventService eventService;
    private final ProjectService projectService;
    private final Map<Tab, ProjectFileModel> fileMap = new HashMap<>();
    private CreateOrOpenDesignTabDialog files;
    private boolean firstStart;

    public ProjectPresenter(@Autowired EventService eventService, @Autowired ProjectService projectService, @Autowired ExceptionService exceptionService) {
        UI ui = UI.getCurrent();
        ui.getSession().setErrorHandler(event -> {
            event.getThrowable().printStackTrace();
            exceptionService.setError(event.getThrowable());
            ui.access(() -> new ErrorViewDialog(exceptionService).open());
        });
        this.eventService = eventService;
        this.projectService = projectService;

        getContent().getTabs().addSelectedChangeListener(selectedChangeEvent -> {
            projectService.setCurrentProjectFileModel(fileMap.get(selectedChangeEvent.getSelectedTab()));
            getContent().getContent().setSelected(getContent().getTabs().getSelectedIndex());
        });
        getContent().getDial().addClickListener(event -> showOpenTabDialog());
        getContent().getTabs().addAddListener(this::showOpenTabDialog);
    }

    void showOpenTabDialog() {
        files = new CreateOrOpenDesignTabDialog(projectService.getProject(), projectService.getProject().getSourceFolder(), file -> {
            getUI().get().access(() -> {
                try {
                    this.addTab(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    getUI().get().getSession().getErrorHandler().error(new ErrorEvent(e));
                }
            });
        });
        files.open();
    }

    public void addTab(File file) throws IOException {
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

    @SneakyThrows
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
        if (firstStart) {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                attachEvent.getUI().access(() -> getContent().showHelper());
            }).start();
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        if (parametersMap.containsKey("firstStart")) {
            firstStart = true;
        }
        try {
            String projectPath = URLDecoder.decode(parametersMap.get("path").get(0), "UTF-8");
            projectService.initProject(new File(projectPath));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
