package com.github.appreciated.designer.application.view.file.designer.template;

import com.github.appreciated.designer.application.component.DesignerComponent;
import com.github.appreciated.designer.application.component.DesignerComponentWrapper;
import com.github.appreciated.designer.application.component.designer.DesignerComponentLabel;
import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.service.ComponentService;
import com.github.appreciated.designer.application.view.BaseView;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DesignerComponentsView extends BaseView {
    private static final long serialVersionUID = 3391173526041012926L;

    private final Map<String, VerticalLayout> componentsCategories;
    private final Map<String, List<DesignerComponentLabel>> components;
    private final VerticalLayout componentsContainer;
    private final ProjectFileModel projectFileModel;


    public DesignerComponentsView(final ProjectFileModel projectFileModel) {
        super("components");
        this.projectFileModel = projectFileModel;

        componentsCategories = new LinkedHashMap<>();
        components = new LinkedHashMap<>();

        componentsContainer = new VerticalLayout();
        componentsContainer.setPadding(false);
        componentsContainer.setSpacing(false);
        componentsContainer.setMargin(false);
        ComponentService service = new ComponentService();
        TextField search = new TextField();
        search.setPlaceholder(getTranslation("search"));
        search.setWidthFull();
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPrefixComponent(VaadinIcon.SEARCH.create());
        search.setClearButtonVisible(true);
        search.addValueChangeListener(event -> initAccordions(event.getValue()));
        add(search);
        components.put(getTranslation("vaadin.layouts"), new ArrayList<>());
        components.put(getTranslation("project.components"), new ArrayList<>());
        components.put(getTranslation("vaadin.components"), new ArrayList<>());
        components.put(getTranslation("html.components"), new ArrayList<>());
        service.getVaadinLayoutsComponents()
                .forEach(component -> initDragAndDrop(component, getTranslation("vaadin.layouts")));
        service.getProjectComponents(projectFileModel.getInformation().getProject())
                .forEach(component -> initDragAndDrop(component, getTranslation("project.components")));
        service.getVaadinComponents()
                .forEach(component -> initDragAndDrop(component, getTranslation("vaadin.components")));
        service.getHtmlComponents()
                .forEach(component -> initDragAndDrop(component, getTranslation("html.components")));

        components.keySet().forEach(key -> {
            VerticalLayout accordionPanel = new VerticalLayout();
            accordionPanel.setMargin(false);
            accordionPanel.setSpacing(false);
            accordionPanel.setPadding(false);
            accordionPanel.add(new H4(key));
            VerticalLayout wrapper = new VerticalLayout();
            wrapper.setMargin(false);
            wrapper.setPadding(false);
            wrapper.setSpacing(false);
            wrapper.getStyle().set("border-radius", "var(--lumo-border-radius-m)")
                    .set("box-shadow", "var(--lumo-box-shadow-xs)")
                    .set("background", "var(--lumo-primary-contrast-color)");
            accordionPanel.add(wrapper);
            componentsCategories.put(key, wrapper);
            componentsContainer.add(accordionPanel);
        });
        initAccordions(null);
        add(componentsContainer);
        componentsContainer.setSizeFull();
        setSpacing(false);
    }

    private void initAccordions(String name) {
        componentsCategories.forEach((s, verticalLayout) -> verticalLayout.removeAll());
        components.forEach((key, value) -> value.stream()
                .filter(designerComponentLabel -> name == null || designerComponentLabel.getName().toLowerCase().contains(name.toLowerCase()))
                .forEach(designerComponentLabel -> componentsCategories.get(key).add(designerComponentLabel)));
    }


    private void initDragAndDrop(DesignerComponent component, String key) {
        DesignerComponentLabel label = new DesignerComponentLabel(component);
        DragSource<DesignerComponentLabel> source = DragSource.create(label);

        AtomicReference<DesignerComponentWrapper> currentComponent = new AtomicReference<>();
        source.addDragStartListener(e -> {
            currentComponent.set(generateDesignerComponentWrapper(label.getDesignerComponent()));
            projectFileModel.setCurrentDragItem(currentComponent.get());
            projectFileModel.getEventService().getDesignerComponentDragEventPublisher().publish(currentComponent.get(), true);
        });
        source.addDragEndListener(e -> {
            projectFileModel.removeCurrentDragItem();
            projectFileModel.getEventService().getDesignerComponentDragEventPublisher().publish(currentComponent.get(), false);
        });
        components.get(key).add(label);
    }

    DesignerComponentWrapper generateDesignerComponentWrapper(DesignerComponent component) {
        DesignerComponentWrapper designerComponentWrapper = component.generateComponent(projectFileModel);
        designerComponentWrapper.setNonNestedClickListener(() ->
                projectFileModel.getEventService().getFocusedEventPublisher().publish(designerComponentWrapper.getActualComponent())
        );
        designerComponentWrapper.setRemoveListener(() ->
                projectFileModel.getEventService().getDesignerComponentRemovedEventPublisher().publish(designerComponentWrapper)
        );
        return designerComponentWrapper;
    }
}
