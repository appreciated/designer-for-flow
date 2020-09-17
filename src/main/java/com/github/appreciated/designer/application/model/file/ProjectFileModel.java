package com.github.appreciated.designer.application.model.file;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.mvp.Model;
import com.vaadin.flow.component.Component;
import lombok.Data;

@Data
public class ProjectFileModel implements Model<ProjectFileModel> {

    private final DesignCompilerInformation information;
    private Component currentDragItem = null;
    private final EventService eventService;
    private Component currentFocus;

    public ProjectFileModel(final DesignCompilerInformation information, final EventService eventService) {
        this.eventService = eventService;
        this.information = information;
    }

    public void setCurrentDragItem(Component item) {
        this.currentDragItem = item;
    }

    public synchronized void removeCurrentDragItem() {
        currentDragItem = null;
    }
}