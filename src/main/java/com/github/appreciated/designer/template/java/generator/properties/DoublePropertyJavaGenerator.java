package com.github.appreciated.designer.template.java.generator.properties;

import com.github.appreciated.designer.application.component.ComponentPropertyParser;
import com.github.appreciated.designer.application.component.CustomPropertyDescriptor;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.interfaces.PropertyComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoublePropertyJavaGenerator implements PropertyComponentJavaGenerator {

    private final DesignCompilerInformation designCompilerInformation;
    private List<String> alreadyParsedProperties;
    private List<CustomPropertyDescriptor> propertiesRequireParsing;

    public DoublePropertyJavaGenerator(DesignCompilerInformation designCompilerInformation) {
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
                .filter(propertyDescriptor -> propertyDescriptor.getPropertyType() == Double.class || propertyDescriptor.getPropertyType() == double.class)
                .collect(Collectors.toList());
        return propertiesRequireParsing.size() > 0;
    }

    @Override
    public Stream<MethodCallExpr> generate(CompilationUnit compilationUnit, Component propertyParent, Expression nameExpr) {
        return propertiesRequireParsing.stream()
                .filter(propertyDescriptor -> {
                    try {
                        Double value = ((Double) propertyDescriptor.getReadMethod().invoke(propertyParent));
                        return value != null && !value.equals("0.0") && !value.isInfinite();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .map(propertyDescriptor -> {
                    try {
                        return new MethodCallExpr(nameExpr, propertyDescriptor.getWriteMethod().getName(), new NodeList<>(new DoubleLiteralExpr((Double) propertyDescriptor.getReadMethod().invoke(propertyParent))));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

}
