package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.appreciated.designer.model.DesignCompilerInformation;

import java.util.ArrayList;
import java.util.Arrays;

public class VaadinComponentJavaGeneratorCollection extends ArrayList<VaadinComponentJavaGenerator> {

    public VaadinComponentJavaGeneratorCollection(DesignCompilerInformation designCompilerInformation) {
        addAll(Arrays.asList(
                new HasSizeInterfaceJavaGenerator(),
                new ImageJavaGenerator(designCompilerInformation),
                new HasTextInterfaceJavaGenerator(),
                new HasStyleInterfaceJavaGenerator(),
                new HasThemeInterfaceJavaGenerator(),
                new FlexComponentJavaGenerator()
        ));
    }
}
