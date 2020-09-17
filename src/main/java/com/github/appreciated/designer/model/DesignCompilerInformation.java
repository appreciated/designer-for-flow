package com.github.appreciated.designer.model;

import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.template.java.generator.ComponentInformation;
import com.github.appreciated.designer.theme.css.Theme;
import com.vaadin.flow.component.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Configurable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Data
@Configurable
@NoArgsConstructor
public class DesignCompilerInformation {
	
    private Project project;
    private File design;
    private Component component;
    private Theme theme;
    private Map<Component, ComponentInformation> componentInformation = new HashMap<>();

    private String className;
    private Map<Component, CompilationMetainformation> compilationMetaInformation = new HashMap<>();

    public void setProject(Project project) {
        this.project = project;
        
        if (project.hasThemeFile()) {
            this.theme = new Theme(project);
        }
    }

    public CompilationMetainformation getComponentMetainfo(Component component) {
        if (!compilationMetaInformation.containsKey(component)) {
            compilationMetaInformation.put(component, new CompilationMetainformation());
        }
        
        return compilationMetaInformation.get(component);
    }
}