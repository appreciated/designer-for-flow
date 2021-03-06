package com.github.appreciated.designer.application.view.file.designer.sidebar;

import com.github.appreciated.designer.application.component.ComponentPropertyParser;
import com.github.appreciated.designer.application.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.application.component.designer.StyleEditor;
import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.BaseView;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.Renderers;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces.HasStyleRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.property.AbstractPropertyRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


public class PropertiesView extends BaseView {
    private final VerticalLayout properties;
    private final Renderers renderers;

    public PropertiesView(ProjectFileModel projectFileModel) {
        super("properties");
        properties = new VerticalLayout();
        properties.setSizeFull();
        properties.setPadding(false);
        properties.setMargin(false);
        properties.setSpacing(false);
        add(properties);
        renderers = new Renderers(projectFileModel);
        projectFileModel.getEventService()
                .getFocusedEventListener()
                .addEventConsumer(elementFocusedEvent ->
                        getUI().ifPresent(ui -> ui.access(() -> onFocus(elementFocusedEvent.getFocus())))
                );
        properties.getStyle().set("overflow", "hidden");
    }

    private void onFocus(Component propertyParent) {
        properties.removeAll();
        if (propertyParent != null) {
            ComponentPropertyParser parser = new ComponentPropertyParser(propertyParent);
            parser.getProperties()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entrySet -> addRenderer(properties, entrySet, propertyParent));

            renderers.getPropertyRenderers().stream()
                    .filter(propertyRenderer -> propertyRenderer.canRender(propertyParent))
                    .map(render -> (Stream<RenderPair>) render.render(propertyParent))
                    .forEach(renderers -> renderers.forEach(renderPair ->
                            properties.add(getLabelComponent(renderPair.getPropertyName(), renderPair.getPropertyComponent()))
                    ));

            HasStyleRenderer renderer = new HasStyleRenderer();
            if (renderer.canRender(propertyParent)) {
                Stream<RenderPair> component = renderer.render((HasStyle) propertyParent);
                RenderPair pair = component.findFirst().get();
                StyleEditor details = (StyleEditor) pair.getPropertyComponent();
                renderers.getPropertyRenderers().stream()
                        .filter(interfaceRenderer -> interfaceRenderer.canRender(propertyParent))
                        .forEach(interfaceRenderer -> details.removeRenderedStyles(interfaceRenderer.rendersCssStyle()));
                addRenderedComponent(properties, pair.getPropertyName(), details);
            }
        }
    }

    private void addRenderer(HasComponents formLayout, Map.Entry<String, CustomPropertyDescriptor> entrySet, Component propertyParent) {
        if (renderers.getPropertyRenderers()
                .stream()
                .filter(abstractPropertyRenderer -> abstractPropertyRenderer.canRender(propertyParent))
                .noneMatch(abstractPropertyRenderer -> abstractPropertyRenderer.rendersProperty().anyMatch(o -> o.equals(entrySet.getKey())))
        ) {

            Optional<AbstractPropertyRenderer> fittingRenderer = renderers.getTypeRenderers().stream()
                    .filter(propertyRenderer -> propertyRenderer.canRender(propertyParent, entrySet.getKey(), entrySet.getValue()))
                    .findFirst();
            fittingRenderer.ifPresent(propertyRenderer ->
                    addRenderedComponent(formLayout, entrySet.getKey(), propertyRenderer.render(entrySet.getKey(), entrySet.getValue(), propertyParent))
            );

            if (!fittingRenderer.isPresent()) {
                System.out.println("The Property \"" + propertyParent.getClass().getName() + "." + entrySet.getKey() + "\" with type \"" + entrySet.getValue().getReadMethod().getReturnType().getName() + "\" cannot be rendered");
            }
        } else {

        }
    }

    private Component getLabelComponent(String caption, Component input) {
        HorizontalLayout hl = new HorizontalLayout();
        hl.getStyle()
                .set("--lumo-contrast-10pct", "transparent")
                .set("flex-shrink", "0")
                .set("min-height", "34px")
                .set("border-bottom", "1px solid var(--lumo-contrast-40pct)")
                .set("--lumo-contrast-10pct", "transparent");

        hl.setAlignItems(Alignment.CENTER);
        Span label = new Span(caption);
        label.getStyle().set("flex-basis", "110px");
        if (!(input instanceof Details)) {
            hl.add(label);
        }
        hl.add(input);

        hl.setWidthFull();
        if (!(input instanceof Checkbox)) {
            hl.setFlexGrow(1, input);
        } else {
            input.getElement().getStyle().set("margin-left", "auto");
        }
        return hl;
    }

    private void addRenderedComponent(HasComponents formLayout, String key, Component render) {
        formLayout.add(getLabelComponent(key, render));
    }
}


