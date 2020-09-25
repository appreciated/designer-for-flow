package com.github.appreciated.designer.application.presenter.file.designer.template;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.presenter.file.designer.template.draghandler.AccordionPanelDragTargetHandler;
import com.github.appreciated.designer.application.presenter.file.designer.template.draghandler.DragTargetHandler;
import com.github.appreciated.designer.application.presenter.file.designer.template.drophandler.AccordionPanelDropHandler;
import com.github.appreciated.designer.application.presenter.file.designer.template.drophandler.DropHandler;
import com.github.appreciated.designer.application.view.file.designer.template.DesignerView;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.component.DropTargetAccordionPanel;
import com.github.appreciated.designer.component.DropTargetComponent;
import com.github.appreciated.designer.component.DropTargetDiv;
import com.github.appreciated.designer.helper.ComponentContainerHelper;
import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.mvp.Presenter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.dnd.EffectAllowed;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;
import static com.github.appreciated.designer.helper.ComponentContainerHelper.removeChild;

@CssImport(value = "./styles/presenter-styles-vaadin-accordion-panel.css", themeFor = "vaadin-accordion-panel")
public class DesignerPresenter extends Presenter<ProjectFileModel, DesignerView> {

    private final DesignerComponentWrapper designerRoot;
    private final DropHandler[] dropHandlers;
    private final DragTargetHandler[] dragTargetHandlers;
    private final EventService eventService;
    private final ProjectFileModel projectFileModel;
    private DesignerComponentWrapper currentFocus;

    public DesignerPresenter(ProjectFileModel projectFileModel) {
        super(projectFileModel);
        eventService = projectFileModel.getEventService();
        this.projectFileModel = projectFileModel;

        getContent().getDesignWrapper().removeAll();
        designerRoot = (DesignerComponentWrapper) projectFileModel.getInformation().getComponent();
        getContent().getDesignWrapper().add(designerRoot);
        initDesignerComponentWrappers(designerRoot);

        DropTarget<Component> dropTarget = DropTarget.create(designerRoot);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(event -> {
            List<Component> components = designerRoot.getActualComponent().getChildren().collect(Collectors.toList());

            Component currentDragItem = projectFileModel.getCurrentDragItem() != null ? projectFileModel.getCurrentDragItem() : event.getDragSourceComponent().get();

            handleDropEvent(currentDragItem, components.get(components.size() - 1));
        });

        ComponentContainerHelper.getChildren(designerRoot.getActualComponent()).forEach(this::initDragAndDropListeners);

        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            if (elementFocusedEvent.getFocus() instanceof DesignerComponentWrapper) {
                focusElementVisually((DesignerComponentWrapper) elementFocusedEvent.getFocus());
            } else if (elementFocusedEvent.getFocus().getParent().isPresent() && elementFocusedEvent.getFocus().getParent().get() instanceof DesignerComponentWrapper) {
                focusElementVisually((DesignerComponentWrapper) elementFocusedEvent.getFocus().getParent().get());
            }
        });
        eventService.getThemeChangedEventListener().addEventConsumer(themeChangedEvent -> {
            CssVariable var = themeChangedEvent.getCssVariable();
            getUI().ifPresent(ui -> ui.access(() -> {
                getContent().getDesignWrapper().getElement().getStyle().set(var.getVariable().getVariableName(), var.getValue());
            }));
        });
        dropHandlers = new DropHandler[]{
                new AccordionPanelDropHandler(this)
        };
        dragTargetHandlers = new DragTargetHandler[]{
                new AccordionPanelDragTargetHandler(this)
        };
        eventService.getDesignerComponentDropListener().addEventConsumer(dropEvent -> {
            handleDropEvent((DesignerComponentWrapper) dropEvent.getDraggedComponent(), dropEvent.getTargetComponent());
        });
        eventService.getDesignerComponentDragListener().addEventConsumer(dragEvent -> {
            setHasComponentsFillerVisible(dragEvent.isStarted(), dragEvent.getDraggedComponent());
        });
    }

    public void setDragClass(boolean isDragging) {
        if (isDragging) {
            designerRoot.getClassNames().add("on-drag");
        } else {
            designerRoot.getClassNames().remove("on-drag");
        }
    }

    private void initDesignerComponentWrappers(DesignerComponentWrapper component) {
        initDesignerComponentWrapper(component);
        if (isComponentContainer(component.getActualComponent())) {
            ComponentContainerHelper.getChildren(component.getActualComponent()).forEach(component1 -> {
                if (!(component1 instanceof AccordionPanel)) {
                    initDesignerComponentWrappers((DesignerComponentWrapper) component1);
                }
            });
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        notifyStructureListeners();
    }

    private void notifyStructureListeners() {
        getModel().getEventService().getStructureChangedEventPublisher().publish(getModel().getInformation().getComponent());
    }

    private void initDragAndDropListeners(Component component) {
        if (component instanceof DesignerComponentWrapper) {
            initDesignerComponentWrapper((DesignerComponentWrapper) component);
            addDragListener(component);
            addDropListener(component);
            if (isComponentContainer(((DesignerComponentWrapper) component).getActualComponent())) {
                ComponentContainerHelper.getChildren(((DesignerComponentWrapper) component).getActualComponent()).forEach(this::initDragAndDropListeners);
            }
        }
    }

    private void addDropListener(Component component) {
        DropTarget<Component> dropTarget = DropTarget.create(component);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(event -> {
            Component currentDraggedItem = projectFileModel.getCurrentDragItem();
            handleDropEvent((DesignerComponentWrapper) (currentDraggedItem == null ? event.getDragSourceComponent().get() : currentDraggedItem), event.getComponent());
        });
    }

    private void addDragListener(Component component) {
        DragSource<Component> dragSource = DragSource.create(component);
        dragSource.setEffectAllowed(EffectAllowed.ALL);
        dragSource.addDragStartListener(event -> setHasComponentsFillerVisible(true, (DesignerComponentWrapper) event.getComponent()));
        dragSource.addDragEndListener(event -> setHasComponentsFillerVisible(false, (DesignerComponentWrapper) event.getComponent()));
    }

    private synchronized void setHasComponentsFillerVisible(boolean isVisible, DesignerComponentWrapper component) {
        if (isVisible) {
            addHasComponentsFiller(designerRoot, component);
            setDragClass(isVisible);
        } else {
            removeHasComponentsFiller(designerRoot);
            notifyStructureListeners();
            setDragClass(isVisible);
        }
    }

    private void addHasComponentsFiller(Component designerComponentWrapper, DesignerComponentWrapper draggedComponent) {
        if (isComponentContainer(unwrapDesignerComponent(designerComponentWrapper))) {
            ComponentContainerHelper.getChildren(unwrapDesignerComponent(designerComponentWrapper)).collect(Collectors.toList()).forEach(component -> {
                if (component instanceof DesignerComponentWrapper) {
                    addHasComponentsFiller(component, draggedComponent);
                } else {
                    addHasComponentsFiller(component, draggedComponent);
                }
            });
            if (!dragTargetWasHandled(draggedComponent, designerComponentWrapper)) {
                ComponentContainerHelper.addComponent(unwrapDesignerComponent(designerComponentWrapper), getDropTargetDiv(designerComponentWrapper));
            }
        }
    }

    public Component unwrapDesignerComponent(Component component) {
        if (component instanceof DesignerComponentWrapper) {
            return ((DesignerComponentWrapper) component).getActualComponent();
        } else {
            return component;
        }
    }

    private void removeHasComponentsFiller(Component componentContainer) {
        Component unwrappedComponent = unwrapDesignerComponent(componentContainer);
        if (isComponentContainer(unwrappedComponent)) {
            ComponentContainerHelper.getChildren(unwrappedComponent).collect(Collectors.toList()).forEach(component -> {
                Component unwrappedChild = unwrapDesignerComponent(component);
                if (isComponentContainer(unwrappedChild) && !(unwrappedChild instanceof DropTargetComponent)) {
                    removeHasComponentsFiller(unwrappedChild);
                }
                if (unwrappedChild instanceof DropTargetComponent) {
                    ComponentContainerHelper.removeChild(component.getParent().get(), unwrappedChild);
                }
            });
        }
    }

    Component getDropTargetDiv(Component target) {
        Component div = getDropTargetComponent(target);
        DropTarget<Component> dropTarget = DropTarget.create(div);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(event -> {
            Component currentDraggedItem = projectFileModel.getCurrentDragItem();
            handleDropEvent((DesignerComponentWrapper) (currentDraggedItem == null ? event.getDragSourceComponent().get() : currentDraggedItem), event.getComponent());
            removeHasComponentsFiller(designerRoot);
        });
        return div;
    }

    Component getDropTargetComponent(Component target) {
        if (unwrapDesignerComponent(target) instanceof Accordion) {
            return new DropTargetAccordionPanel();
        } else {
            return new DropTargetDiv();
        }
    }

    private void handleDropEvent(Component draggedComponent, Component targetComponent) {
        // If a DesignerComponentLabel is dropped onto the root it needs to be transformed into the actual representation
        if (!dropWasHandled(draggedComponent, targetComponent)) {
            draggedComponent.getParent().ifPresent(component -> {
                if (isComponentContainer(component)) {
                    removeChild(component, draggedComponent);
                }
            });
            if (targetComponent instanceof DesignerComponentWrapper || targetComponent instanceof DropTargetDiv) {
                targetComponent.getParent().ifPresent(component1 -> {
                    if (isComponentContainer(component1)) {
                        ComponentContainerHelper.addComponentAtIndex(component1, ComponentContainerHelper.indexOf(component1, targetComponent), draggedComponent);
                    }
                });
            }
        }
        getModel().getEventService().getFocusedEventPublisher().publish(draggedComponent);
    }

    private boolean dropWasHandled(Component draggedComponent, Component targetComponent) {
        if (draggedComponent instanceof DesignerComponentWrapper) {
            Optional<DropHandler> handler = Arrays.stream(dropHandlers).filter(dropHandler1 -> dropHandler1.canHandleDropEvent((DesignerComponentWrapper) draggedComponent, targetComponent)).findFirst();
            if (handler.isPresent()) {
                handler.get().handleDropEvent((DesignerComponentWrapper) draggedComponent, targetComponent);
                return true;
            }
        }
        return false;
    }

    private boolean dragTargetWasHandled(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        Optional<DragTargetHandler> handler = Arrays.stream(dragTargetHandlers).filter(dropHandler1 -> dropHandler1.canHandleDragTargetEvent(draggedComponent, targetComponent)).findFirst();
        if (handler.isPresent()) {
            if (handler.get().showDropTargetInComponent(draggedComponent, targetComponent)) {
                ComponentContainerHelper.addComponent(unwrapDesignerComponent(targetComponent), getDropTargetDiv(targetComponent));
            }
            return true;
        } else {
            return false;
        }
    }

    private void initDesignerComponentWrapper(DesignerComponentWrapper generatedComponent) {
        generatedComponent.setNonNestedClickListener(o -> {
            notifyFocusListeners(generatedComponent.getActualComponent());
        });
    }

    private void focusElementVisually(DesignerComponentWrapper generatedComponent) {
        if (currentFocus != null) {
            currentFocus.setFocus(false);
        }
        generatedComponent.setFocus(true);
        currentFocus = generatedComponent;
    }

    private void notifyFocusListeners(Component actualComponent) {
        getModel().getEventService().getFocusedEventPublisher().publish(actualComponent);
    }

}
