package com.github.appreciated.designer.template.java.compiler;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.template.java.compiler.interfaces.VaadinInterfaceCompilerCollection;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.utils.SourceRoot;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DesignerComponentTreeCompiler {
    private final CompilationUnit compilationUnit;
    private final ClassOrInterfaceDeclaration componentClass;
    private final ConstructorDeclaration constructor;
    private final SourceRoot sourceRoot;
    private final ProjectService service;
    int i = 0;
    private VaadinInterfaceCompilerCollection compilerCollection;

    public DesignerComponentTreeCompiler(ProjectService service) {
        compilerCollection = new VaadinInterfaceCompilerCollection(service);
        this.service = service;
        Component component = service.getCurrentFile().getComponent();
        File file = service.getCurrentFile().getDesign();
        if (service.getCurrentFile().getDesign().getName().endsWith(".java")) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sourceRoot = new SourceRoot(service.getProject().getProjectRoot().toPath());
            compilationUnit = new CompilationUnit();
            compilationUnit.setStorage(file.toPath());
            sourceRoot.add(compilationUnit);
            componentClass = compilationUnit.addClass(file.getName().substring(0, file.getName().length() - 5));
            constructor = componentClass.addConstructor(Modifier.Keyword.PUBLIC);
            componentClass.addExtendedType(unwrapComponent(component).getClass());
            addChildren(new ThisExpr(), component);
            addFieldProperties(new ThisExpr(), component);

        } else {
            throw new IllegalStateException("The file name suffix must be *.java");
        }
    }

    public void save() {
        sourceRoot.saveAll(service.getCurrentFile().getDesign().toPath());
    }

    private FieldDeclaration addComponent(Component component) {
        Component actualComponent = unwrapComponent(component);
        // create a field for the new component
        ObjectCreationExpr creation = new ObjectCreationExpr();
        creation.setType(actualComponent.getClass());
        FieldDeclaration field = componentClass.addFieldWithInitializer(actualComponent.getClass(), "component" + i++, creation);
        field.createGetter();
        field.createSetter();
        // add a none parameterised initialization (a AssignExpr) to the constructor
        addFieldProperties(new NameExpr(field.getVariables().get(0).getName()), actualComponent);
        if (actualComponent instanceof HasComponents && actualComponent.getChildren().count() > 0) {
            addChildren(new NameExpr(field.getVariables().get(0).getName()), actualComponent);
        }
        return field;
    }

    private void addFieldProperties(Expression field, Component component) {
        Component actualComponent = unwrapComponent(component);
        compilerCollection.stream()
                .filter(parser -> parser.canParse(actualComponent) && parser.requiresParsing(actualComponent))
                .map(parser -> (Stream<Expression>) parser.parse(compilationUnit, actualComponent, field))
                .forEach(expressionStream -> expressionStream.forEach(
                        expression -> {
                            constructor.getBody().addAndGetStatement(expression);
                        }));
    }


    private void addChildren(Expression expression, Component component) {
        Collection<Expression> fields = unwrapComponent(component).getChildren()
                .map(this::addComponent)
                .map(newField -> new NameExpr(newField.getVariables().get(0).getName()))
                .collect(Collectors.toList());

        constructor.getBody().addAndGetStatement(
                new MethodCallExpr(
                        expression,
                        "add",
                        new NodeList<>(fields)
                ));
    }

    private Component unwrapComponent(Component component) {
        if (component instanceof DesignerComponentWrapper) {
            return ((DesignerComponentWrapper) component).getActualComponent();
        }
        return component;
    }

}
