package com.github.appreciated.designer.application.view.file.designer.sidebar;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.BaseView;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.Renderers;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces.HasStyleRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.type.AbstractPropertyRenderer;
import com.github.appreciated.designer.component.ComponentPropertyParser;
import com.github.appreciated.designer.component.designer.StyleEditorDetails;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.beans.PropertyDescriptor;
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
        add(properties);
        renderers = new Renderers(projectFileModel);
        projectFileModel.getEventService().getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            getUI().ifPresent(ui -> ui.access(() -> onFocus(elementFocusedEvent.getFocus())));
        });
    }

    private void onFocus(Component propertyParent) {
        ComponentPropertyParser parser = new ComponentPropertyParser(propertyParent);
        properties.removeAll();
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
            StyleEditorDetails details = (StyleEditorDetails) pair.getPropertyComponent();
            renderers.getPropertyRenderers().stream()
                    .filter(interfaceRenderer -> interfaceRenderer.canRender(propertyParent))
                    .forEach(interfaceRenderer -> details.removeRenderedStyles(interfaceRenderer.rendersCssStyle()));
            addRenderedComponent(properties, pair.getPropertyName(), details);
        }
    }

    private void addRenderer(HasComponents formLayout, Map.Entry<String, PropertyDescriptor> entrySet, Component propertyParent) {
        if (renderers.getPropertyRenderers()
                .stream()
                .filter(abstractPropertyRenderer -> abstractPropertyRenderer.canRender(propertyParent))
                .noneMatch(abstractPropertyRenderer -> abstractPropertyRenderer.rendersProperty().anyMatch(o -> o.equals(entrySet.getKey())))
        ) {

            Optional<AbstractPropertyRenderer> fittingRenderer = renderers.getTypeRenderers().stream()
                    .filter(propertyRenderer -> propertyRenderer.canRender(propertyParent, entrySet.getKey(), entrySet.getValue()))
                    .findFirst();
            fittingRenderer.ifPresent(propertyRenderer -> {
                addRenderedComponent(formLayout, entrySet.getKey(), propertyRenderer.render(entrySet.getKey(), entrySet.getValue(), propertyParent));
            });

            if (!fittingRenderer.isPresent()) {
                System.out.println("No Property component for \"" + entrySet.getKey() + "\" with type \"" + entrySet.getValue().getPropertyType().getName() + "\"");
            }
        } else {

        }
    }

    private Component getLabelComponent(String caption, Component input) {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setAlignItems(Alignment.CENTER);
        Label label = new Label(caption);
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
