package com.github.appreciated.designer.application.view.file.designer.template;

import com.github.appreciated.designer.application.view.BaseView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;


public class DesignerView extends BaseView {
    private final VerticalLayout designWrapper;
    private final HorizontalLayout designHolder;

    public DesignerView() {
        super("Design");
        designHolder = new HorizontalLayout();
        designHolder.setPadding(true);
        designHolder.getElement().getClassList().add("design-holder");
        designWrapper = new VerticalLayout();
        Tabs tabs = new Tabs();
        Tab mobile = new Tab(VaadinIcon.MOBILE.create());
        Tab mobileLandscape = new Tab(getRotatedIcon(VaadinIcon.MOBILE, 90));
        Tab tablet = new Tab(getRotatedIcon(VaadinIcon.TABLET, 90));
        Tab tabletLandscape = new Tab(VaadinIcon.TABLET.create());
        Tab laptop = new Tab(VaadinIcon.LAPTOP.create());
        tabs.add(mobile, mobileLandscape, tablet, tabletLandscape, laptop);
        tabs.getStyle().set("align-self", "flex-end");
        tabs.addSelectedChangeListener(event -> {
            if (mobile.equals(event.getSelectedTab())) {
                designWrapper.setWidth("7cm");
                designWrapper.setHeight("14cm");
            } else if (mobileLandscape.equals(event.getSelectedTab())) {
                designWrapper.setWidth("14cm");
                designWrapper.setHeight("7cm");
            } else if (tablet.equals(event.getSelectedTab())) {
                designWrapper.setWidth("17cm");
                designWrapper.setHeight("24cm");
            } else if (tabletLandscape.equals(event.getSelectedTab())) {
                designWrapper.setWidth("24cm");
                designWrapper.setHeight("17cm");
            } else if (laptop.equals(event.getSelectedTab())) {
                designWrapper.setWidth("33.2cm");
                designWrapper.setHeight("18.7cm");
            }
        });
        tabs.setSelectedTab(mobile);

        designHolder.setJustifyContentMode(JustifyContentMode.CENTER);
        designHolder.setAlignItems(Alignment.CENTER);

        designWrapper.getElement().getClassList().add("design-view");
        designWrapper.setWidth("50%");
        designWrapper.setHeight("50%");
        designWrapper.add(designHolder);
        designWrapper.setMargin(false);
        designWrapper.setPadding(false);
        designWrapper.getStyle()
                .set("resize", "both")
                .set("overflow", "hidden");

        HorizontalLayout wrapperContainer = new HorizontalLayout(designWrapper);
        wrapperContainer.setSizeFull();
        wrapperContainer.setAlignItems(Alignment.CENTER);
        wrapperContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        add(tabs, wrapperContainer);
    }

    private Icon getRotatedIcon(VaadinIcon icon, int degree) {
        Icon i = icon.create();
        i.getStyle().set("transform", "rotate(" + degree + "deg)");
        return i;
    }

    public VerticalLayout getDesignWrapper() {
        return designWrapper;
    }

    public HorizontalLayout getDesignHolder() {
        return designHolder;
    }
}
