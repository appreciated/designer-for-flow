package com.github.appreciated.designer;

import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.template.java.generator.DesignerComponentTreeJavaGenerator;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

public class Shortcuts {

    public static void register(UI ui, ProjectService service, EventService eventService) {
        ui.addShortcutListener(shortcutEvent -> {
            if (service.getCurrentProjectFileModel().getCurrentFocus() != null) {
                service.getCurrentProjectFileModel().getCurrentFocus().getParent().get().getParent().ifPresent(parent -> {
                    if (parent instanceof HasComponents) {
                        ((HasComponents) parent).remove(service.getCurrentProjectFileModel().getCurrentFocus().getParent().get());
                        eventService.getStructureChangedEventPublisher().publish(service.getCurrentProjectFileModel().getInformation().getComponent());
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
            DesignerComponentTreeJavaGenerator compiler = new DesignerComponentTreeJavaGenerator(service.getCurrentProjectFileModel().getInformation());
            compiler.save();
            Notification.show("Design was saved!");
        }, Key.KEY_S, KeyModifier.CONTROL);
        eventService.getStructureChangedEventListener().addEventConsumer(structureChangedEvent -> {
            System.out.println("StructureChanged!");
            if (service.getCurrentProjectFileModel() != null) {
                DesignerComponentTreeJavaGenerator compiler = new DesignerComponentTreeJavaGenerator(service.getCurrentProjectFileModel().getInformation());
                compiler.save();
            }
        });
        eventService.getFocusedEventListener().addEventConsumer(elementFocusedEvent -> {
            System.out.println("FocusedEvent!");
            service.getCurrentProjectFileModel().setCurrentFocus(elementFocusedEvent.getFocus());
        });
    }
}
