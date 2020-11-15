package com.github.appreciated.designer.application.model.file;

import com.github.appreciated.designer.application.service.EventService;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.model.DesignFileState;
import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.template.java.generator.JavaGenerator;
import com.github.appreciated.mvp.Model;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import lombok.Data;

import java.io.IOException;
import java.util.Stack;

@Data
public class ProjectFileModel implements Model<ProjectFileModel> {

    private final EventService eventService;
    private final Project project;
    private DesignCompilerInformation information;
    private Component currentDragItem = null;
    private Component currentFocus;
    private Stack<DesignFileState> states = new Stack<>();
    private DesignFileState currentState;

    public ProjectFileModel(final DesignCompilerInformation information, final EventService eventService) throws IOException {
        this.eventService = eventService;
        this.information = information;
        this.project = information.getProject();
        pushState(new DesignFileState(information.getDesign()));
    }

    private void pushState(DesignFileState state) {
        if (getStates().size() > 0) {
            while (getStates().peek() != getCurrentState()) {
                getStates().pop();
            }
        }
        getStates().push(state);
        currentState = state;
    }

    public Stack<DesignFileState> getStates() {
        return states;
    }

    public DesignFileState getCurrentState() {
        return currentState;
    }

    public void setCurrentDragItem(Component item) {
        this.currentDragItem = item;
    }

    public synchronized void removeCurrentDragItem() {
        currentDragItem = null;
    }

    public void save(UI ui, boolean showNotification) {
        save(ui, showNotification, true);
    }

    public void save(UI ui, boolean showNotification, boolean pushState) {
        JavaGenerator compiler = new JavaGenerator(getInformation());
        try {
            DesignFileState model = compiler.save();
            if (pushState) {
                pushState(model);
            }
            if (showNotification) {
                Notification.show(ui.getTranslation("design.was.saved"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Notification notification = new Notification(ui.getTranslation("design.could.not.be.saved"), 5000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }

    public void redo(UI ui) {
        if (getStates().peek() != getCurrentState()) {
            currentState = getStates().elementAt(getStates().indexOf(getCurrentState()) + 1);
            revertState(ui);
        } else {
            Notification.show(ui.getTranslation("cannot.redo.further"));
        }
    }

    private void revertState(UI ui) {
        try {
            information = currentState.revert(project);
            save(ui, false, false);
            eventService.getStructureChangedEventPublisher().publish(information.getComponent(), true);
            eventService.getFocusedEventPublisher().publish(null);
        } catch (IOException e) {
            e.printStackTrace();
            Notification.show(ui.getTranslation("reverting.to.another.state.failed"));
        }
    }

    public void undo(UI ui) {
        if (getStates().elementAt(0) != getCurrentState()) {
            currentState = getStates().elementAt(getStates().indexOf(getCurrentState()) - 1);
            revertState(ui);
        } else {
            Notification.show(ui.getTranslation("cannot.undo.further"));
        }
    }
}