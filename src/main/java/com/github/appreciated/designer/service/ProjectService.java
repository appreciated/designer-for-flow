package com.github.appreciated.designer.service;

import com.github.appreciated.designer.AppConfig;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.model.project.ProjectTypes;
import com.github.appreciated.designer.template.java.parser.DesignerComponentTreeParser;
import com.github.appreciated.designer.theme.css.Theme;
import com.github.javaparser.ParseException;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@UIScope
public class ProjectService {

    private AppConfig config;
    private Project project;
    private DesignCompilerInformation currentFile = null;

    public ProjectService(@Autowired AppConfig config) {
        this.config = config;
    }

    public DesignCompilerInformation getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(int selectedIndex) {
        currentFile = project.getTemplates().get(selectedIndex);
    }

    public void add(File file) {
        if (file.exists()) {
            DesignerComponentTreeParser parser = null;
            try {
                parser = new DesignerComponentTreeParser(file, this);
            } catch (ParseException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            DesignCompilerInformation info = parser.getDesignCompilerInformation();
            info.setProject(project);
            project.getTemplates().add(info);
            currentFile = info;
        } else {
            Notification.show("This file does not exist!");
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void initProject(File file) {
        ProjectTypes types = new ProjectTypes(file);
        if (types.hasFittingProjectType()) {
            project = types.getFittingProjectType();
        } else {
            Notification.show("Illegal State!");
            throw new IllegalStateException("Illegal State!");
        }
    }

    private void add(File designFile, File frontendFolder, File sourceFolder, File themeFile) {
        project.setFrontendFolder(frontendFolder);
        project.setThemeFile(themeFile);
        project.setSourceFolder(sourceFolder);
        try {
            DesignerComponentTreeParser parser = new DesignerComponentTreeParser(designFile, this);
            DesignCompilerInformation info = parser.getDesignCompilerInformation();
            Theme theme = new Theme(project);
            theme.init();
            info.setTheme(theme);
            info.setProject(project);
            project.getTemplates().add(info);
            currentFile = info;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public AppConfig getConfig() {
        return config;
    }

    public void close(DesignCompilerInformation info) {
        project.getTemplates().remove(info);
    }
}
