package com.github.appreciated.designer.application.presenter.file.designer.template;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.presenter.file.designer.template.draghandler.*;
import com.github.appreciated.designer.application.presenter.file.designer.template.drophandler.AccordionPanelDropHandler;
import com.github.appreciated.designer.application.presenter.file.designer.template.drophandler.DropHandler;
import com.github.appreciated.designer.application.presenter.file.designer.template.drophandler.TabsDropHandler;
import com.github.appreciated.designer.application.view.file.designer.template.DesignerView;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.component.droptarget.DropTargetAccordionPanel;
import com.github.appreciated.designer.component.droptarget.DropTargetComponent;
import com.github.appreciated.designer.component.droptarget.DropTargetDiv;
import com.github.appreciated.designer.component.droptarget.DropTargetTab;
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
import com.vaadin.flow.component.tabs.Tabs;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;
import static com.github.appreciated.designer.helper.ComponentContainerHelper.removeChild;

@CssImport(value = "./styles/presenter-styles-vaadin-accordion-panel.css", themeFor = "vaadin-accordion-panel")
public class DesignerPresenter extends Presenter<ProjectFileModel, DesignerView> {

    private final ProjectFileModel projectFileModel;
    private final EventService eventService;
    private DesignerComponentWrapper designerRoot;
    private DropHandler[] dropHandlers;
    private DragHandler[] dragTargetHandlers;
    private DesignerComponentWrapper currentFocus;

    public DesignerPresenter(ProjectFileModel projectFileModel) {
        super(projectFileModel);
        eventService = projectFileModel.getEventService();
        this.projectFileModel = projectFileModel;
        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            if (elementFocusedEvent.getFocus() != null) {
                if (elementFocusedEvent.getFocus() instanceof DesignerComponentWrapper) {
                    focusElementVisually((DesignerComponentWrapper) elementFocusedEvent.getFocus());
                } else if (elementFocusedEvent.getFocus().getParent().isPresent() && elementFocusedEvent.getFocus().getParent().get() instanceof DesignerComponentWrapper) {
                    focusElementVisually((DesignerComponentWrapper) elementFocusedEvent.getFocus().getParent().get());
                }
            }
        });
        eventService.getThemeChangedEventListener().addEventConsumer(themeChangedEvent -> {
            CssVariable var = themeChangedEvent.getCssVariable();
            getUI().ifPresent(ui -> ui.access(() ->
                    getContent().getDesignWrapper().getElement().getStyle().set(var.getVariable().getVariableName(), var.getValue()))
            );
        });
        dropHandlers = new DropHandler[]{
                new AccordionPanelDropHandler(this),
                new TabsDropHandler(this)
        };
        dragTargetHandlers = new DragHandler[]{
                new AccordionPanelDragHandler(this),
                new MenuItemDragHandler(this),
                new ScrollerDragHandler(this),
                new TabsDragHandler(this)
        };
        eventService.getDesignerComponentDropListener().addEventConsumer(dropEvent ->
                handleDropEvent(dropEvent.getDraggedComponent(), dropEvent.getTargetComponent())
        );
        eventService.getDesignerComponentDragListener().addEventConsumer(dragEvent ->
                setComponentContainerFillerVisible(dragEvent.isStarted(), dragEvent.getDraggedComponent())
        );
        eventService.getStructureChangedEventListener().addEventConsumer(structureChangedEvent -> {
            if (structureChangedEvent.isForceReload()) {
                init();
            }
        });
        init();
    }

    private void init() {
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
        if (isComponentContainer(component.getActualComponent(), projectFileModel.getInformation())) {
            ComponentContainerHelper.getChildren(component.getActualComponent())
                    .forEach(component1 -> {
                        if (!(component1 instanceof AccordionPanel) && component1 instanceof DesignerComponentWrapper) {
                            initDesignerComponentWrappers((DesignerComponentWrapper) component1);
                        }
                    });
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        notifyStructureListeners();
        getContent().getRedoButton().addClickListener(buttonClickEvent -> projectFileModel.redo(attachEvent.getUI()));
        getContent().getUndoButton().addClickListener(buttonClickEvent -> projectFileModel.undo(attachEvent.getUI()));
        getContent().getSaveButton().addClickListener(buttonClickEvent -> projectFileModel.save(attachEvent.getUI(), true));
    }

    private void notifyStructureListeners() {
        getModel().getEventService().getStructureChangedEventPublisher().publish(getModel().getInformation().getComponent());
    }

    private void initDragAndDropListeners(Component component) {
        if (component instanceof DesignerComponentWrapper) {
            initDesignerComponentWrapper((DesignerComponentWrapper) component);
            addDragListener(component);
            addDropListener(component);
            if (isComponentContainer(((DesignerComponentWrapper) component).getActualComponent(), projectFileModel.getInformation())) {
                ComponentContainerHelper.getChildren(((DesignerComponentWrapper) component).getActualComponent()).forEach(this::initDragAndDropListeners);
            }
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
        dragSource.addDragStartListener(event -> setComponentContainerFillerVisible(true, (DesignerComponentWrapper) event.getComponent()));
        dragSource.addDragEndListener(event -> setComponentContainerFillerVisible(false, (DesignerComponentWrapper) event.getComponent()));
    }

    private synchronized void setComponentContainerFillerVisible(boolean isVisible, DesignerComponentWrapper component) {
        if (isVisible) {
            addComponentContainerFiller(designerRoot, component);
            setDragClass(true);
        } else {
            removeComponentContainerFiller(designerRoot);
            notifyStructureListeners();
            setDragClass(false);
        }
    }

    private void addComponentContainerFiller(Component designerComponentWrapper, DesignerComponentWrapper draggedComponent) {
        if (isComponentContainer(unwrapDesignerComponent(designerComponentWrapper), projectFileModel.getInformation())) {
            ComponentContainerHelper.getChildren(unwrapDesignerComponent(designerComponentWrapper))
                    .collect(Collectors.toList())
                    .forEach(component -> addComponentContainerFiller(component, draggedComponent));
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

    private void removeComponentContainerFiller(Component componentContainer) {
        Component unwrappedComponent = unwrapDesignerComponent(componentContainer);
        if (isComponentContainer(unwrappedComponent, projectFileModel.getInformation())) {
            ComponentContainerHelper.getChildren(unwrappedComponent).collect(Collectors.toList()).forEach(component -> {
                Component unwrappedChild = unwrapDesignerComponent(component);
                if (isComponentContainer(unwrappedChild, projectFileModel.getInformation()) && !(unwrappedChild instanceof DropTargetComponent)) {
                    removeComponentContainerFiller(unwrappedChild);
                }
                if (unwrappedChild instanceof DropTargetComponent) {
                    if (component.getParent().isPresent()) {
                        ComponentContainerHelper.removeChild(component.getParent().get(), unwrappedChild);
                    }
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
            handleDropEvent(currentDraggedItem == null ? event.getDragSourceComponent().get() : currentDraggedItem, event.getComponent());
            removeComponentContainerFiller(designerRoot);
        });
        return div;
    }

    Component getDropTargetComponent(Component target) {
        if (unwrapDesignerComponent(target) instanceof Accordion) {
            return new DropTargetAccordionPanel();
        } else if (unwrapDesignerComponent(target) instanceof Tabs) {
            return new DropTargetTab();
        } else {
            return new DropTargetDiv();
        }
    }

    private void handleDropEvent(Component draggedComponent, Component targetComponent) {
        // If a DesignerComponentLabel is dropped onto the root it needs to be transformed into the actual representation
        if (!dropWasHandled(draggedComponent, targetComponent)) {
            draggedComponent.getParent().ifPresent(component -> {
                if (isComponentContainer(component, projectFileModel.getInformation())) {
                    removeChild(component, draggedComponent);
                }
            });
            if (targetComponent instanceof DesignerComponentWrapper || targetComponent instanceof DropTargetDiv) {
                targetComponent.getParent().ifPresent(component1 -> {
                    if (isComponentContainer(component1, projectFileModel.getInformation())) {
                        ComponentContainerHelper.addComponentAtIndex(component1, ComponentContainerHelper.indexOf(component1, targetComponent), draggedComponent);
                    }
                });
            }
        }
        notifyFocusListeners(draggedComponent);
        getModel().getEventService().getSaveRequiredEventPublisher().publish(getModel().getInformation().getComponent(), true);
    }

    private boolean dropWasHandled(Component draggedComponent, Component targetComponent) {
        if (draggedComponent instanceof DesignerComponentWrapper) {
            Optional<DropHandler> handler = Arrays.stream(dropHandlers)
                    .filter(dropHandler1 -> dropHandler1.canHandleDropEvent((DesignerComponentWrapper) draggedComponent, targetComponent))
                    .findFirst();
            if (handler.isPresent()) {
                handler.get().handleDropEvent((DesignerComponentWrapper) draggedComponent, targetComponent);
                return true;
            }
        }
        return false;
    }

    private boolean dragTargetWasHandled(DesignerComponentWrapper draggedComponent, Component targetComponent) {
        Optional<DragHandler> handler = Arrays.stream(dragTargetHandlers)
                .filter(dropHandler1 -> dropHandler1.canHandleDragTargetEvent(draggedComponent, targetComponent))
                .findFirst();
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
        generatedComponent.setNonNestedClickListener(o -> notifyFocusListeners(generatedComponent.getActualComponent()));
    }

    private void focusElementVisually(DesignerComponentWrapper generatedComponent) {
        if (currentFocus != null) {
            currentFocus.setFocus(false);
        }
        generatedComponent.setFocus(true);
        currentFocus = generatedComponent;
    }

    private void notifyFocusListeners(Component actualComponent) {
        getModel().getEventService().getFocusedEventPublisher().publish(unwrapDesignerComponent(actualComponent));
    }

}
