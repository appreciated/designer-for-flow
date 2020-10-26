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
                    if (optionalParent.isPresent()) {
                        Component parent;
                        if (optionalParent.get() instanceof DesignerComponentWrapper) {
                            parent = optionalParent.get().getParent().get();
                        } else {
                            parent = optionalParent.get();
                        }
                        removeComponent(parent, service, eventService, ui);
                        eventService.getStructureChangedEventPublisher().publish(service.getCurrentProjectFileModel().getInformation().getComponent());
                    } else {
                        Notification.show(ui.getTranslation("node.cannot.be.removed.since.it.has.no.parent"));
                    }
                } else {
                    Notification.show(ui.getTranslation("no.component.has.currently.focus"));
                }
            }
        }, Key.DELETE);
        ui.addShortcutListener(shortcutEvent -> {
        }, Key.KEY_C, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> {
        }, Key.KEY_V, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> service.getCurrentProjectFileModel().undo(ui), Key.KEY_Z, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> service.getCurrentProjectFileModel().redo(ui), Key.KEY_Y, KeyModifier.CONTROL);
        eventService.getStructureChangedEventListener().addEventConsumer(structureChangedEvent -> {
            save(ui, service);
        });
        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            if (service.getCurrentProjectFileModel() != null) {
                service.getCurrentProjectFileModel().setCurrentFocus(elementFocusedEvent.getFocus());
            }
        });
        eventService.getStructureChangedEventListener().addAfterEventConsumer(structureChangedEvent -> save(ui, service));
    }

    private static boolean isNoDialogOpen(UI ui) {
        return ui.getChildren().noneMatch(component -> component instanceof Dialog);
    }

    public static void removeComponent(Component parent, ProjectService service, EventService eventService, UI ui) {
        if (isComponentContainer(parent, service.getCurrentProjectFileModel().getInformation())) {
            removeChild(parent, service.getCurrentProjectFileModel().getCurrentFocus().getParent().get());
            eventService.getStructureChangedEventPublisher().publish(service.getCurrentProjectFileModel().getInformation().getComponent());
        } else {
            Notification.show(ui.getTranslation("parent.node.is.no.component.container"));
        }
    }

    private static void save(UI ui, ProjectService service) {
        service.getCurrentProjectFileModel().save(ui);
    }
}
