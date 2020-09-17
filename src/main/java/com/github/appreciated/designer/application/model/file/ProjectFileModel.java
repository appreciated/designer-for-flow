package com.github.appreciated.designer.application.model.file;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.mvp.Model;
import com.google.common.collect.Sets;
import com.vaadin.flow.component.Component;

import lombok.Data;

@Data
public class ProjectFileModel implements Model<ProjectFileModel> {
	
    private final DesignCompilerInformation information;
    private final Set<Component> currentDrag;
    private final EventService eventService;
    private Component currentFocus;

    public ProjectFileModel(final DesignCompilerInformation information, final EventService eventService) {
    	currentDrag = Sets.newConcurrentHashSet();
    	
        this.eventService = eventService;
        this.information = information;
    }
    
    public void addCurrentDragItems(final Collection<Component> addItems) {
    	removeOrAddCurrentDragItems(null, addItems);
    }
    
    public void addCurrentDragItems(final Component... addItems) {
    	if (addItems != null) {
    		for (Component item : addItems) {
        		getCurrentDrag().add(item);
			}
    	}
    }
    
    public void removeAndAddCurrentDragItems(final Collection<Component> addItems) {
    	removeOrAddCurrentDragItems(getAndCloneCurrentDragCollection(), addItems);
    }
    
    public void removeAllCurrentDragItems() {
    	removeOrAddCurrentDragItems(getAndCloneCurrentDragCollection(), null);
    }
    
    public synchronized void removeCurrentDragItems(final Collection<Component> removeItems) {
    	removeOrAddCurrentDragItems(removeItems, null);
    }
    
    public synchronized void removeCurrentDragItems(final Component... removeItems) {
    	if (removeItems != null) {
    		for (Component item : removeItems) {
        		getCurrentDrag().remove(item);
			}
    	}
    }
    
    public Set<Component> getAndCloneCurrentDragCollection() {
    	return getCurrentDrag().stream().collect(Collectors.toSet());
    }
    
    public synchronized void removeOrAddCurrentDragItems(final Collection<Component> removeItems, final Collection<Component> addComponents) {
    	if (removeItems != null) {
    		getCurrentDrag().removeAll(removeItems);
    	}
    	
    	if (addComponents != null) {
        	getCurrentDrag().addAll(addComponents);
    	}
    }
    
    public boolean isCurrentDragAvailable() {
    	return (getCurrentDrag().size() > 0);
    }
}