package com.github.appreciated.designer.application.view.file.designer.template;

import com.github.appreciated.designer.application.view.BaseView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;
import lombok.Getter;

@Getter
public class DesignerView extends BaseView {
    private static final long serialVersionUID = -5707329831685871000L;

    // Layout
    protected final VerticalLayout designWrapper;
    protected final HorizontalLayout designHolder;
    protected final HorizontalLayout wrapperContainer;

    // Header
    protected final HorizontalLayout header;
    protected final H3 title;

    // Tabs
    protected final Tabs tabs;
    protected final Tab fullSizeTab;
    protected final Tab mobileTab;
    protected final Tab mobileLandscapeTab;
    protected final Tab tabletTab;
    protected final Tab tabletLandscapeTab;
    protected final Tab laptopTab;

    public DesignerView() {
        super();

        // Tabs
        tabs = new Tabs();

        fullSizeTab = createTab(tabs, VaadinIcon.EXPAND_SQUARE);
        mobileTab = createTab(tabs, VaadinIcon.MOBILE);
        mobileLandscapeTab = createTab(tabs, VaadinIcon.MOBILE, 90);
        tabletTab = createTab(tabs, VaadinIcon.TABLET, 90);
        tabletLandscapeTab = createTab(tabs, VaadinIcon.TABLET);
        laptopTab = createTab(tabs, VaadinIcon.LAPTOP);

        title = new H3("Design");
        title.getStyle().set("margin-top", "0");
        title.getStyle().set("margin-bottom", "0");

        header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        header.getStyle().set("z-index", "1");
        header.add(title, tabs);
        header.setWidthFull();
        header.expand(title);

        designHolder = new HorizontalLayout();
        designHolder.setPadding(true);
        designHolder.setJustifyContentMode(JustifyContentMode.CENTER);
        designHolder.setAlignItems(Alignment.CENTER);

        designWrapper = new VerticalLayout();
        designWrapper.getElement().getClassList().add("design-view");
        designWrapper.setWidth("50%");
        designWrapper.setHeight("50%");
        designWrapper.add(designHolder);
        designWrapper.setMargin(false);
        designWrapper.setPadding(false);
        designWrapper.getStyle()
                .set("resize", "both")
                .set("overflow", "hidden");

        wrapperContainer = new HorizontalLayout(designWrapper);
        wrapperContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        wrapperContainer.getStyle().set("overflow", "auto");
        wrapperContainer.setAlignItems(Alignment.CENTER);
        wrapperContainer.setSizeFull();

        tabs.getStyle().set("align-self", "flex-end");
        tabs.addSelectedChangeListener(this::checkTabIfPresentAndSetup);
        tabs.setSelectedTab(fullSizeTab);

        add(header, wrapperContainer);
    }

    protected void checkTabIfPresentAndSetup(final SelectedChangeEvent event) {
        if (fullSizeTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("100%");
            designWrapper.setHeight("100%");
            designWrapper.getStyle()
                    .set("border", "unset")
                    .set("border-radius", "0px");
        } else if (mobileTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("7cm");
            designWrapper.setHeight("14cm");
            designWrapper.getStyle()
                    .set("border", "3px solid black")
                    .set("border-radius", "10px");
        } else if (mobileLandscapeTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("14cm");
            designWrapper.setHeight("7cm");
            designWrapper.getStyle()
                    .set("border", "3px solid black")
                    .set("border-radius", "10px");
        } else if (tabletTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("17cm");
            designWrapper.setHeight("24cm");
            designWrapper.getStyle()
                    .set("border", "3px solid black")
                    .set("border-radius", "10px");
        } else if (tabletLandscapeTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("24cm");
            designWrapper.setHeight("17cm");
            designWrapper.getStyle()
                    .set("border", "3px solid black")
                    .set("border-radius", "10px");
        } else if (laptopTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("33.2cm");
            designWrapper.setHeight("18.7cm");
            designWrapper.getStyle()
                    .set("border", "3px solid black")
                    .set("border-radius", "10px");
        }
    }

    private Tab createTab(final Tabs tabs, final VaadinIcon icon) {
        return createTab(tabs, icon, 0);
    }

    private Tab createTab(final Tabs tabs, final VaadinIcon icon, final int degree) {
        final Tab tab = new Tab(getRotatedIcon(icon, degree));
        tabs.add(tab);
        return tab;
    }

    private Icon getRotatedIcon(final VaadinIcon icon, final int degree) {
        Icon i = icon.create();

        if (degree != 0) {
            i.getStyle().set("transform", "rotate(" + degree + "deg)");
        }

        return i;
    }
}