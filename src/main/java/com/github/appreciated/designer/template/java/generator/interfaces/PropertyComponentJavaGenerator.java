package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.vaadin.flow.component.Component;

import java.util.stream.Stream;

public interface PropertyComponentJavaGenerator {

    void setAlreadyParsedProperties(Stream<String> alreadyParsedProperties);

    boolean requiresGeneration(Component propertyParent);

    Stream<MethodCallExpr> generate(CompilationUnit compilationUnit, Component propertyParent, Expression nameExpr);

}
