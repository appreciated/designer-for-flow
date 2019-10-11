package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.vaadin.flow.component.Component;

import java.util.stream.Stream;

public interface VaadinComponentJavaGenerator<T> {
    boolean canParse(Component propertyParent);

    boolean requiresParsing(T propertyParent);

    Stream<Expression> parse(CompilationUnit compilationUnit, T propertyParent, Expression nameExpr);

}
