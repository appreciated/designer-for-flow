package com.github.appreciated.designer.view;

import com.github.appreciated.designer.Shortcuts;
import com.github.appreciated.designer.component.AddButton;
import com.github.appreciated.designer.component.IconButton;
import com.github.appreciated.designer.component.ironpages.IronPages;
import com.github.appreciated.designer.dialog.AddNewDesignTabDialog;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ExceptionService;
import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
    HorizontalLayout appBar = new HorizontalLayout();
    private AddNewDesignTabDialog files;
    private HashMap<Tab, DesignCompilerInformation> views = new HashMap<>();
    private String projectPath;
    private EventService eventService;
    private ProjectService projectService;
    private ExceptionService exceptionService;

    public ProjectView(@Autowired EventService eventService, @Autowired ProjectService projectService, @Autowired ExceptionService exceptionService) {
        this.eventService = eventService;
        this.projectService = projectService;
        this.exceptionService = exceptionService;
        tabs = new Tabs();

        AddButton dial = new AddButton(VaadinIcon.PLUS.create(), event -> {
            files = new AddNewDesignTabDialog(projectService.getProject().getSourceFolder(), file -> {
                projectService.add(file);
                addTab(projectService.getCurrentFile());
            });
            files.open();
        });
        dial.setBottom("30px");
        Image logo = new Image("./frontend/styles/images/logo-floating-low.png", "logo");
        logo.getStyle().set("padding", "4px");
        logo.setHeight("48px");
        Label label = new Label("Designer for Flow");
        label.getStyle()
                .set("white-space", "nowrap")
                .set("line-height", "56px");
        appBar.add(logo, label, tabs);
        appBar.getStyle()
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("z-index", "1");
        appBar.setWidthFull();
        tabs.getStyle().set("--lumo-size-s", "var(--lumo-size-xl)");
        appBar.add(new Button("Error", event -> {
            throw new IllegalArgumentException("This is a test");
        }));
        add(appBar);

        content = new IronPages();
        content.setSizeFull();

        add(content);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            projectService.setCurrentFile(tabs.getSelectedIndex());
            content.add(new DividerView(projectService, eventService));
            content.setSelected(tabs.getSelectedIndex());
        });

        setMargin(false);
        setPadding(false);
        setSpacing(false);
        add(dial);
        setSizeFull();
        UI.getCurrent().getSession().setErrorHandler(event -> {
            exceptionService.setError(event.getThrowable());
            UI.getCurrent().navigate(ErrorPage.class);
        });
    }

    public void addTab(DesignCompilerInformation info) {
        Tab tab = new Tab();
        tab.add(new Label(info.getDesign().getName()),
                new IconButton(VaadinIcon.CLOSE_SMALL.create(), event -> {
                    projectService.close(info);
                    tabs.remove(tab);
                })
        );
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
