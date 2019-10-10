package com.github.appreciated.designer.view.designer.sidebar;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.view.BaseView;
import com.github.appreciated.designer.view.designer.DesignerView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.treegrid.TreeGrid;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureView extends BaseView {

    private final TreeGrid<Component> grid;
    private final UI ui;
    private List<Component> dragged;
    private DesignerView designerComponentView;


    public StructureView(ProjectService projectService, EventService eventService) {
        super("Structure");
        ui = UI.getCurrent();
        grid = new TreeGrid<>();
        grid.setThemeName("compact");
        grid.setThemeName("no-border");
        grid.addHierarchyColumn(component -> component.getClass().getSimpleName());
        grid.setSizeFull();
        grid.setRowsDraggable(true);
        grid.setDropMode(GridDropMode.ON_TOP_OR_BETWEEN);
        grid.setDragFilter(component -> component != projectService.getCurrentFile().getComponent());
        grid.addItemClickListener(event -> {
            event.getItem().getParent().ifPresent(component -> {

            });
        });

        grid.addDragStartListener(componentGridDragStartEvent -> dragged = componentGridDragStartEvent.getDraggedItems());

        //  Actual Parent Layout
        //      |
        //  DesignerComponentWrapper
        //      |
        //  Component

        grid.addDropListener(componentGridDropEvent -> {
            GridDropLocation name = componentGridDropEvent.getDropLocation();
            if (name == GridDropLocation.BELOW || name == GridDropLocation.ABOVE) {
                componentGridDropEvent.getDropTargetItem().ifPresent(component -> {
                    component.getParent().ifPresent(parent -> {
                        if (parent instanceof DesignerComponentWrapper) {
                            parent.getParent().ifPresent(actualParent -> {
                                dragged.forEach(draggedComponent -> ((HasComponents) draggedComponent.getParent().get().getParent().get()).remove(draggedComponent.getParent().get()));
                                dragged.forEach(draggedComponent -> ((HasComponents) actualParent).addComponentAtIndex(
                                        actualParent.getElement().indexOfChild(parent.getElement()) + (name == GridDropLocation.BELOW ? 1 : 0),
                                        draggedComponent.getParent().get()
                                ));
                            });
                        } else {
                            System.err.println(parent.getClass().getName() + " not supported!");
                        }
                    });
                });
            } else if (name == GridDropLocation.ON_TOP) {
                componentGridDropEvent.getDropTargetItem().ifPresent(component -> {
                    if (component instanceof HasComponents) {
                        component.getParent().ifPresent(parent -> {
                            if (parent instanceof DesignerComponentWrapper) {
                                parent.getParent().ifPresent(actualParent -> {
                                    dragged.forEach(draggedComponent -> ((HasComponents) draggedComponent.getParent().get().getParent().get()).remove(draggedComponent.getParent().get()));
                                    dragged.forEach(draggedComponent -> ((HasComponents) component).add(draggedComponent.getParent().get()));
                                });
                            } else {
                                System.err.println(parent.getClass().getName() + " not supported!");
                            }
                        });
                    } else {
                        Notification.show("Elements cannot be dropped on items that do not extend HasComponents");
                    }
                });
            }
            updateStructure(projectService.getCurrentFile().getComponent());
        });
        grid.addItemClickListener(event -> eventService.getFocusedEventPublisher().publish(event.getItem()));
        add(grid);
        eventService.getStructureChangedEventListener().addEventConsumer(event -> {
            System.out.println("StructureChangedEvent");
            if (event.getComponent() == projectService.getCurrentFile().getComponent()) {
                updateStructure(event.getComponent());
            }
        });
        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            getUI().ifPresent(ui1 -> ui1.access(() -> {
                grid.select(elementFocusedEvent.getFocus());
            }));
        });
        updateStructure(projectService.getCurrentFile().getComponent());
    }

    private void updateStructure(Component designRootComponent) {
        if (designRootComponent != null) {
            ui.access(() -> {
                grid.setItems(Collections.singletonList(unpack(designRootComponent)), component -> {
                    if (component instanceof HasComponents) {
                        return unpack(component.getChildren()).collect(Collectors.toList());
                    } else {
                        return Collections.emptyList();
                    }
                });
                grid.expandRecursively(Stream.of(unpack(designRootComponent)), 30);
                grid.getDataProvider().refreshAll();
            });
        } else {
            grid.setItems(Collections.emptyList());
        }
    }

    private Component unpack(Component designRootComponent) {
        if (designRootComponent instanceof DesignerComponentWrapper) {
            return ((DesignerComponentWrapper) designRootComponent).getActualComponent();
        } else {
            return designRootComponent;
        }
    }

    private Stream<Component> unpack(Stream<Component> children) {
        return children.map(this::unpack);
    }
}
