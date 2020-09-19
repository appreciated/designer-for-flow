package com.github.appreciated.designer.model.project.gradle;

import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.model.project.ProjectInformation;
import com.github.appreciated.designer.model.project.ProjectType;

import java.io.File;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class GradleProject extends Project {

    public GradleProject(File projectRootPath) {
        super(projectRootPath);
    }

    @Override
    public boolean isValidProject() {
        return false;
    }

    @Override
    public boolean hasThemeFile() {
        return false;
    }

    @Override
    public File getThemeFile() {
        return null;
    }

    @Override
    public File getFrontendFolder() {
        return null;
    }

    @Override
    public void setFrontendFolder(File frontendFolder) {

    }

    @Override
    public ProjectType getType() {
        return ProjectType.GRADLE;
    }

    @Override
    public boolean hasMissingProjectInformation() {
        return false;
    }

    @Override
    public Stream<ProjectInformation> getMissingProjectInformation() {
        return null;
    }

    @Override
    public File getSourceFolder() {
        return null;
    }

    @Override
    public void setSourceFolder(File folder) {

    }

    @Override
    public File getResourceFolder() {
        return null;
    }

    @Override
    public void setResourceFolder(File folder) {

    }

    @Override
    public boolean hasTranslations() {
        return false;
    }

    @Override
    public ResourceBundle getTranslationsBundle() {
        return null;
    }

    @Override
    public String getTranslationForKey(String key) {
        return null;
    }

    @Override
    public void setThemeFile(File themeFile) {

    }

    @Override
    public boolean createThemeFile() {
        return false;
    }
}
