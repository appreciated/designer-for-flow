package com.github.appreciated.designer.template.java.generator.components;

import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class HasStyleJavaGenerator implements ComponentJavaGenerator<HasStyle> {

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasStyle;
    }

    @Override
    public boolean requiresParsing(HasStyle propertyParent) {
        return propertyParent.getStyle().getNames().count() > 0;
    }

    @Override
    public Stream<Expression> parse(CompilationUnit compilationUnit, HasStyle propertyParent, Expression nameExpr) {
        List<Expression> expressionList = new ArrayList<>();
        propertyParent.getStyle().getNames().forEach(s ->
                // TODO why is the test String required?
                new MethodCallExpr(nameExpr, "test",
                        new NodeList<>(new MethodCallExpr(
                                new MethodCallExpr(new MethodCallExpr(), "getStyle"),
                                "set",
                                new NodeList<>(new StringLiteralExpr(s), new StringLiteralExpr(propertyParent.getStyle().get(s)))
                        ))
                ));
        return expressionList.stream();
    }

    @Override
    public List<String> generatedProperties() {
        return Arrays.asList("style");
    }

}
