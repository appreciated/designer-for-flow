package com.github.appreciated.designer.model;

import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.template.java.compiler.ComponentInformation;
import com.github.appreciated.designer.theme.css.Theme;
import com.vaadin.flow.component.Component;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configurable
public class DesignCompilerInformation {
    Project project;
    File design;
    Component component;
    Theme theme;
    Map<Component, ComponentInformation> componentInformation = new HashMap<>();

    private String className;
    private Map<Component, CompilationMetainformation> compilationMetaInformation = new HashMap<>();

    public DesignCompilerInformation() {
    }

    public Map<Component, ComponentInformation> getComponentInformation() {
        return componentInformation;
    }

    public void setComponentInformation(Map<Component, ComponentInformation> componentInformation) {
        this.componentInformation = componentInformation;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        if (project.hasThemeFile()) {
            this.theme = new Theme(project);
        }
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public File getDesign() {
        return design;
    }

    public void setDesign(File design) {
        this.design = design;
    }

    public CompilationMetainformation getComponentMetainfo(Component component) {
        if (!compilationMetaInformation.containsKey(component)) {
            compilationMetaInformation.put(component, new CompilationMetainformation());
        }
        return compilationMetaInformation.get(component);
    }

}
