package com.github.appreciated.designer.application.view;

import com.github.appreciated.designer.application.component.AddButton;
import com.github.appreciated.designer.application.component.CustomTabs;
import com.github.appreciated.designer.application.component.IconButton;
import com.github.appreciated.designer.application.component.ironpages.IronPages;
import com.github.appreciated.designer.helper.UrlHelper;
import com.vaadin.componentfactory.Tooltip;
import com.vaadin.componentfactory.TooltipAlignment;
import com.vaadin.componentfactory.TooltipPosition;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import org.vaadin.addon.driverjs.DriverJS;
import org.vaadin.addon.driverjs.model.DriverDefinition;
import org.vaadin.addon.driverjs.model.StepDefinitionBuilder;
import org.vaadin.addon.driverjs.model.StepPosition;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@StyleSheet("./styles/styles.css")
public class ProjectView extends VerticalLayout {

    private final CustomTabs tabs;
    private final IronPages content;
    private final AddButton dial;
    HorizontalLayout appBar = new HorizontalLayout();

    public ProjectView() {
        tabs = new CustomTabs();
        tabs.setHeight("60px");
        tabs.getStyle().set("box-shadow", "none");
        dial = new AddButton(VaadinIcon.PLUS.create());
        dial.setBottom("30px");
        Image logo = new Image("img/logo-floating-low.png", "logo");
        logo.getStyle().set("padding", "4px");
        logo.setHeight("48px");
        Span label = new Span(getTranslation("designer.for.flow"));
        label.getStyle()
                .set("white-space", "nowrap")
                .set("line-height", "56px");
        Button backButton = new Button(VaadinIcon.ANGLE_LEFT.create());

        Tooltip tooltip = new Tooltip();
        tooltip.setPosition(TooltipPosition.RIGHT);
        tooltip.setAlignment(TooltipAlignment.CENTER);
        tooltip.attachToComponent(backButton);
        tooltip.add(new Span(getTranslation("back.to.project.selection")));

        backButton.getStyle().set("font-size", "25px").set("margin-right", "-20px");
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        backButton.addClickListener(event -> UI.getCurrent().navigate(ProjectSelectionView.class));
        Button donate = new Button(getTranslation("donate"), VaadinIcon.COFFEE.create(), event -> {
            UrlHelper.openUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=RH84HC939XQHS");
        });
        donate.getStyle()
                .set("flex-shrink", "0")
                .set("margin-right", "10px");
        donate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        appBar.add(backButton, tooltip, logo, label, tabs, donate);
        appBar.setAlignItems(Alignment.CENTER);
        appBar.getStyle()
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("z-index", "1")
                .set("background", "var(--lumo-primary-contrast-color)");
        appBar.setWidthFull();
        tabs.getStyle().set("--lumo-size-s", "var(--lumo-size-xl)");
        getStyle().set("overflow", "hidden");
        add(appBar);
        content = new IronPages();
        content.getElement().getStyle().set("overflow", "hidden");
        content.setSizeFull();
        add(content);
        tabs.setWidthFull();
        setMargin(false);
        setPadding(false);
        setSpacing(false);

        Tooltip addTooltip = new Tooltip(dial);
        addTooltip.setPosition(TooltipPosition.LEFT);
        addTooltip.setAlignment(TooltipAlignment.CENTER);
        addTooltip.add(new Span(getTranslation("add.new.design")));
        add(dial, addTooltip);
        setSizeFull();
    }

    public void showHelper() {
        DriverJS driver = new DriverJS();
        driver.setStepDefinitions(
                StepDefinitionBuilder.ofComponent(tabs.getAddTab())
                        .withTitle(getTranslation("introduction"))
                        .withPosition(StepPosition.BOTTOM_CENTER)
                        .withDescription(getTranslation("start.by.adding.a.new.design.here")),
                StepDefinitionBuilder.ofComponent(dial)
                        .withTitle(getTranslation("start.by.adding.a.new.design.here.second"))
                        .withPosition(StepPosition.TOP_RIGHT)
        );
        DriverDefinition definition = new DriverDefinition();
        definition.setCloseBtnText(getTranslation("close"));
        definition.setPrevBtnText(getTranslation("previous"));
        definition.setNextBtnText(getTranslation("next"));
        definition.setDoneBtnText(getTranslation("done"));
        driver.setupDriver(definition);
        driver.start();
        add(driver);
    }

    public Tab createTab(@NotNull String name, Consumer<Tab> clickListener) {
        Tab tab = new Tab();
        IconButton button = new IconButton(VaadinIcon.CLOSE_SMALL.create(), event -> clickListener.accept(tab));
        button.getStyle().set("--_lumo-button-color", "gray");
        tab.add(new Span(name), button);
        return tab;
    }

    public void addTab(Tab tab) {
        tabs.add(tab);
        tabs.setSelectedTab(tab);
    }

    public CustomTabs getTabs() {
        return tabs;
    }

    public AddButton getDial() {
        return dial;
    }

    public IronPages getContent() {
        return content;
    }
}
