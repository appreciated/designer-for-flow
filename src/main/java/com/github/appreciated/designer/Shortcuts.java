package com.github.appreciated.designer;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;

import java.util.Optional;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;
import static com.github.appreciated.designer.helper.ComponentContainerHelper.removeChild;

public class Shortcuts {

    public static void register(UI ui, ProjectService service, EventService eventService) {
        ui.addShortcutListener(shortcutEvent -> {
            if (isNoDialogOpen(ui)) {
                if (service.getCurrentProjectFileModel() != null && service.getCurrentProjectFileModel().getCurrentFocus() != null) {
                    Component currentFocus = service.getCurrentProjectFileModel().getCurrentFocus();
                    Optional<Component> optionalParent = currentFocus.getParent();
                    onDelete(ui, service, eventService, currentFocus, optionalParent);
                } else {
                    Notification.show(ui.getTranslation("no.component.has.currently.focus"));
                }
            }
        }, Key.DELETE);
        eventService.getDesignerComponentRemovedEventListener().addEventConsumer(designerComponentRemoveEvent ->
                onDelete(ui, service, eventService, designerComponentRemoveEvent.getRemovedComponent(), designerComponentRemoveEvent.getRemovedComponent().getParent())
        );
        ui.addShortcutListener(shortcutEvent -> {
        }, Key.KEY_C, KeyModifier.CONTROL);

        ui.addShortcutListener(shortcutEvent -> {
            if (service.getCurrentProjectFileModel().getCurrentFocus() != null) {
                Optional<Component> parent = service.getCurrentProjectFileModel().getCurrentFocus().getParent();
                if (parent.isPresent() && parent.get() instanceof DesignerComponentWrapper) {
                    ((DesignerComponentWrapper) parent.get()).setFocus(false);
                    service.getCurrentProjectFileModel().setCurrentFocus(null);
                    eventService.getFocusedEventPublisher().publish(null);
                }
            }
        }, Key.ESCAPE);

        ui.addShortcutListener(shortcutEvent -> service.getCurrentProjectFileModel().save(ui, true), Key.KEY_S, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> {
        }, Key.KEY_V, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> service.getCurrentProjectFileModel().undo(ui), Key.KEY_Z, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> service.getCurrentProjectFileModel().redo(ui), Key.KEY_Y, KeyModifier.CONTROL);
        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            if (service.getCurrentProjectFileModel() != null) {
                service.getCurrentProjectFileModel().setCurrentFocus(elementFocusedEvent.getFocus());
            }
        });
        eventService.getSaveRequiredEventListener().addEventConsumer(saveRequiredEvent -> save(ui, service, saveRequiredEvent.isShowNotification()));
    }

    private static boolean isNoDialogOpen(UI ui) {
        return ui.getChildren().noneMatch(component -> component instanceof Dialog);
    }

    public static void onDelete(UI ui, ProjectService service, EventService eventService, Component currentFocus, Optional<Component> optionalParent) {
        if (optionalParent.isPresent()) {
            Component parent;
            if (optionalParent.get() instanceof DesignerComponentWrapper) {
                parent = optionalParent.get().getParent().get();
            } else {
                parent = optionalParent.get();
            }
            removeComponent(parent, currentFocus, service, eventService, ui);
            eventService.getStructureChangedEventPublisher().publish(service.getCurrentProjectFileModel().getInformation().getComponent());
        } else {
            Notification.show(ui.getTranslation("node.cannot.be.removed.since.it.has.no.parent"));
        }
    }

    private static void save(UI ui, ProjectService service, boolean showNotification) {
        service.getCurrentProjectFileModel().save(ui, showNotification);
    }

    public static void removeComponent(Component parent, Component currentFocus, ProjectService service, EventService eventService, UI ui) {
        if (isComponentContainer(parent, service.getCurrentProjectFileModel().getInformation())) {
            removeChild(parent, currentFocus);
            eventService.getStructureChangedEventPublisher().publish(service.getCurrentProjectFileModel().getInformation().getComponent());
        } else {
            Notification.show(ui.getTranslation("parent.node.is.no.component.container"));
        }
    }
}
