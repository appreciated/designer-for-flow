package com.github.appreciated.designer.template.java.generator.components;

import com.github.appreciated.designer.application.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.HasItems;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HasItemsJavaGenerator implements ComponentJavaGenerator<HasItems> {

    private final DesignCompilerInformation designCompilerInformation;

    public HasItemsJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasItems;
    }

    @Override
    public boolean requiresGeneration(HasItems propertyParent) {
        return designCompilerInformation.hasCompilationMetaInformation((Component) propertyParent) && designCompilerInformation.getCompilationMetaInformation((Component) propertyParent).hasPropertyReplacement("items");
    }

    @Override
    public Stream<Expression> generate(CompilationUnit compilationUnit, HasItems propertyParent, Expression nameExpr) {
        Collection<String> items = (Collection<String>) designCompilerInformation.getCompilationMetaInformation((Component) propertyParent).getPropertyReplacement("items");
        return Stream.of(new MethodCallExpr(nameExpr, "setItems", new NodeList<>(items.stream().map(StringLiteralExpr::new).collect(Collectors.toList()))));
    }


    @Override
    public List<String> generatedProperties() {
        return Arrays.asList("items");
    }

}
