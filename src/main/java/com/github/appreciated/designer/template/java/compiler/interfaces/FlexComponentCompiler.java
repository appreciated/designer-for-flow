package com.github.appreciated.designer.template.java.compiler.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FlexComponentCompiler implements VaadinComponentCompiler<FlexComponent> {

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof FlexComponent;
    }

    @Override
    public boolean requiresParsing(FlexComponent propertyParent) {
        return propertyParent.getAlignItems() != null || propertyParent.getJustifyContentMode() != null;
    }

    @Override
    public Stream<Expression> parse(CompilationUnit compilationUnit, FlexComponent propertyParent, Expression nameExpr) {
        List<Expression> expressionList = new ArrayList<>();
        if (propertyParent.getAlignItems() != null) {
            compilationUnit.addImport(FlexComponent.class);
            expressionList.add(new MethodCallExpr(nameExpr, "setAlignItems", new NodeList<>(
                    new FieldAccessExpr(
                            new FieldAccessExpr(
                                    new NameExpr(FlexComponent.class.getSimpleName()),
                                    "Alignment"), propertyParent.getAlignItems().name()
                    )
            )));
        }

        if (propertyParent.getJustifyContentMode() != null) {
            compilationUnit.addImport(FlexComponent.class);
            expressionList.add(
                    new MethodCallExpr(nameExpr, "setJustifyContentMode", new NodeList<>(
                            new FieldAccessExpr(
                                    new FieldAccessExpr(
                                            new NameExpr(FlexComponent.class.getSimpleName()),
                                            "JustifyContentMode"), propertyParent.getJustifyContentMode().name()
                            )
                    )));
        }
        return expressionList.stream();
    }

}
