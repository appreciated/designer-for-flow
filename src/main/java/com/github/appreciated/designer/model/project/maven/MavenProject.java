package com.github.appreciated.designer.model.project.maven;

import com.github.appreciated.designer.exception.MissingProjectFileException;
import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.model.project.ProjectInformation;
import com.github.appreciated.designer.model.project.ProjectType;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class MavenProject extends Project {

    private String defaultSourcePath = "src" + File.separator + "main" + File.separator + "java";
    private String defaultFrontendPath = "frontend";
    private String defaultThemePath = defaultFrontendPath + File.separator + "styles";
    private String defaultThemeFileName = "theme.css";
    private String projectDefinitionFile = "pom.xml";

    private File themeFile;
    private File frontendFolder;
    private File sourceFolder;

    public MavenProject(File projectRootPath) {
        super(projectRootPath);
    }

    @Override
    public File getThemeFile() {
        if (themeFile != null && themeFile.exists()) {
            return themeFile;
        }
        File defaultThemeFile = new File(getProjectRoot().getPath() + File.separator + defaultThemePath + File.separator + defaultThemeFileName);
        if (defaultThemeFile.exists()) {
            return defaultThemeFile;
        } else {
            throw new MissingProjectFileException("There was no theme file found under \"" + defaultThemeFile.getPath() + "\"");
        }
    }

    @Override
    public void setThemeFile(File themeFile) {
        this.themeFile = themeFile;
    }

    @Override
    public File getFrontendFolder() {
        if (frontendFolder != null && frontendFolder.exists()) {
            return frontendFolder;
        }
        File defaultFrontend = new File(getProjectRoot().getPath() + File.separator + defaultFrontendPath);
        if (defaultFrontend.exists()) {
            return defaultFrontend;
        } else {
            throw new MissingProjectFileException("There was no theme file found under \"" + defaultFrontend.getPath() + "\"");
        }
    }

    @Override
    public void setFrontendFolder(File frontendFolder) {
        this.frontendFolder = frontendFolder;
    }

    @Override
    public ProjectType getType() {
        return ProjectType.MAVEN;
    }

    @Override
    public boolean hasMissingProjectInformation() {
        return isValidProject() && hasThemeFile();
    }

    @Override
    public boolean isValidProject() {
        return Arrays.stream(getProjectRoot().list()).anyMatch(s -> s.equals(projectDefinitionFile));
    }

    @Override
    public boolean hasThemeFile() {
        return (themeFile != null || new File(getProjectRoot().getPath() + File.separator + defaultThemePath + File.separator + defaultThemeFileName).exists());
    }

    @Override
    public Stream<ProjectInformation> getMissingProjectInformation() {
        return Stream.empty();
    }

    @Override
    public File getSourceFolder() {
        if (sourceFolder != null && sourceFolder.exists()) {
            return sourceFolder;
        }
        File defaultSourceFolder = new File(getProjectRoot().getPath() + File.separator + defaultSourcePath);
        if (defaultSourceFolder.exists()) {
            return defaultSourceFolder;
        } else {
            throw new MissingProjectFileException("There was no theme file found under \"" + defaultSourceFolder.getPath() + "\"");
        }
    }

    @Override
    public void setSourceFolder(File sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

}
