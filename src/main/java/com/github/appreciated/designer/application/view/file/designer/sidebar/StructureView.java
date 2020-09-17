package com.github.appreciated.designer.application.view.file.designer.sidebar;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.BaseView;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.helper.ComponentContainerHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.treegrid.TreeGrid;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class StructureView extends BaseView {
    private static final long serialVersionUID = -878124261779595119L;

    // Data
    private final TreeGrid<Component> grid;

    public StructureView(final ProjectFileModel projectFileModel) {
        super("Structure");

        grid = new TreeGrid<Component>();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER);
        grid.addHierarchyColumn(component -> component.getClass().getSimpleName());
        grid.setSizeFull();
        grid.setRowsDraggable(true);
        grid.setDropMode(GridDropMode.ON_TOP_OR_BETWEEN);
        grid.setDragFilter(component -> component != projectFileModel.getInformation().getComponent());
        grid.addDragStartListener(componentGridDragStartEvent -> projectFileModel.addCurrentDragItems(componentGridDragStartEvent.getDraggedItems()));

        //  Actual Parent Layout
        //      |
        //  DesignerComponentWrapper
        //      |
        //  Component

        grid.addDropListener(componentGridDropEvent -> {
            final GridDropLocation name = componentGridDropEvent.getDropLocation();

            if (projectFileModel.isCurrentDragAvailable() && name != null && componentGridDropEvent.getDropTargetItem().isPresent()) {
                final Set<Component> currentDragItems = projectFileModel.getAndCloneCurrentDragCollection();
                final Component component = componentGridDropEvent.getDropTargetItem().get();

                if (!currentDragItems.contains(component) || (currentDragItems.contains(component) && currentDragItems.size() > 1)) {
                    if (ComponentContainerHelper.isComponentContainer(component)) {
                        if (currentDragItems.contains(component)) {
                            currentDragItems.remove(component);
                            projectFileModel.removeCurrentDragItems(component);
                        }
                    	
                    	/*
                    	if (event.getComponent() instanceof DesignerComponentWrapper) {
                            Component actualComponent = ((DesignerComponentWrapper) event.getComponent()).getActualComponent();
                            Component dragSourceComponent = event.getDragSourceComponent().get();
                            if (dragSourceComponent instanceof DesignerComponentLabel) {
                                dragSourceComponent = transformDesignerComponentLabel(dragSourceComponent);
                            }
                            if (actualComponent instanceof HasOrderedComponents) {
                                ((HasOrderedComponents<?>) actualComponent).add(dragSourceComponent);
                            }
                            notifyStructureListeners();
                        }
                    	*/

                        for (Component item : currentDragItems) {
                            System.out.println(item.getParent().get());
                            System.out.println(item.getParent().get().getParent().get());
                        }

                        switch (name) {
                            case BELOW:
                            case ABOVE:
                                component.getParent().ifPresent(parent -> {
                                    if (parent instanceof DesignerComponentWrapper) {
                                        parent.getParent().ifPresent(actualParent -> {
                                            currentDragItems.forEach(draggedComponent -> ComponentContainerHelper.removeChild(draggedComponent.getParent().get().getParent().get(), draggedComponent.getParent().get()));
                                            currentDragItems.forEach(draggedComponent -> ComponentContainerHelper.addComponentAtIndex(actualParent,
                                                    actualParent.getElement().indexOfChild(parent.getElement()) + (name == GridDropLocation.BELOW ? 1 : 0),
                                                    draggedComponent.getParent().get()
                                            ));
                                            projectFileModel.removeCurrentDragItems(currentDragItems);
                                        });
                                    } else {
                                        log.error(parent.getClass().getName() + " not supported!");
                                    }
                                });
                                break;
                            case ON_TOP:
                                component.getParent().ifPresent(parent -> {
                                    if (parent instanceof DesignerComponentWrapper) {
                                        parent.getParent().ifPresent(actualParent -> {
                                            currentDragItems.forEach(draggedComponent -> ComponentContainerHelper.removeChild(draggedComponent.getParent().get().getParent().get(), draggedComponent.getParent().get()));
                                            currentDragItems.forEach(draggedComponent -> ComponentContainerHelper.addComponent(component, draggedComponent.getParent().get()));
                                            projectFileModel.removeCurrentDragItems(currentDragItems);
                                        });
                                    } else {
                                        log.error(parent.getClass().getName() + " not supported!");
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    } else {
                        Notification.show("Elements cannot be dropped on items that do not extend HasComponents");
                    }
                }
            }

            updateStructure(projectFileModel.getInformation().getComponent());
        });
        grid.addItemClickListener(event -> projectFileModel.getEventService().getFocusedEventPublisher().publish(event.getItem()));
        add(grid);
        projectFileModel.getEventService().getStructureChangedEventListener().addEventConsumer(event -> {
            System.out.println("StructureChangedEvent");
            if (event.getComponent() == projectFileModel.getInformation().getComponent()) {
                updateStructure(event.getComponent());
            }
        });
        projectFileModel.getEventService().getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            uiAccess(() -> {
                grid.select(elementFocusedEvent.getFocus());
            });
        });
        updateStructure(projectFileModel.getInformation().getComponent());
    }

    private void updateStructure(Component designRootComponent) {
        if (designRootComponent != null) {
            uiAccess(() -> {
                grid.setItems(Collections.singletonList(unpack(designRootComponent)), component -> {
                    if (ComponentContainerHelper.isComponentContainer(component)) {
                        return unpack(ComponentContainerHelper.getChildren(component)).collect(Collectors.toList());
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
