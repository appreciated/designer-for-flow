package com.github.appreciated.designer;

import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.template.java.generator.DesignerComponentTreeJavaGenerator;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

public class Shortcuts {
    @Autowired
    ProjectService service;
    private Component currentFocus;

    public Shortcuts(UI ui, ProjectService service, EventService eventService) {
        ui.addShortcutListener(shortcutEvent -> {
            if (currentFocus != null) {
                currentFocus.getParent().get().getParent().ifPresent(parent -> {
                    if (parent instanceof HasComponents) {
                        ((HasComponents) parent).remove(currentFocus.getParent().get());
                        eventService.getStructureChangedEventPublisher().publish(service.getCurrentProjectFile().getComponent());
                    } else {
                        Notification.show("Parent is not of type HasComponents");
                    }
                });
            } else {
                Notification.show("No Component has currently focus");
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
            DesignerComponentTreeJavaGenerator compiler = new DesignerComponentTreeJavaGenerator(service.getCurrentProjectFile());
            compiler.save();
            Notification.show("Design was saved!");
        }, Key.KEY_S, KeyModifier.CONTROL);
        eventService.getStructureChangedEventListener().addEventConsumer(structureChangedEvent -> {
            System.out.println("StructureChanged!");
            if (service.getCurrentProjectFile() != null) {
                DesignerComponentTreeJavaGenerator compiler = new DesignerComponentTreeJavaGenerator(service.getCurrentProjectFile());
                compiler.save();
            }
        });
        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            System.out.println("FocusedEvent!");
            currentFocus = elementFocusedEvent.getFocus();
        });
    }
}
