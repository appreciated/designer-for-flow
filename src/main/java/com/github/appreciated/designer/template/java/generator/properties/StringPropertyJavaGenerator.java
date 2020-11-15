package com.github.appreciated.designer.template.java.generator.properties;

import com.github.appreciated.designer.application.component.ComponentPropertyParser;
import com.github.appreciated.designer.application.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.interfaces.PropertyComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringPropertyJavaGenerator implements PropertyComponentJavaGenerator {

    private final DesignCompilerInformation designCompilerInformation;
    private List<String> alreadyParsedProperties;
    private List<CustomPropertyDescriptor> propertiesRequireParsing;

    public StringPropertyJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public void setAlreadyParsedProperties(Stream<String> alreadyParsedProperties) {
        this.alreadyParsedProperties = alreadyParsedProperties.collect(Collectors.toList());
    }

    @Override
    public boolean requiresGeneration(Component propertyParent) {
        ComponentPropertyParser propertyParser = new ComponentPropertyParser(propertyParent);
        Map<String, CustomPropertyDescriptor> properties = propertyParser.getProperties();
        propertiesRequireParsing = properties.values().stream()
                .filter(propertyDescriptor -> alreadyParsedProperties.stream().noneMatch(s -> s.equals(propertyDescriptor.getName())))
                .filter(propertyDescriptor -> propertyDescriptor.getPropertyType() == String.class)
                .collect(Collectors.toList());
        return propertiesRequireParsing.size() > 0;
    }

    @Override
    public Stream<MethodCallExpr> generate(CompilationUnit compilationUnit, Component propertyParent, Expression nameExpr) {
        return propertiesRequireParsing.stream()
                .filter(propertyDescriptor -> {
                    try {
                        String value = ((String) propertyDescriptor.getReadMethod().invoke(propertyParent));
                        return value != null && !value.equals("");
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .map(propertyDescriptor -> {
                    try {
                        return new MethodCallExpr(nameExpr, propertyDescriptor.getWriteMethod().getName(), new NodeList<>(new StringLiteralExpr((String) propertyDescriptor.getReadMethod().invoke(propertyParent))));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

}
