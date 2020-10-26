package com.github.appreciated.designer.model;

import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.template.java.parser.DesignerComponentTreeParser;
import com.github.javaparser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DesignFileState {
    private final List<String> state;
    private final File file;

    public DesignFileState(File file) throws IOException {
        this.file = file;
        state = Files.readAllLines(Paths.get(file.getPath()));
    }

    public DesignCompilerInformation revert(Project project) throws IOException {
        FileWriter fw = new FileWriter(file, false);
        for (String s : state) {
            fw.append(s);
        }
        fw.close();
        // generate new DesignCompilerInformation
        DesignerComponentTreeParser parser = null;
        try {
            parser = new DesignerComponentTreeParser(file, project);
        } catch (ParseException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        DesignCompilerInformation info = parser.getRootDesignCompilerInformation();
        info.setProject(project);
        return info;
    }

}
