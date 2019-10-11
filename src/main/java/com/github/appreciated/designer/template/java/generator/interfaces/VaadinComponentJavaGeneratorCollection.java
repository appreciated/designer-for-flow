package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.appreciated.designer.service.ProjectService;

import java.util.ArrayList;
import java.util.Arrays;

public class VaadinComponentJavaGeneratorCollection extends ArrayList<VaadinComponentJavaGenerator> {

    private ProjectService service;

    public VaadinComponentJavaGeneratorCollection(ProjectService service) {
        this.service = service;
        addAll(Arrays.asList(
                new HasSizeInterfaceJavaGenerator(),
                new ImageJavaGenerator(service),
                new HasTextInterfaceJavaGenerator(),
                new HasStyleInterfaceJavaGenerator(),
                new HasThemeInterfaceJavaGenerator(),
                new FlexComponentJavaGenerator()
        ));
    }
}
