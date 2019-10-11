package com.github.appreciated.designer.template.java.parser;


import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.service.ProjectService;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DesignerComponentTreeParser {

    private static DesignerComponentTreeParser parser;
    private final File file;
    private final ProjectService projectService;
    private final ComponentTreeParser generator;

    public DesignerComponentTreeParser(File file, ProjectService projectService) throws ParseException, ClassNotFoundException {
        this.file = file;
        this.projectService = projectService;
        SourceRoot sourceRoot = new SourceRoot(projectService.getProject().getProjectRoot().toPath());
        CompilationUnit compilationUnit = sourceRoot.parse(CodeGenerationUtils.packageToPath(file.getParent()), file.getName());
        generator = new ComponentTreeParser(compilationUnit);
    }

    public static Component wrap(Component component) {
        if (component instanceof HasComponents) {
            List<Component> children = component.getChildren().collect(Collectors.toList());
            ((HasComponents) component).removeAll();
            children.forEach(child -> ((HasComponents) component).add(wrap(child)));
            return new DesignerComponentWrapper(component);
        }
        return new DesignerComponentWrapper(component);
    }

    public DesignCompilerInformation getDesignCompilerInformation() {
        DesignCompilerInformation info = new DesignCompilerInformation();
        info.setDesign(file);
        info.setProject(projectService.getProject());
        Component parsedComponent = generator.getComponent();
        DesignerComponentNormalizer.normalize(parsedComponent, info);
        info.setComponent(wrap(parsedComponent));
        info.setClassName(generator.getClassName());
        return info;
    }

}