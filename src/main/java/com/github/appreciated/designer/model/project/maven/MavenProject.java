package com.github.appreciated.designer.model.project.maven;

import com.github.appreciated.designer.exception.MissingProjectFileException;
import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.model.project.ProjectInformation;
import com.github.appreciated.designer.model.project.ProjectType;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@Log4j2
public class MavenProject extends Project {

    private String defaultSourcePath = "src" + File.separator + "main" + File.separator + "java";
    private String defaultResourcePath = "src" + File.separator + "main" + File.separator + "resources";
    private String defaultFrontendPath = "frontend";
    private String defaultThemePath = defaultFrontendPath + File.separator + "styles";
    private String defaultThemeFileName = "theme.css";
    private String projectDefinitionFile = "pom.xml";

    private File themeFile;
    private File frontendFolder;
    private File sourceFolder;
    private File resourceFolder;

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
            setThemeFile(defaultThemeFile);
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
    public boolean createThemeFile() {
        File defaultThemeFile = new File(getProjectRoot().getPath() + File.separator + defaultThemePath + File.separator + defaultThemeFileName);

        if (!defaultThemeFile.exists()) {
            try {
                if (defaultThemeFile.createNewFile()) {
                    final BufferedWriter writer = new BufferedWriter(new FileWriter(defaultThemeFile));
                    writer.append(Strings.EMPTY);
                    writer.close();
                    setThemeFile(defaultThemeFile);
                    return true;
                } else {
                    log.error("Failed to create theme file on: " + defaultThemeFile.getPath());
                }
            } catch (IOException e) {
                log.error("Failed to create theme file on: " + defaultThemeFile.getPath(), e);
            }
        }

        return false;
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

    @Override
    public String getTranslationForKey(String key) {
        if (hasTranslations()) {
            return getTranslationsBundle().getString(key);
        }
        return null;
    }

    @Override
    public boolean hasTranslations() {
        File file = new File(getResourceFolder().getPath() + File.separator + "i18n.properties");
        return getResourceFolder() != null && file.exists();
    }

    @Override
    public ResourceBundle getTranslationsBundle() {
        if (hasTranslations()) {
            try {
                File file = getResourceFolder();
                URL[] urls = new URL[]{file.toURI().toURL()};
                ClassLoader loader = new URLClassLoader(urls);
                return ResourceBundle.getBundle("i18n", Locale.getDefault(), loader);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public File getResourceFolder() {
        File resourceFolder = new File(getProjectRoot().getPath() + File.separator + defaultResourcePath);
        if (resourceFolder.exists()) {
            return resourceFolder;
        }
        return null;
    }

    @Override
    public void setResourceFolder(File folder) {
        this.resourceFolder = folder;
    }

}
