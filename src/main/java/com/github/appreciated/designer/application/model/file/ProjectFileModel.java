package com.github.appreciated.designer.application.model.file;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.mvp.Model;

public class ProjectFileModel implements Model<ProjectFileModel> {
    DesignCompilerInformation information;

    EventService eventService;

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
}
