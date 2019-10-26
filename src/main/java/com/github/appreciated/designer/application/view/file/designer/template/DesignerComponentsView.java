package com.github.appreciated.designer.application.view.file.designer.template;

import com.github.appreciated.designer.application.view.BaseView;
import com.github.appreciated.designer.component.designer.DesignerComponentLabel;
import com.github.appreciated.designer.service.ComponentService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.*;

public class DesignerComponentsView extends BaseView {
    private final TextField search;
    Map<String, VerticalLayout> componentsCategories = new HashMap<>();

    Map<String, List<DesignerComponentLabel>> components = new LinkedHashMap<>();
    Accordion accordion = new Accordion();


    public DesignerComponentsView() {
        super("Components");
        ComponentService service = new ComponentService();
        search = new TextField();
        search.setPlaceholder("Search ...");
        search.setWidthFull();
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPrefixComponent(VaadinIcon.SEARCH.create());
        search.setClearButtonVisible(true);
        search.addValueChangeListener(event -> initAccordions(event.getValue()));
        add(search);
        components.put("Vaadin Components", new ArrayList<>());
        components.put("HTML Components", new ArrayList<>());
        service.getAllComponents().forEach(component -> {
            DesignerComponentLabel label = new DesignerComponentLabel(component);
            DragSource<DesignerComponentLabel> source = DragSource.create(label);
            if (component.getTagName().startsWith("<vaadin")) {
                components.get("Vaadin Components").add(label);
            } else {
                components.get("HTML Components").add(label);
            }
        });
        components.keySet().forEach(key -> {
            AccordionPanel accordionPanel = new AccordionPanel();
            accordionPanel.setSummaryText(key);
            VerticalLayout wrapper = new VerticalLayout();
            accordionPanel.addContent(wrapper);
            componentsCategories.put(key, wrapper);
            accordion.add(accordionPanel);
        });
        initAccordions(null);
        add(accordion);
        accordion.setSizeFull();
    }

    private void initAccordions(String name) {
        componentsCategories.forEach((s, verticalLayout) -> verticalLayout.removeAll());
        components.forEach((key, value) -> value.stream()
                .filter(designerComponentLabel -> name == null || designerComponentLabel.getName().toLowerCase().contains(name.toLowerCase()))
                .forEach(designerComponentLabel -> componentsCategories.get(key).add(designerComponentLabel)));
    }
}
