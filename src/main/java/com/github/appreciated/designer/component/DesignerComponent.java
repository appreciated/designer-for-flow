package com.github.appreciated.designer.component;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.model.CompilationMetainformation;
import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.template.java.parser.ComponentTreeParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.vaadin.flow.component.Component;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import static com.github.appreciated.designer.helper.DesignerFileHelper.getCompilationUnitFromDesignerFile;

public class DesignerComponent {
    String name;
    Component component;
    private final String classNameAsString;
    private Class<? extends Component> className;
    private File file;

    public DesignerComponent(String name, Class<? extends Component> className) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        this.name = name;
        this.className = className;
        classNameAsString = this.className.getSimpleName();
        className.getDeclaredConstructor().newInstance();
    }

    public DesignerComponent(File file) {
        this.file = file;
        classNameAsString = file.getName().substring(0, file.getName().length() - 5);
    }

    public String getTagName() {
        return "<" + getName() + ">";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Component getComponent() {
        return component;
    }

    public DesignerComponentWrapper generateComponent(ProjectFileModel projectModel) {
        if (file != null) {
            try {
                Project project = projectModel.getInformation().getProject();
                CompilationUnit compilationUnit = getCompilationUnitFromDesignerFile(projectModel, file);
                Component generatedComponent = new ComponentTreeParser(compilationUnit, project).getComponent();
                CompilationMetainformation info = projectModel.getInformation().getOrCreateCompilationMetainformation(generatedComponent);
                info.setProjectComponent(true);
                info.setPackage(compilationUnit.getPackageDeclaration().get().getName().asString());
                info.setClassName(compilationUnit.getPrimaryTypeName().get());
                return new DesignerComponentWrapper(generatedComponent, true);
            } catch (ParseException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return new DesignerComponentWrapper(className.getDeclaredConstructor().newInstance(), false);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                //e.printStackTrace();
            }
        }
        return null;
    }

    public String getClassNameAsString() {
        return classNameAsString;
    }

    public Class<? extends Component> getClassName() {
        return className;
    }
}


