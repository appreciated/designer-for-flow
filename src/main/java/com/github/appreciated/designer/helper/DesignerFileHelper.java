package com.github.appreciated.designer.helper;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.model.project.Project;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DesignerFileHelper {

    public static boolean isFileDesignerFile(File file) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
            return lines.stream().anyMatch(line -> line.startsWith("@Generated(value = \"com.github.appreciated.designer.template.java.generator.JavaGenerator\""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static CompilationUnit getCompilationUnitFromDesignerFile(ProjectFileModel model, File file) {
        Project project = model.getInformation().getProject();
        return getCompilationUnitFromDesignerFile(project.getProjectRoot().toPath(), file);
    }

    public static CompilationUnit getCompilationUnitFromDesignerFile(Path sourceRootPath, File file) {
        SourceRoot sourceRoot = new SourceRoot(sourceRootPath);
        return sourceRoot.parse(CodeGenerationUtils.packageToPath(file.getParent()), file.getName());
    }
}
