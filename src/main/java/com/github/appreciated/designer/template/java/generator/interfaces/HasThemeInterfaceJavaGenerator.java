package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasTheme;

import java.util.stream.Stream;

public class HasThemeInterfaceJavaGenerator implements VaadinComponentJavaGenerator<HasTheme> {

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasTheme;
    }

    @Override
    public boolean requiresParsing(HasTheme propertyParent) {
        return propertyParent.getThemeName() != null;
    }

    @Override
    public Stream<Expression> parse(CompilationUnit compilationUnit, HasTheme propertyParent, Expression nameExpr) {
        MethodCallExpr textAssignment = new MethodCallExpr(nameExpr, "setThemeName", new NodeList<>(new StringLiteralExpr(propertyParent.getThemeName())));
        return Stream.of(textAssignment);
    }

}
