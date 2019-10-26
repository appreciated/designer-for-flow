package com.github.appreciated.designer.application.presenter.file.designer.template;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.file.designer.template.DesignerView;
import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.component.designer.DesignerComponentLabel;
import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.mvp.Presenter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;

public class DesignerPresenter extends Presenter<ProjectFileModel, DesignerView> {

    private DesignerComponentWrapper currentFocus;

    public DesignerPresenter(ProjectFileModel projectFileModel) {
        super(projectFileModel);

        getContent().getDesignWrapper().removeAll();
        Component component = projectFileModel.getInformation().getComponent();
        getContent().getDesignWrapper().add(component);
        initDesignerComponentWrappers((DesignerComponentWrapper) component);
        ((DesignerComponentWrapper) component).getActualComponent().getChildren().forEach(this::initDragAndDropListeners);
        addDropListeners(component);

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
            initDragAndDropListeners(((DesignerComponentWrapper) component).getActualComponent());
            addDragListeners(component);
        } else {
            if (component instanceof HasComponents) {
                component.getChildren().forEach(this::initDragAndDropListeners);
                addDropListeners(component);
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
        }
    }

    private void addDragListeners(Component root) {
        if (root instanceof DesignerComponentWrapper) {
            DragSource<Component> dragSource = DragSource.create(root);
            dragSource.setDraggable(true);
        } else {
            throw new IllegalStateException("This should never happen");
        }
    }

    private void handleDrop(DropEvent<Component> event) {
        event.getDragSourceComponent().ifPresent(extension -> {
            Component source = extension;
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
                if (event.getDragSourceComponent().get() instanceof DesignerComponentWrapper) {
                    event.getDragSourceComponent().get().getParent().ifPresent(parent -> {
                        if (parent instanceof HasComponents) {
                            ((HasComponents) parent).remove(source);
                        }
                    });
                    if (event.getComponent() instanceof HasComponents) {
                        ((HasComponents) event.getComponent()).add(source);
                    } else {
                        throw new IllegalStateException("This should never happen");
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
        addDragListeners(generatedComponent);
        addDropListeners(generatedComponent);
        return generatedComponent;
    }

    private void initDesignerComponentWrapper(DesignerComponentWrapper generatedComponent) {
        generatedComponent.setNonNestedClickListener(o -> {
            System.out.println(generatedComponent.getActualComponent().getClass().getSimpleName());
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

    public Component getRootComponent() {
        return getModel().getInformation().getComponent();
    }

}
