package com.github.appreciated.designer.application.presenter.file.designer.template;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.presenter.file.designer.template.drophandler.DesignerComponentLabelDropHandler;
import com.github.appreciated.designer.application.presenter.file.designer.template.drophandler.DropHandler;
import com.github.appreciated.designer.application.view.file.designer.template.DesignerView;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.component.DropTargetDiv;
import com.github.appreciated.designer.component.designer.DesignerComponentLabel;
import com.github.appreciated.designer.helper.ComponentContainerHelper;
import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.mvp.Presenter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.dnd.EffectAllowed;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;
import static com.github.appreciated.designer.helper.ComponentContainerHelper.removeChild;

public class DesignerPresenter extends Presenter<ProjectFileModel, DesignerView> {

    private final DesignerComponentWrapper designerRoot;
    private final DropHandler[] dropHandlers;
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
            if (event.getComponent() instanceof DesignerComponentWrapper) {
                Component actualComponent = ((DesignerComponentWrapper) event.getComponent()).getActualComponent();
                Component dragSourceComponent = event.getDragSourceComponent().get();
                if (dragSourceComponent instanceof DesignerComponentLabel) {
                    dragSourceComponent = transformDesignerComponentLabel(dragSourceComponent);
                }
                if (isComponentContainer(actualComponent)) {
                    ComponentContainerHelper.addComponent(actualComponent, dragSourceComponent);
                }
                notifyStructureListeners();
            }
        });

        designerRoot.getActualComponent().getChildren().forEach(this::initDragAndDropListeners);

        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> focusElementVisually(elementFocusedEvent.getParent()));
        eventService.getThemeChangedEventListener().addEventConsumer(themeChangedEvent -> {
            CssVariable var = themeChangedEvent.getCssVariable();
            getUI().ifPresent(ui -> ui.access(() -> {
                getContent().getDesignWrapper().getElement().getStyle().set(var.getVariable().getVariableName(), var.getValue());
            }));
        });
        dropHandlers = new DropHandler[]{
                new DesignerComponentLabelDropHandler(this)
        };
        eventService.getDesignerComponentDropListener().addEventConsumer(dropEvent -> {
            handleDropEvent(dropEvent.getDraggedComponent(), dropEvent.getTargetComponent());
        });
        eventService.getDesignerComponentDragListener().addEventConsumer(dragEvent -> {
            setHasComponentsFillerVisible(dragEvent.isStarted());
        });
    }

    private void initDesignerComponentWrappers(DesignerComponentWrapper component) {
        initDesignerComponentWrapper(component);
        if (isComponentContainer(component.getActualComponent())) {
            ComponentContainerHelper.getChildren(component.getActualComponent()).forEach(component1 -> initDesignerComponentWrappers((DesignerComponentWrapper) component1));
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
        } else {
            throw new IllegalStateException("This should never happen");
        }
    }

    private void addDropListener(Component component) {
        DropTarget<Component> dropTarget = DropTarget.create(component);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(event -> {
            Component currentDraggedItem = projectFileModel.getCurrentDragItem();
            handleDropEvent(currentDraggedItem == null ? event.getDragSourceComponent().get() : currentDraggedItem, event.getComponent());
        });
    }

    private void addDragListener(Component component) {
        DragSource<Component> dragSource = DragSource.create(component);
        dragSource.setEffectAllowed(EffectAllowed.ALL);
        dragSource.addDragStartListener(event -> setHasComponentsFillerVisible(true));
        dragSource.addDragEndListener(event -> setHasComponentsFillerVisible(false));
    }

    private synchronized void setHasComponentsFillerVisible(boolean isVisible) {
        if (isVisible) {
            addHasComponentsFiller(designerRoot);
        } else {
            removeHasComponentsFiller(designerRoot);
            notifyStructureListeners();
        }
    }

    private void addHasComponentsFiller(DesignerComponentWrapper designerComponentWrapper) {
        if (isComponentContainer(designerComponentWrapper.getActualComponent())) {
            ComponentContainerHelper.getChildren(designerComponentWrapper.getActualComponent()).collect(Collectors.toList()).forEach(component -> {
                if (component instanceof DesignerComponentWrapper) {
                    addHasComponentsFiller((DesignerComponentWrapper) component);
                }
            });
            ComponentContainerHelper.addComponent(designerComponentWrapper.getActualComponent(), getDropTargetDiv());
        }
    }

    private void removeHasComponentsFiller(DesignerComponentWrapper designerComponentWrapper) {
        if (isComponentContainer(designerComponentWrapper.getActualComponent())) {
            ComponentContainerHelper.getChildren(designerComponentWrapper.getActualComponent()).collect(Collectors.toList()).forEach(component -> {
                if (component instanceof DesignerComponentWrapper) {
                    removeHasComponentsFiller((DesignerComponentWrapper) component);
                }
                if (component instanceof DropTargetDiv) {
                    ComponentContainerHelper.removeChild(component.getParent().get(), component);
                }
            });
        }
    }

    DropTargetDiv getDropTargetDiv() {
        DropTargetDiv div = new DropTargetDiv();
        DropTarget<Component> dropTarget = DropTarget.create(div);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(event -> {
            Component currentDraggedItem = projectFileModel.getCurrentDragItem();
            handleDropEvent(currentDraggedItem == null ? event.getDragSourceComponent().get() : currentDraggedItem, event.getComponent());
            removeHasComponentsFiller(designerRoot);
        });
        return div;
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
    }

    private boolean dropWasHandled(Component draggedComponent, Component targetComponent) {
        Optional<DropHandler> handler = Arrays.stream(dropHandlers).filter(dropHandler1 -> dropHandler1.canHandleDropEvent(draggedComponent, targetComponent)).findFirst();
        if (handler.isPresent()) {
            handler.get().handleDropEvent(draggedComponent, targetComponent);
            return true;
        } else {
            return false;
        }
    }

    public Component transformDesignerComponentLabel(Component component) {
        DesignerComponentWrapper generatedComponent = ((DesignerComponentLabel) component).getDesignerComponent().generateComponent();
        initDesignerComponentWrapper(generatedComponent);
        addDragListener(generatedComponent);
        addDropListener(generatedComponent);
        return generatedComponent;
    }

    private void initDesignerComponentWrapper(DesignerComponentWrapper generatedComponent) {
        generatedComponent.setNonNestedClickListener(o -> {
            notifyFocusListeners(generatedComponent.getActualComponent(), generatedComponent);
        });
    }

    private void focusElementVisually(DesignerComponentWrapper generatedComponent) {
        if (currentFocus != null) {
            currentFocus.setFocus(false);
        }
        generatedComponent.setFocus(true);
        currentFocus = generatedComponent;
    }

    private void notifyFocusListeners(Component actualComponent, DesignerComponentWrapper parent) {
        getModel().getEventService().getFocusedEventPublisher().publish(actualComponent, parent);
    }

}
