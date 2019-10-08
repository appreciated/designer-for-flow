package com.github.appreciated.designer.view;

import com.github.appreciated.designer.Shortcuts;
import com.github.appreciated.designer.component.AddButton;
import com.github.appreciated.designer.component.ironpages.IronPages;
import com.github.appreciated.designer.dialog.AddNewDesignTabDialog;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.ui.Transport;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("project")
@Push(transport = Transport.LONG_POLLING)
@StyleSheet("frontend://styles/styles.css")
public class ProjectView extends VerticalLayout implements HasUrlParameter<String> {

    private final Tabs tabs;
    private final IronPages content;

    private AddNewDesignTabDialog files;
    private HashMap<Tab, DesignCompilerInformation> views = new HashMap<>();
    private String projectPath;
    private EventService eventService;
    private ProjectService projectService;

    public ProjectView(@Autowired EventService eventService, @Autowired ProjectService projectService) {
        this.eventService = eventService;
        this.projectService = projectService;
        tabs = new Tabs();

        AddButton dial = new AddButton(VaadinIcon.PLUS.create(), event -> {
            files = new AddNewDesignTabDialog(projectService.getProject().getSourceFolder(), file -> {
                projectService.add(file);
                addTab(projectService.getCurrentFile());
            });
            files.open();
        });
        dial.setBottom("30px");

        add(tabs);
        content = new IronPages();
        content.setSizeFull();

        add(content);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            projectService.setCurrentFile(tabs.getSelectedIndex());
            content.add(new DividerView(projectService, eventService));
            content.setSelected(tabs.getSelectedIndex());
        });
        tabs.getStyle()
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("z-index", "1");
        setMargin(false);
        setPadding(false);
        setSpacing(false);
        add(dial);
        setSizeFull();
    }

    public void addTab(DesignCompilerInformation info) {
        Tab tab = new Tab(info.getDesign().getName());
        views.put(tab, info);
        tabs.add(tab);
        tabs.setSelectedTab(tab);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        new Shortcuts(attachEvent.getUI(), projectService, eventService);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        projectPath = URLDecoder.decode(parametersMap.get("path").get(0));
        projectService.initProject(new File(projectPath));
        if (projectService.getConfig().getDeveloperMode()) {
            projectService.add(new File("C:\\Users\\Johannes\\IdeaProjects\\designer-test-project\\src\\main\\java\\com\\github\\appreciated\\designer\\TestDesign.java"));
            addTab(projectService.getCurrentFile());
        }
    }


}
