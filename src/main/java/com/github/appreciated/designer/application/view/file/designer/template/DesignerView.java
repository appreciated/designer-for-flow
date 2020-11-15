package com.github.appreciated.designer.application.view.file.designer.template;

import com.github.appreciated.designer.application.component.CustomSelect;
import com.github.appreciated.designer.application.component.IconButton;
import com.github.appreciated.designer.application.view.BaseView;
import com.vaadin.componentfactory.Tooltip;
import com.vaadin.componentfactory.TooltipAlignment;
import com.vaadin.componentfactory.TooltipPosition;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import lombok.Getter;

@Getter
public class DesignerView extends BaseView {
    private static final long serialVersionUID = -5707329831685871000L;

    // Layout
    protected final Div designWrapper;
    protected final Div designHolder;
    protected final Div wrapperContainer;

    // Header
    protected final HorizontalLayout header;
    protected final H3 title;

    private final CustomSelect<Size> sizes;
    private final IconButton rotateButton;
    private final IconButton undoButton;
    private final IconButton redoButton;
    private final Button saveButton;

    public DesignerView() {
        super();

        HorizontalLayout designerViewSizeInput = new HorizontalLayout();

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

        designHolder = new Div();
        designWrapper = new Div();
        designWrapper.getElement().getClassList().add("design-view");
        designWrapper.add(designHolder);
        designWrapper.getStyle()
                .set("flex-shrink", "0")
                .set("margin", "auto")
                .set("overflow", "hidden");
        designWrapper.setWidth("98%");
        designWrapper.setHeight("98%");

        sizes = new CustomSelect<>();
        sizes.setInputRenderer((size, sizeConsumer) -> {
            HorizontalLayout wrapper = new HorizontalLayout();
            wrapper.setAlignItems(Alignment.CENTER);
            wrapper.setWidthFull();
            wrapper.getElement().setAttribute("theme", "spacing-s");
            TextField width = new TextField();
            width.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
            width.setPlaceholder(getTranslation("width"));
            width.setWidth("70px");
            width.getStyle()
                    .set("flex", "1 1")
                    .set("min-width", "0")
                    .set("--lumo-contrast-10pct", "transparent");
            TextField height = new TextField();
            height.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
            height.setPlaceholder(getTranslation("height"));
            height.getStyle()
                    .set("flex", "1 1")
                    .set("min-width", "0")
                    .set("--lumo-contrast-10pct", "transparent");
            Span x = new Span("x");
            height.setWidth("70px");
            if (size.isPresent() && !size.get().isForceAlternativeDisplay()) {
                width.setValue(size.get().getWidth());
                height.setValue(size.get().getHeight());
            }
            HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<TextField, String>> listener = changeEvent -> {
                if (width.getValue() != null && height.getValue() != null) {
                    sizeConsumer.accept(new Size("", height.getValue(), width.getValue()));
                } else if (size.isPresent()) {
                    if (width.getValue() != null) {
                        sizeConsumer.accept(new Size("", size.get().getHeight(), width.getValue()));
                    } else if (height.getValue() != null) {
                        sizeConsumer.accept(new Size("", height.getValue(), size.get().getWidth()));
                    }
                }
            };
            width.addValueChangeListener(listener);
            height.addValueChangeListener(listener);
            wrapper.add(width, x, height);
            wrapper.setSpacing(false);
            wrapper.setWidthFull();
            return wrapper;
        });
        sizes.setItemLabelGenerator(Size::getName);

        //sizes.setPlaceholder(getTranslation("select.a.resolution"));
        sizes.setItems(
                new Size("Fullsize", "98%", "98%", true),
                new Size("1024x600", "1024px", "600px"),
                new Size("1280x720", "1280px", "720px"),
                new Size("1920x1080", "1920px", "1080px"),
                new Size("Phone", "calc(2.81em * 7)", "calc(2.81em * 14)", true),
                new Size("Tablet", "calc(2.81em * 17)", "calc(2.81em * 24)", true),
                new Size("Laptop", "calc(2.81em * 33.2)", "calc(2.81em * 18.7)", true)
        );

        sizes.setValueChangeListener(size -> setWithAndHeight(size.getWidth(), size.getHeight()));

        rotateButton = new IconButton(VaadinIcon.ROTATE_RIGHT.create(), buttonClickEvent -> {
            String currentHeight = designWrapper.getHeight();
            String currentWidth = designWrapper.getWidth();
            designWrapper.setWidth(currentHeight);
            designWrapper.setHeight(currentWidth);
        });
        Tooltip rotateTooltip = new Tooltip(rotateButton);
        rotateTooltip.setPosition(TooltipPosition.BOTTOM);
        rotateTooltip.setAlignment(TooltipAlignment.CENTER);
        rotateTooltip.add(new Span(getTranslation("rotate")));

        undoButton = new IconButton(VaadinIcon.ARROW_LEFT.create());
        Tooltip undoTooltip = new Tooltip(undoButton);
        undoTooltip.setPosition(TooltipPosition.BOTTOM);
        undoTooltip.setAlignment(TooltipAlignment.CENTER);
        undoTooltip.add(new Span(getTranslation("undo")));

        redoButton = new IconButton(VaadinIcon.ARROW_RIGHT.create());
        Tooltip redoTooltip = new Tooltip(redoButton);
        redoTooltip.setPosition(TooltipPosition.BOTTOM);
        redoTooltip.setAlignment(TooltipAlignment.CENTER);
        redoTooltip.add(new Span(getTranslation("redo")));

        saveButton = new Button("\uD83D\uDCBE");
        saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Tooltip saveTooltip = new Tooltip(saveButton);
        saveTooltip.setPosition(TooltipPosition.BOTTOM);
        saveTooltip.setAlignment(TooltipAlignment.RIGHT);
        saveTooltip.add(new Span(getTranslation("save")));

        designerViewSizeInput.add(
                undoButton, undoTooltip,
                redoButton, redoTooltip,
                sizes,
                rotateButton, rotateTooltip,
                saveButton, saveTooltip
        );

        wrapperContainer = new Div(designWrapper);
        wrapperContainer.getStyle().set("overflow", "auto");
        wrapperContainer.getStyle().set("display", "flex");
        wrapperContainer.setSizeFull();

        designerViewSizeInput.getStyle().set("align-self", "flex-end");

        add(header, wrapperContainer);
        setPadding(false);
        setSpacing(false);
    }

    private void setWithAndHeight(String width, String height) {
        designWrapper.setWidth(width);
        designWrapper.setHeight(height);
        designWrapper.getStyle()
                .set("resize", "none")
                .set("border", "3px solid black")
                .set("box-shadow", "var(--design-shadow-m)")
                .set("border-radius", "10px");
    }
}

class Size {

    private String name;
    private boolean forceAlternativeDisplay;
    private String width;
    private String height;

    public Size(String name, String width, String height) {
        this(name, width, height, false);
    }

    public Size(String name, String width, String height, boolean forceAlternativeDisplay) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.forceAlternativeDisplay = forceAlternativeDisplay;
    }

    static String present(Size size) {
        return size.getWidth() + "x" + size.getHeight();
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public boolean isForceAlternativeDisplay() {
        return forceAlternativeDisplay;
    }


}