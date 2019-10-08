package com.github.appreciated.designer.view.designer.template;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.component.designer.DesignerComponentLabel;
import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.view.BaseView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;


public class DesignView extends BaseView {
    private final VerticalLayout designWrapper;
    private final HorizontalLayout designHolder;

    private ProjectService projectService;
    private EventService eventService;
    private DesignerComponentWrapper currentFocus;

    public DesignView(ProjectService projectService, EventService eventService) {
        super("Design");
        this.projectService = projectService;
        this.eventService = eventService;

        designHolder = new HorizontalLayout();
        designHolder.setPadding(true);
        designHolder.getElement().getClassList().add("design-holder");
        H3 h3 = new H3("Drop a Component");
        h3.getElement().setAttribute("style", "margin-bottom: unset;margin-top: unset;pointer-events:none;");
        designHolder.add(h3);

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

        if (projectService.getCurrentFile().getComponent() == null) {
            DropTarget<HorizontalLayout> dropTarget = DropTarget.create(designHolder);
            dropTarget.addDropListener(dropEvent -> {
                dropEvent.getDragSourceComponent().ifPresent(dragSourceExtension -> {
                    Component droppedRoot = transformDesignerComponentLabel(dragSourceExtension);
                    designWrapper.removeAll();
                    designWrapper.add(droppedRoot);
                    projectService.getCurrentFile().setComponent(droppedRoot);
                });
            });
        } else {
            designWrapper.removeAll();
            Component component = projectService.getCurrentFile().getComponent();
            designWrapper.add(component);
            initDesignerComponentWrapper((DesignerComponentWrapper) component);
            if (((DesignerComponentWrapper) component).getActualComponent() instanceof HasComponents) {
                ((DesignerComponentWrapper) component).getActualComponent().getChildren().forEach(component1 -> initDesignerComponentWrapper((DesignerComponentWrapper) component1));
            }
            initDragAndDropListeners(component);
            addDropListeners(component);
        }

        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> focusElementVisually(elementFocusedEvent.getParent()));
        eventService.getThemeChangedEventListener().addEventConsumer(themeChangedEvent -> {
            CssVariable var = themeChangedEvent.getCssVariable();
            getUI().ifPresent(ui -> ui.access(() -> {
                designWrapper.getElement().getStyle().set(var.getVariable().getVariableName(), var.getValue());
            }));
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        notifyStructureListeners();
    }

    private Icon getRotatedIcon(VaadinIcon icon, int degree) {
        Icon i = icon.create();
        i.getStyle().set("transform", "rotate(" + degree + "deg)");
        return i;
    }

    private void initDragAndDropListeners(Component component) {
        if (component instanceof DesignerComponentWrapper) {
            initDesignerComponentWrapper((DesignerComponentWrapper) component);
            initDragAndDropListeners(((DesignerComponentWrapper) component).getActualComponent());
        } else {
            if (component instanceof HasComponents) {
                component.getChildren().forEach(this::initDragAndDropListeners);
            }
        }
    }

    private void addDropListeners(Component root) {
        if (root instanceof DesignerComponentWrapper) {
            addDropListeners(((DesignerComponentWrapper) root).getActualComponent());
        } else if (root instanceof HasComponents) {
            DropTarget<Component> dropTarget = DropTarget.create(root);
            dropTarget.setDropEffect(DropEffect.MOVE);
            dropTarget.addDropListener(this::handleDrop);
            root.getChildren().forEach(this::addDropListeners);
        }
    }

    private void handleDrop(DropEvent event) {
        event.getDragSourceComponent().ifPresent(extension -> {
            Component source = (Component) extension;
            // If a DesignerComponentLabel is dropped onto the root it needs to be transformed into the actual representation
            if (source instanceof DesignerComponentLabel) {
                Component transformedComponent = transformDesignerComponentLabel(source);
                // Anyways the element will be added to the drop source.
                if (event.getComponent() instanceof DesignerComponentWrapper) {
                    ((HasComponents) ((DesignerComponentWrapper) event.getComponent()).getActualComponent()).add(transformedComponent);
                } else if (event.getComponent() instanceof HasComponents) {
                    ((HasComponents) event.getComponent()).add(transformedComponent);
                }
            } else {
                if (event.getComponent() instanceof DesignerComponentWrapper) {
                    Component actualComponent = ((DesignerComponentWrapper) event.getComponent()).getActualComponent();
                    event.getComponent().getParent().ifPresent(parent -> {
                        if (parent instanceof HasComponents) {
                            ((HasComponents) parent).remove(source);
                        }
                    });
                    if (actualComponent instanceof HasComponents) {
                        ((HasComponents) actualComponent).add(source);
                    }
                } else {
                    throw new IllegalStateException("This should never happen");
                }
            }
            notifyStructureListeners();
        });
    }

    private Component transformDesignerComponentLabel(Component component) {
        DesignerComponentWrapper generatedComponent = ((DesignerComponentLabel) component).getDesignerComponent().generateComponent();
        initDesignerComponentWrapper(generatedComponent);
        return generatedComponent;
    }

    private void initDesignerComponentWrapper(DesignerComponentWrapper generatedComponent) {
        generatedComponent.setNonNestedClickListener(o -> {
            System.out.println(generatedComponent.getActualComponent().getClass().getSimpleName());
            notifyFocusListeners(generatedComponent.getActualComponent(), generatedComponent);
        });

        // If the transformed element is from type #{com.vaadin.flow.component.HasComponents} it needs to be possible to drop elements in it
        if (generatedComponent.getActualComponent() instanceof HasComponents) {
            DropTarget<Component> dropTarget = DropTarget.create(generatedComponent);
            dropTarget.addDropListener(event -> handleDrop(event));
        }
    }

    private void focusElementVisually(DesignerComponentWrapper generatedComponent) {
        if (currentFocus != null) {
            currentFocus.setFocus(false);
        }
        generatedComponent.setFocus(true);
        currentFocus = generatedComponent;
    }

    private void notifyStructureListeners() {
        eventService.getStructureChangedEventPublisher().publish(projectService.getCurrentFile().getComponent());
    }

    private void notifyFocusListeners(Component actualComponent, DesignerComponentWrapper parent) {
        eventService.getFocusedEventPublisher().publish(actualComponent, parent);
    }

    public Component getRootComponent() {
        return projectService.getCurrentFile().getComponent();
    }
}
