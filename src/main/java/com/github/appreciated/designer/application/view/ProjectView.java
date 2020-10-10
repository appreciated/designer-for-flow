package com.github.appreciated.designer.application.view;

import com.github.appreciated.designer.component.AddButton;
import com.github.appreciated.designer.component.IconButton;
import com.github.appreciated.designer.component.ironpages.IronPages;
import com.github.appreciated.designer.helper.UrlHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@StyleSheet("./styles/styles.css")
public class ProjectView extends VerticalLayout {

    private final Tabs tabs;
    private final IronPages content;
    private final AddButton dial;
    HorizontalLayout appBar = new HorizontalLayout();

    public ProjectView() {
        tabs = new Tabs();
        tabs.setHeight("60px");
        tabs.getStyle().set("box-shadow", "none");
        dial = new AddButton(VaadinIcon.PLUS.create());
        dial.setBottom("30px");
        Image logo = new Image("img/logo-floating-low.png", "logo");
        logo.getStyle().set("padding", "4px");
        logo.setHeight("48px");
        Label label = new Label(getTranslation("designer.for.flow"));
        label.getStyle()
                .set("white-space", "nowrap")
                .set("line-height", "56px");
        Button button = new Button(VaadinIcon.ANGLE_LEFT.create());
        button.getStyle().set("font-size", "25px").set("margin-right", "-20px");
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClickListener(event -> UI.getCurrent().navigate(ProjectSelectionView.class));
        Button donate = new Button(getTranslation("donate"), VaadinIcon.COFFEE.create(), event -> UrlHelper.openUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=RH84HC939XQHS"));
        donate.getStyle()
                .set("flex-shrink", "0")
                .set("margin-right", "10px");
        donate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        appBar.add(button, logo, label, tabs, donate);
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
        add(dial);
        setSizeFull();
    }

    public Tab createTab(@NotNull String name, Consumer<Tab> clickListener) {
        Tab tab = new Tab();
        tab.add(new Label(name),
                new IconButton(VaadinIcon.CLOSE_SMALL.create(), event -> clickListener.accept(tab))
        );
        return tab;
    }

    public void addTab(Tab tab) {
        tabs.add(tab);
        tabs.setSelectedTab(tab);
    }

    public Tabs getTabs() {
        return tabs;
    }

    public AddButton getDial() {
        return dial;
    }

    public IronPages getContent() {
        return content;
    }
}
