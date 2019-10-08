package com.github.appreciated.designer.template.java.compiler.interfaces;

import com.github.appreciated.designer.service.ProjectService;

import java.util.ArrayList;
import java.util.Arrays;

public class VaadinInterfaceCompilerCollection extends ArrayList<VaadinComponentCompiler> {

    private ProjectService service;

    public VaadinInterfaceCompilerCollection(ProjectService service) {
        this.service = service;
        addAll(Arrays.asList(
                new HasSizeInterfaceCompiler(),
                new ImageCompiler(service),
                new HasTextInterfaceCompiler(),
                new HasStyleInterfaceCompiler(),
                new HasThemeInterfaceCompiler(),
                new FlexComponentCompiler()
        ));
    }
}
