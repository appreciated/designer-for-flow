package com.github.appreciated.designer.template.java.parser;

import com.github.appreciated.designer.model.CompilationMetainformation;
import com.github.appreciated.designer.model.project.Project;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.data.binder.HasItems;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentTreeParser {

    private final ClassOrInterfaceDeclaration classDefinition;
    Component rootComponent;
    Map<String, Component> fieldMap = new HashMap<>();

    private CompilationUnit compilationUnit;
    private Project project;
    private String className;

    private Map<Component, CompilationMetainformation> compilationMetaInformation = new HashMap<>();

    public ComponentTreeParser(CompilationUnit compilationUnit, Project project) throws ParseException, ClassNotFoundException {
        this.compilationUnit = compilationUnit;
        this.project = project;
        if (compilationUnit.getPrimaryType().isPresent()) {
            TypeDeclaration<?> definition = compilationUnit.getPrimaryType().get();
            if (definition instanceof ClassOrInterfaceDeclaration) {
                classDefinition = (ClassOrInterfaceDeclaration) definition;
            } else {
                throw new ParseException("Nope!");
            }
        } else {
            throw new ParseException("Nope!");
        }
        instantiateRootComponent();
        classDefinition.getFields().forEach(this::instantiateField);
        // If the design does not have a constructor there is nothing left to do
        if (classDefinition.getConstructors().size() > 0) {
            for (Statement statement : classDefinition.getConstructors().get(0).getBody().getStatements()) {
                processBodyStatement(statement);
            }
        }
    }

    public Object invokeMethodOnComponent(MethodCallExpr expression, Component component) throws ClassNotFoundException {
        List<Object> list = new ArrayList<>();
        for (Expression expression1 : expression.getArguments()) {
            Object o = resolveParameter(expression1, component);
            list.add(o);
        }
        Object[] args = list.toArray();

        Method[] foundMethods = Arrays.stream(component.getClass().getMethods())
                .filter(method1 -> method1.getName().equals(expression.getNameAsString()))
                .filter(method1 -> method1.getParameterCount() == args.length || method1.isVarArgs())
                .toArray(Method[]::new);

        boolean found = false;
        ArrayList<Throwable> errors = new ArrayList<>();
        Object result = null;
        for (Method foundMethod : foundMethods) {
            try {
                if (args.length == 1 && !foundMethod.isVarArgs()) {
                    result = foundMethod.invoke(component, args[0]);
                    found = true;
                } else {
                    if (args[0].getClass() == String.class) {
                        result = foundMethod.invoke(component, new Object[]{Arrays.stream(args).toArray(String[]::new)});
                        if (component instanceof HasItems && foundMethod.getName().equals("setItems")) {
                            CompilationMetainformation info = new CompilationMetainformation();
                            info.setPropertyReplacement("items", Arrays.stream(Arrays.stream(args).toArray(String[]::new)).collect(Collectors.toSet()));
                            compilationMetaInformation.put(component, info);
                        }
                    } else {
                        result = foundMethod.invoke(component, new Object[]{Arrays.stream(args).toArray(Component[]::new)});
                    }
                    found = true;
                }
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                // These are only relevant if no fitting method was found so we save them for later
                errors.add(e);
            }
        }
        if (!found) {
            errors.forEach(throwable -> throwable.printStackTrace());
            throw new UnsupportedOperationException("No fitting Method found for:\"" + expression.getNameAsString() + "\"");
        }
        return result;
    }

    Object resolveParameter(Expression arg, Component component) throws ClassNotFoundException {
        if (arg instanceof StringLiteralExpr) {
            return ((StringLiteralExpr) arg).asString();
        } else if (arg instanceof BooleanLiteralExpr) {
            return ((BooleanLiteralExpr) arg).getValue();
        } else if (arg instanceof NameExpr) {
            return fieldMap.get(((NameExpr) arg).getNameAsString());
        } else if (arg instanceof FieldAccessExpr) {
            return resolveFieldAccess((FieldAccessExpr) arg);
        } else if (arg instanceof MethodCallExpr) {
            if (((MethodCallExpr) arg).getNameAsString().equals("getTranslation")) {
                StringLiteralExpr argumentExpression = (StringLiteralExpr) ((MethodCallExpr) arg).getArguments().getFirst().get();
                CompilationMetainformation info = new CompilationMetainformation();
                info.setPropertyReplacement("text", argumentExpression.asString());
                compilationMetaInformation.put(component, info);
                return project.getTranslationForKey(argumentExpression.getValue());
            }
            return invokeMethodOnComponent((MethodCallExpr) arg, rootComponent);
        } else {
            throw new UnsupportedOperationException(arg.getClass().getSimpleName() + " is not supported");
        }
    }

    private Object resolveFieldAccess(FieldAccessExpr arg) throws ClassNotFoundException {
        if (fieldMap.containsKey(arg.getNameAsString())) {
            accessFieldOfObject(fieldMap.get(arg.getNameAsString()), arg);
        } else {
            FlexComponent.Alignment.CENTER.name();
            String[] scope = resolveScope(arg).toArray(String[]::new);
            String resolved = resolveName(scope[0]);
            Class classname = Class.forName(resolved + "$" + scope[1]);
            return Enum.valueOf(classname, scope[2]);
        }
        return null;
    }

    private Object accessFieldOfObject(Object clazz, FieldAccessExpr arg) {
        String[] access = resolveScope(arg).skip(1).toArray(String[]::new);
        return null;
    }

    Stream<String> resolveScope(FieldAccessExpr expr) {
        if (expr.getScope() instanceof NameExpr) {
            return Stream.of(((NameExpr) expr.getScope()).getNameAsString(), expr.getNameAsString());
        } else {
            return Stream.concat(resolveScope((FieldAccessExpr) expr.getScope()), Stream.of(expr.getNameAsString()));
        }
    }

    private void instantiateRootComponent() {
        className = classDefinition.getNameAsString();
        String typeName = classDefinition.getExtendedTypes().get(0).getNameAsString();
        rootComponent = (Component) instantiateClassWithName(typeName);
    }

    private Object instantiateClassWithName(String typeName) {
        try {
            Class<?> className = Class.forName(resolveName(typeName));
            return className.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String resolveName(String typeName) {
        return compilationUnit.getImports()
                .stream()
                .filter(importDeclaration -> importDeclaration.getName().asString().endsWith(typeName))
                .findFirst().get().getName().asString();
    }

    private void instantiateField(FieldDeclaration field) {
        String fieldName = field.getVariables().get(0).getNameAsString();
        String fieldType = resolveName(field.getElementType().asString());
        fieldMap.put(fieldName, (Component) instantiateClassWithName(fieldType));
    }

    private void processBodyStatement(Statement statement) throws ClassNotFoundException {
        if (statement instanceof ExpressionStmt) {
            Expression expression = ((ExpressionStmt) statement).getExpression();
            if (expression instanceof MethodCallExpr) {
                MethodCallExpr methodCallExpr = (MethodCallExpr) expression;
                Component relatedComponent = resolveComponent(methodCallExpr);
                invokeMethodOnComponent(methodCallExpr, relatedComponent);
            } else {
                throw new UnsupportedOperationException("Currently only MethodCallExpr are supported");
            }
        } else {
            throw new UnsupportedOperationException("Currently only ExpressionStmt are supported");
        }
    }

    private Component resolveComponent(MethodCallExpr methodCallExpr) {
        Expression variableExpression = methodCallExpr.getScope().get();
        if (variableExpression instanceof NameExpr) {
            String name = methodCallExpr.getScope().get().asNameExpr().getNameAsString();
            return fieldMap.get(name);
        } else if (variableExpression instanceof ThisExpr) {
            return rootComponent;
        } else {
            throw new UnsupportedOperationException("Currently only NameExpr and ThisExpr are supported");
        }
    }

    public Component getComponent() {
        return rootComponent;
    }

    public String getClassName() {
        return className;
    }

    public Map<Component, CompilationMetainformation> getCompilationMetaInformation() {
        return compilationMetaInformation;
    }
}
