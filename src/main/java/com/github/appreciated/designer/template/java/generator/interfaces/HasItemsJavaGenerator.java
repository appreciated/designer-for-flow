package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.HasItems;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HasItemsJavaGenerator implements VaadinComponentJavaGenerator<HasItems> {

    private final DesignCompilerInformation designCompilerInformation;

    public HasItemsJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasItems;
    }

    @Override
    public boolean requiresParsing(HasItems propertyParent) {
        return designCompilerInformation.hasComponentMetainfo((Component) propertyParent) && designCompilerInformation.getComponentMetainfo((Component) propertyParent).hasPropertyReplacement("items");
    }

    @Override
    public Stream<Expression> parse(CompilationUnit compilationUnit, HasItems propertyParent, Expression nameExpr) {
        Set<String> items = (Set<String>) designCompilerInformation.getComponentMetainfo((Component) propertyParent).getPropertyReplacement("items");
        return Stream.of(new MethodCallExpr(nameExpr, "setItems", new NodeList<>(items.stream().map(StringLiteralExpr::new).collect(Collectors.toList()))));
    }


}
