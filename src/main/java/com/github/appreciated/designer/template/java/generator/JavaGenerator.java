package com.github.appreciated.designer.template.java.generator;

import com.github.appreciated.designer.component.DesignerComponentWrapper;
import com.github.appreciated.designer.helper.ComponentContainerHelper;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.interfaces.VaadinComponentJavaGeneratorCollection;
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
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.orderedlayout.Scroller;

import javax.annotation.Generated;
import java.io.File;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGenerator {
    private final CompilationUnit compilationUnit;
    private final ConstructorDeclaration constructor;
    private final SourceRoot sourceRoot;
    private final DesignCompilerInformation designCompilerInformation;
    private final Map<String, Integer> counters = new HashMap<>();
    private final VaadinComponentJavaGeneratorCollection compilerCollection;
    private ClassOrInterfaceDeclaration componentClass;

    public JavaGenerator(DesignCompilerInformation designCompilerInformation) {
        compilerCollection = new VaadinComponentJavaGeneratorCollection(designCompilerInformation);
        this.designCompilerInformation = designCompilerInformation;
        Component component = designCompilerInformation.getComponent();
        File file = designCompilerInformation.getDesign();
        if (designCompilerInformation.getDesign().getName().endsWith(".java")) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sourceRoot = new SourceRoot(designCompilerInformation.getProject().getProjectRoot().toPath());
            compilationUnit = new CompilationUnit();
            compilationUnit.setStorage(file.toPath());
            String packageName = file.getParentFile().getPath().substring(designCompilerInformation.getProject().getSourceFolder().getPath().length() + 1).replace(File.separator, ".");
            compilationUnit.setPackageDeclaration(packageName);
            sourceRoot.add(compilationUnit);
            componentClass = compilationUnit.addClass(file.getName().substring(0, file.getName().length() - 5));
            compilationUnit.addImport(Generated.class);
            componentClass = componentClass.addAnnotation(new NormalAnnotationExpr(new Name("Generated"),
                    new NodeList<>(
                            new MemberValuePair("value", new StringLiteralExpr("com.github.appreciated.designer.template.java.generator.JavaGenerator")),
                            new MemberValuePair("comments", new StringLiteralExpr("This is generated code, do not modify")),
                            new MemberValuePair("date", new StringLiteralExpr(ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)))
                    )
            ));
            constructor = componentClass.addConstructor(Modifier.Keyword.PUBLIC);
            componentClass.addExtendedType(unwrapComponent(component).getClass());
            addChildren(new ThisExpr(), component);
            addFieldProperties(new ThisExpr(), component);

        } else {
            throw new IllegalStateException("The file name suffix must be *.java");
        }
    }

    public void save() {
        sourceRoot.saveAll(designCompilerInformation.getDesign().toPath());
    }

    private FieldDeclaration addComponent(Component component) {
        FieldDeclaration field = null;
        ObjectCreationExpr creation = new ObjectCreationExpr();
        // Project Components
        Component actualComponent = unwrapComponent(component);
        if (designCompilerInformation.hasCompilationMetaInformation(actualComponent) && designCompilerInformation.getCompilationMetaInformation(actualComponent).isProjectComponent()) {
            compilationUnit.addImport(designCompilerInformation.getCompilationMetaInformation(actualComponent).getPackageDeclaration() + "." + designCompilerInformation.getCompilationMetaInformation(actualComponent).getClassName());
            creation.setType(designCompilerInformation.getCompilationMetaInformation(actualComponent).getClassName());

            field = componentClass.addFieldWithInitializer(
                    designCompilerInformation.getCompilationMetaInformation(actualComponent).getClassName(),
                    designCompilerInformation.getCompilationMetaInformation(actualComponent).getClassName() + getCounterForComponent(designCompilerInformation.getCompilationMetaInformation(actualComponent).getClassName()), creation
            );
        } else {
            // All other Components
            creation.setType(actualComponent.getClass());
            field = componentClass.addFieldWithInitializer(actualComponent.getClass(), getGeneratedFieldName(actualComponent) + getCounterForComponent(actualComponent), creation);
            addFieldProperties(new NameExpr(field.getVariables().get(0).getName()), actualComponent);
            if (ComponentContainerHelper.isComponentContainer(actualComponent, designCompilerInformation) && ComponentContainerHelper.getChildren(actualComponent).count() > 0) {
                addChildren(new NameExpr(field.getVariables().get(0).getName()), actualComponent);
            }
        }
        field.createGetter();
        field.createSetter();
        // add a none parameterised initialization (a AssignExpr) to the constructor
        return field;
    }

    String getGeneratedFieldName(Component actualComponent) {
        String name = actualComponent.getClass().getSimpleName();
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    private int getCounterForComponent(Component simpleName) {
        return getCounterForComponent(simpleName.getClass().getSimpleName());
    }

    private int getCounterForComponent(String name) {
        if (counters.containsKey(name)) {
            counters.put(name, counters.get(name) + 1);
        } else {
            counters.put(name, 1);
        }
        return counters.get(name);
    }

    private void addFieldProperties(Expression field, Component component) {
        Component actualComponent = unwrapComponent(component);
        compilerCollection.stream()
                .filter(parser -> parser.canParse(actualComponent) && parser.requiresParsing(actualComponent))
                .map(parser -> (Stream<Expression>) parser.parse(compilationUnit, actualComponent, field))
                .forEach(expressionStream ->
                        expressionStream.forEach(expression -> constructor.getBody().addAndGetStatement(expression))
                );
    }

    private void addChildren(Expression expression, Component component) {
        Component actualComponent = component instanceof DesignerComponentWrapper ? ((DesignerComponentWrapper) component).getActualComponent() : component;
        if (actualComponent instanceof HasComponents) {
            addChildrenHasComponents(expression, component, "add", true);
        } else if (actualComponent instanceof AccordionPanel) {
            addChildrenHasComponents(expression, component, "addContent", false);
        } else if (actualComponent instanceof Accordion) {
            addChildrenHasComponents(expression, component, "add", false);
        } else if (actualComponent instanceof Scroller) {
            addChildrenHasComponents(expression, component, "setContent", false);
        } else {
            throw new IllegalStateException("The following component class is currently not supported: " + component.getClass().getSimpleName());
        }
    }

    private void addChildrenHasComponents(Expression expression, Component component, String method, boolean addAllAtOnce) {
        Collection<Expression> fields = ComponentContainerHelper.getChildren(unwrapComponent(component))
                .map(this::addComponent)
                .map(newField -> new NameExpr(newField.getVariables().get(0).getName()))
                .collect(Collectors.toList());
        if (addAllAtOnce) {
            constructor.getBody().addAndGetStatement(
                    new MethodCallExpr(
                            expression,
                            method,
                            new NodeList<>(fields)
                    )
            );
        } else {
            fields.forEach(field -> constructor.getBody().addAndGetStatement(
                    new MethodCallExpr(expression, method, new NodeList<>(field))
            ));
        }
    }

    private Component unwrapComponent(Component component) {
        if (component instanceof DesignerComponentWrapper) {
            return ((DesignerComponentWrapper) component).getActualComponent();
        }
        return component;
    }

}
