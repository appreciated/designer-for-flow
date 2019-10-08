package com.github.appreciated.designer.template.java.compiler.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.vaadin.flow.component.Component;

import java.util.stream.Stream;

public interface VaadinComponentCompiler<T> {
    boolean canParse(Component propertyParent);

    boolean requiresParsing(T propertyParent);

    Stream<Expression> parse(CompilationUnit compilationUnit, T propertyParent, Expression nameExpr);

}
