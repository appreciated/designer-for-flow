package com.github.appreciated.designer.template.java.generator.components;

import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasTheme;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class HasThemeJavaGenerator implements ComponentJavaGenerator<HasTheme> {

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasTheme;
    }

    @Override
    public boolean requiresGeneration(HasTheme propertyParent) {
        return propertyParent.getThemeName() != null;
    }

    @Override
    public Stream<Expression> generate(CompilationUnit compilationUnit, HasTheme propertyParent, Expression nameExpr) {
        MethodCallExpr textAssignment = new MethodCallExpr(nameExpr, "setThemeName", new NodeList<>(new StringLiteralExpr(propertyParent.getThemeName())));
        return Stream.of(textAssignment);
    }

    @Override
    public List<String> generatedProperties() {
        return Arrays.asList("themeName");
    }
}
