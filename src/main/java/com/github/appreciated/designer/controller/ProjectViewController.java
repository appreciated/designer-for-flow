package com.github.appreciated.designer.controller;

import com.github.appreciated.designer.Shortcuts;
import com.github.appreciated.designer.application.view.DividerView;
import com.github.appreciated.designer.application.view.ErrorPageView;
import com.github.appreciated.designer.application.view.ProjectView;
import com.github.appreciated.designer.dialog.AddNewDesignTabDialog;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ExceptionService;
import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@UIScope
public class ProjectViewController {

    private final EventService eventService;
    private final ProjectService projectService;
    private final ExceptionService exceptionService;
    private ProjectView projectView;
    private HashMap<Tab, DesignCompilerInformation> views = new HashMap<>();
    private String projectPath;

    public ProjectViewController(@Autowired EventService eventService, @Autowired ProjectService projectService, @Autowired ExceptionService exceptionService) {
        this.eventService = eventService;
        this.projectService = projectService;
        this.exceptionService = exceptionService;
        UI.getCurrent().getSession().setErrorHandler(event -> {
            if (projectService.getConfig().getDeveloperMode()) {
                event.getThrowable().printStackTrace();
            }
            exceptionService.setError(event.getThrowable());
            UI.getCurrent().navigate(ErrorPageView.class);
        });
    }

    public void setView(ProjectView projectView) {
        this.projectView = projectView;
        projectView.getDial().addClickListener(event -> {
            AddNewDesignTabDialog files = new AddNewDesignTabDialog(projectService.getProject().getSourceFolder(), this::addTab);
            files.open();
        });
        projectView.getTabs().addSelectedChangeListener(selectedChangeEvent -> {
            projectService.setCurrentFile(projectView.getTabs().getSelectedIndex());
            projectView.getTabs().add(new DividerView(projectService, eventService));
            projectView.getContent().setSelected(projectView.getTabs().getSelectedIndex());
        });
    }

    private void addTab(File defaultFile) {
        projectService.add(defaultFile);
        Tab tab = projectView.addTab(projectService.getCurrentFile().getDesign().getName(), clickEvent -> {
            projectService.close(projectService.getCurrentFile());
            projectView.getTabs().remove();
        });
        views.put(tab, projectService.getCurrentFile());
    }

    public void onAttach(AttachEvent attachEvent) {
        new Shortcuts(attachEvent.getUI(), projectService, eventService);
    }

    public void setParameter(BeforeEvent beforeEvent) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        projectPath = URLDecoder.decode(parametersMap.get("path").get(0));
        projectService.initProject(new File(projectPath));
        if (projectService.getConfig().getDeveloperMode()) {
            File defaultFile = new File("C:\\Users\\Johannes\\IdeaProjects\\designer-test-project\\src\\main\\java\\com\\github\\appreciated\\designer\\TestDesign.java");
            if (defaultFile.exists()) {
                addTab(defaultFile);
            }
        }
    }
}
