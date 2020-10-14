package com.github.appreciated.designer.application.view.file.designer.template;

import com.github.appreciated.designer.application.view.BaseView;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicReference;

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
    protected final Tab resizeableTab;
    protected final Tab mobileTab;
    protected final Tab mobileLandscapeTab;
    protected final Tab tabletTab;
    protected final Tab tabletLandscapeTab;
    protected final Tab laptopTab;
    private final ComboBox<String> sizes;

    public DesignerView() {
        super();


        // Tabs
        tabs = new Tabs();

        HorizontalLayout designerViewSizeInput = new HorizontalLayout(tabs);

        resizeableTab = createTab(tabs, VaadinIcon.EXPAND_SQUARE);
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
        header.add(title, designerViewSizeInput);
        header.setWidthFull();
        header.expand(title);
        header.setPadding(true);

        designHolder = new HorizontalLayout();
        designHolder.setPadding(true);
        designHolder.setJustifyContentMode(JustifyContentMode.CENTER);
        designHolder.setAlignItems(Alignment.CENTER);

        designWrapper = new VerticalLayout();
        designWrapper.getElement().getClassList().add("design-view");
        designWrapper.add(designHolder);
        designWrapper.setMargin(false);
        designWrapper.setPadding(false);
        designWrapper.getStyle()
                .set("flex-shrink", "0")
                .set("overflow", "hidden");
        designWrapper.setWidth("98%");
        designWrapper.setHeight("98%");

        sizes = new ComboBox<>();
        sizes.setPlaceholder(getTranslation("select.a.resolution"));
        Binder<AtomicReference<Size>> binder = new Binder<>();
        binder.forField(sizes)
                .withValidator((size, valueContext) -> {
                    try {
                        return ValidationResult.ok();
                    } catch (Exception e) {
                        return ValidationResult.error(getTranslation("input.requires.the.following.format"));
                    }
                })
                .withConverter(new Converter<String, Size>() {
                    @Override
                    public Result<Size> convertToModel(String s, ValueContext valueContext) {
                        return Result.ok(Size.parse(s));
                    }

                    @Override
                    public String convertToPresentation(Size size, ValueContext valueContext) {
                        return Size.present(size);
                    }
                })
                .bind(AtomicReference::get, AtomicReference::set);
        binder.setBean(null);
        binder.addValueChangeListener((HasValue.ValueChangeListener<HasValue.ValueChangeEvent<?>>) valueChangeEvent -> {
            Size value = Size.parse((String) valueChangeEvent.getValue());
            designWrapper.setWidth(value.getWidthAsString());
            designWrapper.setHeight(value.getHeightAsString());
            designWrapper.getStyle()
                    .set("resize", "none")
                    .set("border", "3px solid black")
                    .set("box-shadow", "var(--design-shadow-m)")
                    .set("border-radius", "10px");
            tabs.setSelectedTab(null);
        });
        sizes.setItems("1024x600", "1280x720", "1920x1080");
        sizes.addCustomValueSetListener(event -> sizes.setValue(event.getDetail()));
        sizes.setAllowCustomValue(true);
        designerViewSizeInput.addComponentAtIndex(0, sizes);

        wrapperContainer = new HorizontalLayout(designWrapper);
        wrapperContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        wrapperContainer.getStyle().set("overflow", "auto");
        wrapperContainer.setAlignItems(Alignment.CENTER);
        wrapperContainer.setSizeFull();

        designerViewSizeInput.getStyle().set("align-self", "flex-end");
        tabs.addSelectedChangeListener(this::checkTabIfPresentAndSetup);
        tabs.setSelectedTab(resizeableTab);
        add(header, wrapperContainer);
        setPadding(false);
        setSpacing(false);
    }

    protected void checkTabIfPresentAndSetup(final SelectedChangeEvent event) {
        if (event.getSelectedTab() != null) {
            sizes.setValue(null);
        }
        if (resizeableTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("98%");
            designWrapper.setHeight("98%");
            designWrapper.getStyle()
                    .set("resize", "both")
                    .set("border", "unset")
                    .set("box-shadow", "var(--design-shadow-xs)")
                    .set("border-radius", "0px");
        } else if (mobileTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("calc(2.81em * 7)");
            designWrapper.setHeight("calc(2.81em * 14)");
            designWrapper.getStyle()
                    .set("resize", "none")
                    .set("border", "3px solid black")
                    .set("box-shadow", "var(--design-shadow-m)")
                    .set("border-radius", "10px");
        } else if (mobileLandscapeTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("calc(2.81em * 14)");
            designWrapper.setHeight("calc(2.81em * 7)");
            designWrapper.getStyle()
                    .set("resize", "none")
                    .set("border", "3px solid black")
                    .set("box-shadow", "var(--design-shadow-m)")
                    .set("border-radius", "10px");
        } else if (tabletTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("calc(2.81em * 17)");
            designWrapper.setHeight("calc(2.81em * 24)");
            designWrapper.getStyle()
                    .set("resize", "none")
                    .set("border", "3px solid black")
                    .set("box-shadow", "var(--design-shadow-m)")
                    .set("border-radius", "10px");
        } else if (tabletLandscapeTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("calc(2.81em * 24)");
            designWrapper.setHeight("calc(2.81em * 17)");
            designWrapper.getStyle()
                    .set("resize", "none")
                    .set("border", "3px solid black")
                    .set("box-shadow", "var(--design-shadow-m)")
                    .set("border-radius", "10px");
        } else if (laptopTab.equals(event.getSelectedTab())) {
            designWrapper.setWidth("calc(2.81em * 33.2)");
            designWrapper.setHeight("calc(2.81em * 18.7)");
            designWrapper.getStyle()
                    .set("resize", "none")
                    .set("border", "3px solid black")
                    .set("box-shadow", "var(--design-shadow-l)")
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

class Size {

    private int width;
    private int height;

    public Size() {
    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    static Size parse(String value) {
        if (value.contains("x") && value.split("x").length == 2) {
            int x = Integer.parseInt(value.split("x")[0]);
            int y = Integer.parseInt(value.split("x")[1]);
            return new Size(x, y);
        }
        throw new NumberFormatException("invalid String");
    }

    static String present(Size size) {
        return size.getWidth() + "x" + size.getHeight();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getWidthAsString() {
        return width + "px";
    }

    public String getHeightAsString() {
        return height + "px";
    }

}