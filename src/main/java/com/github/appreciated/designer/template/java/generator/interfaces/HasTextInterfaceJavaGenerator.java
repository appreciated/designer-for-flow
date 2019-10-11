package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;

import java.util.stream.Stream;

public class HasTextInterfaceJavaGenerator implements VaadinComponentJavaGenerator<HasText> {
    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasText;
    }

    @Override
    public boolean requiresParsing(HasText propertyParent) {
        return propertyParent.getText() != null && !propertyParent.getText().equals("");
    }

    @Override
    public Stream<Expression> parse(CompilationUnit compilationUnit, HasText propertyParent, Expression nameExpr) {
        MethodCallExpr textAssignment = new MethodCallExpr(nameExpr, "setText", new NodeList<>(new StringLiteralExpr(propertyParent.getText())));
        return Stream.of(textAssignment);
    }

}
