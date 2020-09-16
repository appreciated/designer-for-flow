package com.github.appreciated.designer.application.model.file;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.mvp.Model;
import com.vaadin.flow.component.Component;

public class ProjectFileModel implements Model<ProjectFileModel> {
    DesignCompilerInformation information;

    EventService eventService;
    private Component currentFocus;

    public ProjectFileModel(DesignCompilerInformation information, EventService eventService) {
        this.information = information;
        this.eventService = eventService;
    }

    public DesignCompilerInformation getInformation() {
        return information;
    }

    public EventService getEventService() {
        return eventService;
    }

    public Component getCurrentFocus() {
        return currentFocus;
    }

    public void setCurrentFocus(Component focus) {
        this.currentFocus = focus;
    }
}
