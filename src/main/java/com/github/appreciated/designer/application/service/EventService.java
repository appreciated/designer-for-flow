package com.github.appreciated.designer.application.service;

import com.github.appreciated.designer.application.designer.dnd.DesignerComponentDragEventListener;
import com.github.appreciated.designer.application.designer.dnd.DesignerComponentDragEventPublisher;
import com.github.appreciated.designer.application.designer.dnd.DesignerComponentDropEventListener;
import com.github.appreciated.designer.application.designer.dnd.DesignerComponentDropEventPublisher;
import com.github.appreciated.designer.application.designer.focus.ElementFocusedEventListener;
import com.github.appreciated.designer.application.designer.focus.ElementFocusedEventPublisher;
import com.github.appreciated.designer.application.designer.remove.DesignerComponentRemoveEventListener;
import com.github.appreciated.designer.application.designer.remove.DesignerComponentRemoveEventPublisher;
import com.github.appreciated.designer.application.designer.save.SaveRequiredEventListener;
import com.github.appreciated.designer.application.designer.save.SaveRequiredEventPublisher;
import com.github.appreciated.designer.application.designer.structure.StructureChangedEventListener;
import com.github.appreciated.designer.application.designer.structure.StructureChangedEventPublisher;
import com.github.appreciated.designer.application.designer.theme.ThemeChangedEventListener;
import com.github.appreciated.designer.application.designer.theme.ThemeChangedEventPublisher;
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
    private final DesignerComponentDropEventListener designerComponentDropListener;
    private final DesignerComponentDropEventPublisher designerComponentDropEventPublisher;
    private final DesignerComponentDragEventListener designerComponentDragListener;
    private final DesignerComponentDragEventPublisher designerComponentDragEventPublisher;
    private final DesignerComponentRemoveEventListener designerComponentRemoveEventListener;
    private final DesignerComponentRemoveEventPublisher designerComponentRemoveEventPublisher;
    private final SaveRequiredEventListener saveRequiredEventListener;
    private final SaveRequiredEventPublisher saveRequiredEventPublisher;

    public EventService(@Autowired ElementFocusedEventPublisher focusedEventPublisher,
                        @Autowired ElementFocusedEventListener focusedEventListener,
                        @Autowired StructureChangedEventPublisher structureChangedEventPublisher,
                        @Autowired StructureChangedEventListener structureChangedEventListener,
                        @Autowired SaveRequiredEventPublisher saveRequiredEventPublisher,
                        @Autowired SaveRequiredEventListener saveRequiredEventListener,
                        @Autowired ThemeChangedEventPublisher themeChangedEventPublisher,
                        @Autowired ThemeChangedEventListener themeChangedEventListener,
                        @Autowired DesignerComponentDropEventListener designerComponentDropListener,
                        @Autowired DesignerComponentDropEventPublisher designerComponentDropEventPublisher,
                        @Autowired DesignerComponentDragEventListener designerComponentDragListener,
                        @Autowired DesignerComponentDragEventPublisher designerComponentDragEventPublisher,
                        @Autowired DesignerComponentRemoveEventListener designerComponentRemoveEventListener,
                        @Autowired DesignerComponentRemoveEventPublisher designerComponentRemoveEventPublisher
    ) {
        this.focusedEventPublisher = focusedEventPublisher;
        this.structureChangedEventPublisher = structureChangedEventPublisher;
        this.themeChangedEventPublisher = themeChangedEventPublisher;
        this.focusedEventListener = focusedEventListener;
        this.structureChangedEventListener = structureChangedEventListener;
        this.saveRequiredEventPublisher = saveRequiredEventPublisher;
        this.saveRequiredEventListener = saveRequiredEventListener;
        this.themeChangedEventListener = themeChangedEventListener;
        this.designerComponentDropListener = designerComponentDropListener;
        this.designerComponentDropEventPublisher = designerComponentDropEventPublisher;
        this.designerComponentDragListener = designerComponentDragListener;
        this.designerComponentDragEventPublisher = designerComponentDragEventPublisher;
        this.designerComponentRemoveEventListener = designerComponentRemoveEventListener;
        this.designerComponentRemoveEventPublisher = designerComponentRemoveEventPublisher;
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

    public DesignerComponentDropEventListener getDesignerComponentDropListener() {
        return designerComponentDropListener;
    }

    public DesignerComponentDropEventPublisher getDesignerComponentDropEventPublisher() {
        return designerComponentDropEventPublisher;
    }

    public DesignerComponentDragEventListener getDesignerComponentDragListener() {
        return designerComponentDragListener;
    }

    public DesignerComponentDragEventPublisher getDesignerComponentDragEventPublisher() {
        return designerComponentDragEventPublisher;
    }

    public SaveRequiredEventListener getSaveRequiredEventListener() {
        return saveRequiredEventListener;
    }

    public SaveRequiredEventPublisher getSaveRequiredEventPublisher() {
        return saveRequiredEventPublisher;
    }

    public DesignerComponentRemoveEventListener getDesignerComponentRemovedEventListener() {
        return designerComponentRemoveEventListener;
    }

    public DesignerComponentRemoveEventPublisher getDesignerComponentRemovedEventPublisher() {
        return designerComponentRemoveEventPublisher;
    }
}
