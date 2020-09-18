package com.github.appreciated.designer;

import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.template.java.generator.JavaGenerator;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;
import static com.github.appreciated.designer.helper.ComponentContainerHelper.removeChild;

public class Shortcuts {

    public static void register(UI ui, ProjectService service, EventService eventService) {
        ui.addShortcutListener(shortcutEvent -> {
            if (isNoDialogOpen(ui)) {
                if (service.getCurrentProjectFileModel() != null && service.getCurrentProjectFileModel().getCurrentFocus() != null) {
                    service.getCurrentProjectFileModel().getCurrentFocus().getParent().get().getParent().ifPresent(parent -> {
                        if (isComponentContainer(parent)) {
                            removeChild(parent, service.getCurrentProjectFileModel().getCurrentFocus().getParent().get());
                            eventService.getStructureChangedEventPublisher().publish(service.getCurrentProjectFileModel().getInformation().getComponent());
                        } else {
                            Notification.show("Parent is not of type HasComponents");
                        }
                    });
                } else {
                    Notification.show("No Component has currently focus");
                }
            }
        }, Key.DELETE);
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
                    Notification.show("Design was saved!");
                }
            }
        }, Key.KEY_S, KeyModifier.CONTROL);
        eventService.getStructureChangedEventListener().addEventConsumer(structureChangedEvent -> {
            if (service.getCurrentProjectFileModel() != null) {
                System.out.println("StructureChanged!");
                JavaGenerator compiler = new JavaGenerator(service.getCurrentProjectFileModel().getInformation());
                compiler.save();
            }
        });
        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            if (service.getCurrentProjectFileModel() != null) {
                System.out.println("FocusedEvent!");
                service.getCurrentProjectFileModel().setCurrentFocus(elementFocusedEvent.getFocus());
            }
        });
    }

    private static boolean isNoDialogOpen(UI ui) {
        return ui.getChildren().noneMatch(component -> component instanceof Dialog);
    }
}
