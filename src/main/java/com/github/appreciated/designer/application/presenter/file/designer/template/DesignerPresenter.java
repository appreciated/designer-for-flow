package com.github.appreciated.designer.application.presenter.file.designer.template;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.file.designer.template.DesignerView;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.component.DropTargetDiv;
import com.github.appreciated.designer.component.designer.DesignerComponentLabel;
import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.mvp.Presenter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.dnd.*;

import java.util.stream.Collectors;

public class DesignerPresenter extends Presenter<ProjectFileModel, DesignerView> {

    private final DesignerComponentWrapper designerRoot;
    private DesignerComponentWrapper currentFocus;

    public DesignerPresenter(ProjectFileModel projectFileModel) {
        super(projectFileModel);

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
                if (actualComponent instanceof HasOrderedComponents) {
                    ((HasOrderedComponents<?>) actualComponent).add(dragSourceComponent);
                }
                notifyStructureListeners();
            }
        });

        designerRoot.getActualComponent().getChildren().forEach(this::initDragAndDropListeners);

        projectFileModel.getEventService().getFocusedEventListener().addEventConsumer(elementFocusedEvent -> focusElementVisually(elementFocusedEvent.getParent()));
        projectFileModel.getEventService().getThemeChangedEventListener().addEventConsumer(themeChangedEvent -> {
            CssVariable var = themeChangedEvent.getCssVariable();
            getUI().ifPresent(ui -> ui.access(() -> {
                getContent().getDesignWrapper().getElement().getStyle().set(var.getVariable().getVariableName(), var.getValue());
            }));
        });
    }

    private void initDesignerComponentWrappers(DesignerComponentWrapper component) {
        initDesignerComponentWrapper(component);
        if (component.getActualComponent() instanceof HasComponents) {
            component.getActualComponent().getChildren().forEach(component1 -> initDesignerComponentWrappers((DesignerComponentWrapper) component1));
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
            if (((DesignerComponentWrapper) component).getActualComponent() instanceof HasComponents) {
                ((DesignerComponentWrapper) component).getActualComponent().getChildren().forEach(this::initDragAndDropListeners);
            }
        } else {
            throw new IllegalStateException("This should never happen");
        }
    }

    private void addDropListener(Component component) {
        DropTarget<Component> dropTarget = DropTarget.create(component);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(this::handleDrop);
    }

    private void addDragListener(Component root) {
        DragSource<Component> dragSource = DragSource.create(root);
        dragSource.setEffectAllowed(EffectAllowed.ALL);
        dragSource.addDragStartListener(event -> setHasComponentsFillerVisible(true));
        dragSource.addDragEndListener(event -> setHasComponentsFillerVisible(false));
    }

    private synchronized void setHasComponentsFillerVisible(boolean isVisible) {
        if (isVisible) {
            addHasComponentsFiller(designerRoot);
        } else {
            removeHasComponentsFiller(designerRoot);
        }
    }

    private void addHasComponentsFiller(DesignerComponentWrapper designerComponentWrapper) {
        if (designerComponentWrapper.getActualComponent() instanceof HasOrderedComponents) {
            designerComponentWrapper.getActualComponent().getChildren().collect(Collectors.toList()).forEach(component -> {
                if (component instanceof DesignerComponentWrapper) {
                    addHasComponentsFiller((DesignerComponentWrapper) component);
                }
            });
            ((HasOrderedComponents<?>) designerComponentWrapper.getActualComponent()).add(getDropTargetDiv());
        }
    }

    private void removeHasComponentsFiller(DesignerComponentWrapper designerComponentWrapper) {
        if (designerComponentWrapper.getActualComponent() instanceof HasOrderedComponents) {
            designerComponentWrapper.getActualComponent().getChildren().collect(Collectors.toList()).forEach(component -> {
                if (component instanceof DesignerComponentWrapper) {
                    removeHasComponentsFiller((DesignerComponentWrapper) component);
                }
                if (component instanceof DropTargetDiv) {
                    ((HasOrderedComponents<?>) designerComponentWrapper.getActualComponent()).remove(component);
                }
            });
        }
    }

    DropTargetDiv getDropTargetDiv() {
        DropTargetDiv div = new DropTargetDiv();
        DropTarget<Component> dropTarget = DropTarget.create(div);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(event -> {
            handleDrop(event);
            removeHasComponentsFiller(designerRoot);
        });
        return div;
    }

    private void handleDrop(DropEvent<Component> event) {
        event.getDragSourceComponent().ifPresent(extension -> {
            // If a DesignerComponentLabel is dropped onto the root it needs to be transformed into the actual representation
            if (extension instanceof DesignerComponentLabel) {
                Component transformedComponent = transformDesignerComponentLabel(extension);
                // Anyways the element will be added to the drop source.
                if (event.getComponent() instanceof DesignerComponentWrapper) {
                    event.getComponent().getParent().ifPresent(component1 -> {
                        if (component1 instanceof HasOrderedComponents) {
                            ((HasOrderedComponents<?>) component1).addComponentAtIndex(((HasOrderedComponents<?>) component1).indexOf(event.getComponent()), transformedComponent);
                        }
                    });
                } else if (event.getComponent() instanceof HasComponents) {
                    ((HasComponents) event.getComponent()).add(transformedComponent);
                    transformedComponent.getElement().executeJs("focus()");
                }
            } else {
                event.getDragSourceComponent().get().getParent().ifPresent(component -> {
                    if (component instanceof HasOrderedComponents) {
                        ((HasOrderedComponents<?>) component).remove(event.getDragSourceComponent().get());
                    }
                });
                if (event.getComponent() instanceof DesignerComponentWrapper || event.getComponent() instanceof DropTargetDiv) {
                    event.getComponent().getParent().ifPresent(component1 -> {
                        if (component1 instanceof HasOrderedComponents) {
                            ((HasOrderedComponents<?>) component1).addComponentAtIndex(((HasOrderedComponents<?>) component1).indexOf(event.getComponent()), event.getDragSourceComponent().get());
                        }
                    });
                }
            }
            notifyStructureListeners();
        });
    }

    private Component transformDesignerComponentLabel(Component component) {
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
