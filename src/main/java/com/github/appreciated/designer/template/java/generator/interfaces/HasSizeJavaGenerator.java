package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HasSizeJavaGenerator implements VaadinComponentJavaGenerator<HasSize> {

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasSize;
    }

    @Override
    public boolean requiresParsing(HasSize propertyParent) {
        if (propertyParent instanceof Component && ((Component) propertyParent).getParent().isPresent()) {
            Component parent = ((Component) propertyParent).getParent().get();
            if (parent instanceof HasSize) {
                HasSize hasSizeParent = ((HasSize) parent);
                return hasSizeParent.getWidth() != null || hasSizeParent.getHeight() != null;
            }
        }
        return false;
    }

    @Override
    public Stream<Expression> parse(CompilationUnit compilationUnit, HasSize propertyParent, Expression nameExpr) {
        List<Expression> expressionList = new ArrayList<>();
        HasSize parent = ((HasSize) ((Component) propertyParent).getParent().get());
        if (parent.getWidth() != null && !parent.getWidth().equals("")) {
            expressionList.add(new MethodCallExpr(nameExpr, "setWidth", new NodeList<>(new StringLiteralExpr(parent.getWidth()))));
        }
        if (parent.getHeight() != null && !parent.getHeight().equals("")) {
            expressionList.add(new MethodCallExpr(nameExpr, "setHeight", new NodeList<>(new StringLiteralExpr(parent.getHeight()))));
        }
        return expressionList.stream();
    }

}
