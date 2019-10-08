package com.github.appreciated.designer.model.project;

import com.github.appreciated.designer.model.project.maven.MavenProject;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ProjectTypes {
    List<Project> scanners;

    public ProjectTypes(File projectFolder) {
        this.scanners = Collections.singletonList(new MavenProject(projectFolder));
    }

    public boolean hasFittingProjectType() {
        return scanners.stream().anyMatch(Project::isValidProject);
    }

    public Project getFittingProjectType() {
        return scanners.stream().filter(Project::isValidProject).findFirst().get();
    }
}
