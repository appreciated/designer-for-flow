package com.github.appreciated.designer.template.java.generator;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.components.*;
import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.appreciated.designer.template.java.generator.interfaces.PropertyComponentJavaGenerator;
import com.github.appreciated.designer.template.java.generator.properties.StringPropertyJavaGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class JavaGeneratorCollection {

    private final ArrayList<ComponentJavaGenerator> componentJavaGenerators = new ArrayList<>();
    private final ArrayList<PropertyComponentJavaGenerator> propertyComponentJavaGenerators = new ArrayList<>();

    public JavaGeneratorCollection(DesignCompilerInformation designCompilerInformation) {
        componentJavaGenerators.addAll(Arrays.asList(
                new HasSizeJavaGenerator(),
                new ImageJavaGenerator(designCompilerInformation),
                new HasTextJavaGenerator(designCompilerInformation),
                new HasStyleJavaGenerator(),
                new ThemeableLayoutJavaGenerator(),
                new HasThemeJavaGenerator(),
                new FlexComponentJavaGenerator(),
                new TextFieldJavaGenerator(designCompilerInformation),
                new HasItemsJavaGenerator(designCompilerInformation)
        ));

        propertyComponentJavaGenerators.addAll(Collections.singletonList(
                new StringPropertyJavaGenerator(designCompilerInformation)
        ));
    }

    public ArrayList<ComponentJavaGenerator> getComponentJavaGenerators() {
        return componentJavaGenerators;
    }

    public ArrayList<PropertyComponentJavaGenerator> getPropertyComponentJavaGenerators() {
        return propertyComponentJavaGenerators;
    }
}
