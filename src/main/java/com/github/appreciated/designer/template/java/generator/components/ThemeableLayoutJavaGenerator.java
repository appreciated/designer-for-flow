package com.github.appreciated.designer.template.java.generator.components;

import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.ThemableLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ThemeableLayoutJavaGenerator implements ComponentJavaGenerator<ThemableLayout> {

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof ThemableLayout;
    }

    @Override
    public boolean requiresGeneration(ThemableLayout propertyParent) {
        return propertyParent instanceof ThemableLayout;
    }

    @Override
    public Stream<Expression> generate(CompilationUnit compilationUnit, ThemableLayout propertyParent, Expression nameExpr) {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(new MethodCallExpr(nameExpr, "setMargin", new NodeList<>(new BooleanLiteralExpr(propertyParent.isMargin()))));
        expressionList.add(new MethodCallExpr(nameExpr, "setPadding", new NodeList<>(new BooleanLiteralExpr(propertyParent.isPadding()))));
        expressionList.add(new MethodCallExpr(nameExpr, "setSpacing", new NodeList<>(new BooleanLiteralExpr(propertyParent.isSpacing()))));
        return expressionList.stream();
    }

    @Override
    public List<String> generatedProperties() {
        return Arrays.asList("margin", "padding", "spacing");
    }

}
