package com.github.appreciated.designer;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.template.java.generator.JavaGenerator;
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
                    } else {
                        Notification.show(ui.getTranslation("node.cannot.be.removed.since.it.has.no.parent"));
                    }
                } else {
                    Notification.show(ui.getTranslation("no.component.has.currently.focus"));
                }
            }
        }, Key.DELETE);
        ui.addShortcutListener(shortcutEvent -> {
            throw new RuntimeException("This is only a test Exception");
        }, Key.KEY_E, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> {
        }, Key.KEY_C, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> {
        }, Key.KEY_V, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> {
        }, Key.KEY_Z, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> {
        }, Key.KEY_Y, KeyModifier.CONTROL);
        ui.addShortcutListener(shortcutEvent -> {
            if (isNoDialogOpen(ui)) {
                if (service.getCurrentProjectFileModel() != null) {
                    JavaGenerator compiler = new JavaGenerator(service.getCurrentProjectFileModel().getInformation());
                    compiler.save();
                    Notification.show(ui.getTranslation("design.was.saved"));
                }
            }
        }, Key.KEY_S, KeyModifier.CONTROL);
        eventService.getStructureChangedEventListener().addEventConsumer(structureChangedEvent -> {
            if (service.getCurrentProjectFileModel() != null) {
                JavaGenerator compiler = new JavaGenerator(service.getCurrentProjectFileModel().getInformation());
                compiler.save();
            }
        });
        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            if (service.getCurrentProjectFileModel() != null) {
                service.getCurrentProjectFileModel().setCurrentFocus(elementFocusedEvent.getFocus());
            }
        });
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
}
