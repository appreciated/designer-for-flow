package com.github.appreciated.designer.model.project;

import com.github.appreciated.designer.model.DesignCompilerInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class Project {
    private File projectRoot;
    private ArrayList<DesignCompilerInformation> templates = new ArrayList<>();
    private File projectFolder;

    public Project(File projectFolder) {
        this.projectRoot = projectFolder;
    }

    abstract public boolean isValidProject();

    public File getProjectRoot() {
        return projectRoot;
    }

    abstract public boolean hasThemeFile();
    
    abstract public boolean createThemeFile();

    abstract public File getThemeFile();

    public abstract void setThemeFile(File themeFile);

    abstract public File getFrontendFolder();

    abstract public void setFrontendFolder(File frontendFolder);

    abstract public ProjectType getType();

    abstract public boolean hasMissingProjectInformation();

    abstract public Stream<ProjectInformation> getMissingProjectInformation();

    public ArrayList<DesignCompilerInformation> getTemplates() {
        return templates;
    }

    public abstract File getSourceFolder();

    public abstract void setSourceFolder(File folder);

}
