package com.github.appreciated.designer.model;

import com.github.appreciated.designer.model.project.Project;
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

    private String className;
    private Map<Component, CompilationMetaInformation> compilationMetaInformation = new HashMap<>();

    public void setProject(Project project) {
        this.project = project;

        if (project.hasThemeFile()) {
            this.theme = new Theme(project);
        }
    }

    public CompilationMetaInformation getCompilationMetaInformation(Component component) {
        if (hasCompilationMetaInformation(component)) {
            return compilationMetaInformation.get(component);
        } else {
            return null;
        }
    }

    public boolean hasCompilationMetaInformation(Component component) {
        return compilationMetaInformation.containsKey(component);
    }

    public CompilationMetaInformation getOrCreateCompilationMetaInformation(Component component) {
        if (!hasCompilationMetaInformation(component)) {
            compilationMetaInformation.put(component, new CompilationMetaInformation());
        }
        return compilationMetaInformation.get(component);
    }
}