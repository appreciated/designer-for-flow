package com.github.appreciated.designer.application.view;

import com.github.appreciated.card.RippleClickableCard;
import com.github.appreciated.card.label.PrimaryLabelComponent;
import com.github.appreciated.card.label.SecondaryLabelComponent;
import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.css.grid.sizes.Flex;
import com.github.appreciated.css.grid.sizes.Length;
import com.github.appreciated.css.grid.sizes.MinMax;
import com.github.appreciated.css.grid.sizes.Repeat;
import com.github.appreciated.designer.AppConfig;
import com.github.appreciated.designer.application.component.AddButton;
import com.github.appreciated.designer.application.dialog.ErrorViewDialog;
import com.github.appreciated.designer.application.dialog.file.OpenProjectDialog;
import com.github.appreciated.designer.application.model.ProjectPath;
import com.github.appreciated.designer.application.model.project.ProjectTypes;
import com.github.appreciated.designer.application.presenter.ProjectPresenter;
import com.github.appreciated.designer.application.service.ExceptionService;
import com.github.appreciated.designer.application.service.ProjectRepository;
import com.github.appreciated.layout.FlexibleGridLayout;
import com.google.common.collect.Maps;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Route("")
@StyleSheet("./styles/theme.css")
@Push(transport = Transport.LONG_POLLING)
@Configurable
public class ProjectSelectionView extends VerticalLayout {
    private static final long serialVersionUID = -1279840235826097701L;

    // Layout
    private final FlexibleGridLayout layout;

    // Data
    private final ExceptionService exceptionService;
    private final ProjectRepository repository;
    private final AppConfig config;

    public ProjectSelectionView(@Autowired ProjectRepository repository, @Autowired ExceptionService exceptionService, @Autowired AppConfig config) {
        super();

        this.exceptionService = exceptionService;
        this.repository = repository;
        this.config = config;

        layout = new FlexibleGridLayout()
                .withColumns(Repeat.RepeatMode.AUTO_FILL, new MinMax(new Length("320px"), new Flex(1)))
                .withPadding(true)
                .withSpacing(true)
                .withAutoFlow(GridLayoutComponent.AutoFlow.ROW_DENSE)
                .withOverflow(GridLayoutComponent.Overflow.AUTO);
        layout.setWidth("100%");
        add(new H2(getTranslation("recent.projects")));
        setSizeFull();
        setMargin(false);
        setPadding(true);
        add(layout);
        if (repository.findAll().size() == 0) {
            add(new Span(getTranslation("get.started.by.adding.a.project")));
        } else {
            repository.findAll().forEach(projectPath -> layout.add(getCard(projectPath)));
            layout.add(getCreateCard());
        }
        AddButton button = new AddButton(VaadinIcon.PLUS.create(),
                buttonClickEvent -> new OpenProjectDialog(file -> addProject(file, true)).open()
        );
        button.setBottom("30px");
        add(button);
    }

    private Component getCard(final ProjectPath projectPath) {
        final Icon img = VaadinIcon.FOLDER.create();

        Button button = new Button(VaadinIcon.CLOSE.create());
        button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

        button.getStyle()
                .set("position", "absolute")
                .set("right", "5px")
                .set("top", "-8px")
                .set("color", "black");

        final VerticalLayout item = new VerticalLayout(
                new HorizontalLayout(img, new PrimaryLabelComponent(projectPath.getName())),
                new SecondaryLabelComponent(projectPath.getPath()),
                button
        );
        item.getElement().setAttribute("style", item.getElement().getAttribute("style") + ";padding:25px");

        item.setSizeFull();

        final RippleClickableCard card = new RippleClickableCard(event -> openProject(projectPath, false), item);
        card.getStyle().set("margin", "5px");
        card.setBackground("var(--lumo-primary-contrast-color)");

        button.addClickListener(event -> {
            repository.delete(projectPath);
            layout.remove(card);
            if (repository.findAll().size() == 0) {
                add(new Span(getTranslation("get.started.by.adding.a.project")));
            }
        });

        return card;
    }

    private Component getCreateCard() {
        final Icon img = VaadinIcon.PLUS.create();
        H4 h4 = new H4(getTranslation("add.new.project"));
        h4.getElement().setAttribute("style", "margin-top: unset;margin-bottom: unset;");
        HorizontalLayout hl = new HorizontalLayout(img, h4);
        hl.setAlignItems(Alignment.CENTER);
        hl.setJustifyContentMode(JustifyContentMode.CENTER);
        final VerticalLayout content = new VerticalLayout(hl);
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        final RippleClickableCard card = new RippleClickableCard(event ->
                new OpenProjectDialog(file -> addProject(file, true)).open()
                , content
        );
        content.setSizeFull();
        card.setElevation(0);
        card.getStyle().set("margin", "5px");
        card.setBackground("var(--lumo-contrast-5pct)");
        return card;
    }


    private void addProject(final File directory, boolean firstStart) {
        final ProjectTypes types = new ProjectTypes(directory);

        if (types.hasFittingProjectType()) {
            final ProjectPath projectPath = repository.save(new ProjectPath(directory.getPath()));
            openProject(projectPath, firstStart);
        } else {
            Notification.show(getTranslation("this.project.seems.to.be.missing.files"));
        }
    }

    private void openProject(final ProjectPath directory, boolean firstStart) {
        getUI().flatMap(ui -> ui.getRouter()
                .getRegistry()
                .getTargetUrl(ProjectPresenter.class))
                .ifPresent(urlPath -> {
                    urlPath = urlPath.replace("{String}", "");
                    final Map<String, List<String>> map = Maps.newHashMap();
                    try {
                        map.put("path", Collections.singletonList(URLEncoder.encode(directory.getPath(), "UTF-8")));
                        if (firstStart) {
                            map.put("firstStart", Collections.singletonList("true"));
                        }
                        // using UI#navigate causes many Components to not be drawn incorrectly
                        getUI().get().getPage().executeJs("location.href = \"" + urlPath + "?" + new QueryParameters(map).getQueryString() + "\"");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attachEvent.getUI().getSession().setErrorHandler(event -> {
            event.getThrowable().printStackTrace();
            exceptionService.setError(event.getThrowable());
            attachEvent.getUI().access(() -> new Dialog(new ErrorViewDialog(exceptionService)).open());
        });
    }
}