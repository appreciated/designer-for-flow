package com.github.appreciated.designer.template.java.generator.properties;

import com.github.appreciated.designer.component.ComponentPropertyParser;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.interfaces.PropertyComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringPropertyJavaGenerator implements PropertyComponentJavaGenerator {

    private final DesignCompilerInformation designCompilerInformation;
    private Stream<String> alreadyParsedProperties;
    private List<PropertyDescriptor> propertiesRequireParsing;

    public StringPropertyJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasText;
    }

    @Override
    public void setAlreadyParsedProperties(Stream<String> alreadyParsedProperties) {
        this.alreadyParsedProperties = alreadyParsedProperties;
    }

    @Override
    public boolean requiresParsing(Component propertyParent) {
        ComponentPropertyParser propertyParser = new ComponentPropertyParser(propertyParent);
        propertiesRequireParsing = propertyParser.getProperties().values().stream()
                .filter(propertyDescriptor -> alreadyParsedProperties.noneMatch(s -> s.equals(propertyDescriptor.getName())))
                .filter(propertyDescriptor -> propertyDescriptor.getPropertyType() == String.class)
                .collect(Collectors.toList());
        return propertiesRequireParsing.size() > 0;
    }

    @Override
    public Stream<MethodCallExpr> parse(CompilationUnit compilationUnit, Component propertyParent, Expression nameExpr) {
        return propertiesRequireParsing.stream()
                .map(propertyDescriptor -> {
                    try {
                        return new MethodCallExpr(nameExpr, propertyDescriptor.getWriteMethod().getName(), new NodeList<>(new StringLiteralExpr((String) propertyDescriptor.getReadMethod().invoke(propertyParent))));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull);
    }

}
