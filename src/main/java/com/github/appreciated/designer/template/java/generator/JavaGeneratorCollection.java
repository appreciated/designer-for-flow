package com.github.appreciated.designer.template.java.generator;

import com.github.appreciated.designer.application.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.components.*;
import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.appreciated.designer.template.java.generator.interfaces.PropertyComponentJavaGenerator;
import com.github.appreciated.designer.template.java.generator.properties.DoublePropertyJavaGenerator;
import com.github.appreciated.designer.template.java.generator.properties.StringPropertyJavaGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaGeneratorCollection {

    private final List<ComponentJavaGenerator> componentJavaGenerators = new ArrayList<>();
    private final List<PropertyComponentJavaGenerator> propertyComponentJavaGenerators = new ArrayList<>();

    public JavaGeneratorCollection(DesignCompilerInformation designCompilerInformation) {
        componentJavaGenerators.addAll(Arrays.asList(
                new HasSizeJavaGenerator(),
                new ImageJavaGenerator(designCompilerInformation),
                new HasTextJavaGenerator(designCompilerInformation),
                new HasStyleJavaGenerator(),
                new FormLayoutJavaGenerator(designCompilerInformation),
                new ThemeableLayoutJavaGenerator(),
                new HasThemeJavaGenerator(),
                new FlexComponentJavaGenerator(),
                new TextFieldJavaGenerator(designCompilerInformation),
                new HasItemsJavaGenerator(designCompilerInformation)
        ));

        propertyComponentJavaGenerators.addAll(Arrays.asList(
                new StringPropertyJavaGenerator(designCompilerInformation),
                new DoublePropertyJavaGenerator(designCompilerInformation)
        ));
    }

    public List<ComponentJavaGenerator> getComponentJavaGenerators() {
        return componentJavaGenerators;
    }

    public List<PropertyComponentJavaGenerator> getPropertyComponentJavaGenerators() {
        return propertyComponentJavaGenerators;
    }
}