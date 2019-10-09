package com.github.appreciated.designer.view;

import com.github.appreciated.card.RippleClickableCard;
import com.github.appreciated.card.label.PrimaryLabelComponent;
import com.github.appreciated.card.label.SecondaryLabelComponent;
import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.css.grid.sizes.Flex;
import com.github.appreciated.css.grid.sizes.Length;
import com.github.appreciated.css.grid.sizes.MinMax;
import com.github.appreciated.css.grid.sizes.Repeat;
import com.github.appreciated.designer.component.AddButton;
import com.github.appreciated.designer.dialog.OpenProjectDialog;
import com.github.appreciated.designer.model.ProjectPath;
import com.github.appreciated.designer.model.project.ProjectTypes;
import com.github.appreciated.designer.service.ExceptionService;
import com.github.appreciated.designer.service.ProjectRepository;
import com.github.appreciated.layout.FlexibleGridLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.ui.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.File;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Route("")
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
@Push(transport = Transport.LONG_POLLING)
@Configurable
public class ProjectSelectionView extends VerticalLayout {

    private ProjectRepository repository;
    private ExceptionService exceptionService;

    public ProjectSelectionView(@Autowired ProjectRepository repository, @Autowired ExceptionService exceptionService) {
        this.repository = repository;
        this.exceptionService = exceptionService;
        FlexibleGridLayout layout = new FlexibleGridLayout()
                .withColumns(Repeat.RepeatMode.AUTO_FILL, new MinMax(new Length("320px"), new Flex(1)))
                .withPadding(true)
                .withSpacing(true)
                .withAutoFlow(GridLayoutComponent.AutoFlow.ROW_DENSE)
                .withOverflow(GridLayoutComponent.Overflow.AUTO);
        layout.setWidth("100%");
        add(new H2("Recent Projects"));
        setSizeFull();
        setMargin(false);
        setPadding(true);
        add(layout);
        if (repository.findAll().size() == 0) {
            add(new Label("Get started by adding a project"));
        } else {
            repository.findAll().forEach(projectPath -> layout.add(getCard(projectPath)));
        }
        AddButton button = new AddButton(VaadinIcon.PLUS.create(), buttonClickEvent -> {
            new OpenProjectDialog(file -> addProject(file)).open();
        });
        button.setBottom("30px");
        add(button);
        UI.getCurrent().getSession().setErrorHandler(event -> {
            exceptionService.setError(event.getThrowable());
            UI.getCurrent().navigate(ErrorPage.class);
        });
    }

    private Component getCard(ProjectPath projectPath) {
        Icon img = VaadinIcon.FOLDER.create();

        VerticalLayout item = new VerticalLayout(
                new HorizontalLayout(img, new PrimaryLabelComponent(projectPath.getName())),
                new SecondaryLabelComponent(projectPath.getPath())
        );
        item.getElement().setAttribute("style", item.getElement().getAttribute("style") + ";padding:25px");

        item.setSizeFull();

        RippleClickableCard card = new RippleClickableCard(
                event -> openProject(projectPath), item
        );
        card.getStyle().set("margin", "5px");
        return card;
    }

    private void addProject(File directory) {
        ProjectTypes types = new ProjectTypes(directory);
        if (types.hasFittingProjectType()) {
            ProjectPath projectPath = repository.save(new ProjectPath(directory.getPath()));
            openProject(projectPath);
        } else {
            Notification.show("This project seems to be missing file!");
        }
    }


    private void openProject(ProjectPath directory) {
        getUI().ifPresent(ui -> ui.getRouter().getRegistry()
                .getTargetUrl(ProjectView.class)
                .ifPresent(urlPath -> {
                    urlPath = urlPath.replace("{String}", "");
                    Map<String, List<String>> map = new HashMap<>();
                    map.put("path", Collections.singletonList(URLEncoder.encode(directory.getPath())));
                    // using UI#navigate causes many Components to not be drawn incorrectly
                    ui.getPage().executeJs("location.href = \"" + urlPath + "?" + new QueryParameters(map).getQueryString() + "\"");
                }));
    }

}
