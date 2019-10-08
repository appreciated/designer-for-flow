package com.github.appreciated.designer.service;

import com.github.appreciated.designer.events.designer.focus.ElementFocusedEventListener;
import com.github.appreciated.designer.events.designer.focus.ElementFocusedEventPublisher;
import com.github.appreciated.designer.events.designer.structure.StructureChangedEventListener;
import com.github.appreciated.designer.events.designer.structure.StructureChangedEventPublisher;
import com.github.appreciated.designer.events.designer.theme.ThemeChangedEventListener;
import com.github.appreciated.designer.events.designer.theme.ThemeChangedEventPublisher;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class EventService {

    private final ElementFocusedEventPublisher focusedEventPublisher;
    private final StructureChangedEventPublisher structureChangedEventPublisher;
    private final ThemeChangedEventPublisher themeChangedEventPublisher;
    private final ElementFocusedEventListener focusedEventListener;
    private final StructureChangedEventListener structureChangedEventListener;
    private final ThemeChangedEventListener themeChangedEventListener;

    public EventService(@Autowired ElementFocusedEventPublisher focusedEventPublisher,
                        @Autowired StructureChangedEventPublisher structureChangedEventPublisher,
                        @Autowired ThemeChangedEventPublisher themeChangedEventPublisher,
                        @Autowired ElementFocusedEventListener focusedEventListener,
                        @Autowired StructureChangedEventListener structureChangedEventListener,
                        @Autowired ThemeChangedEventListener themeChangedEventListener) {
        this.focusedEventPublisher = focusedEventPublisher;
        this.structureChangedEventPublisher = structureChangedEventPublisher;
        this.themeChangedEventPublisher = themeChangedEventPublisher;
        this.focusedEventListener = focusedEventListener;
        this.structureChangedEventListener = structureChangedEventListener;
        this.themeChangedEventListener = themeChangedEventListener;
    }

    public ThemeChangedEventListener getThemeChangedEventListener() {
        return themeChangedEventListener;
    }

    public ThemeChangedEventPublisher getThemeChangedEventPublisher() {
        return themeChangedEventPublisher;
    }

    public ElementFocusedEventPublisher getFocusedEventPublisher() {
        return focusedEventPublisher;
    }

    public StructureChangedEventPublisher getStructureChangedEventPublisher() {
        return structureChangedEventPublisher;
    }

    public ElementFocusedEventListener getFocusedEventListener() {
        return focusedEventListener;
    }

    public StructureChangedEventListener getStructureChangedEventListener() {
        return structureChangedEventListener;
    }

    public void setStructureChangedEventListener(StructureChangedEventListener structureChangedEventListener) {

    }

}
