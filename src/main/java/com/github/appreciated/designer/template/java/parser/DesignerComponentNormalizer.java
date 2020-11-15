package com.github.appreciated.designer.template.java.parser;

import com.github.appreciated.designer.application.util.FileStreamResource;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;

import java.io.File;

import static com.github.appreciated.designer.helper.ComponentContainerHelper.getChildren;
import static com.github.appreciated.designer.helper.ComponentContainerHelper.isComponentContainer;

public class DesignerComponentNormalizer {
    private final DesignCompilerInformation compilerInformation;

    public DesignerComponentNormalizer(Component parsedComponent, DesignCompilerInformation compilerInformation) {
        this.compilerInformation = compilerInformation;
        iterate(parsedComponent);
    }

    public static void normalize(Component parsedComponent, DesignCompilerInformation compilerInformation) {
        new DesignerComponentNormalizer(parsedComponent, compilerInformation);
    }

    private void iterate(Component component) {
        processComponent(component);
        if (isComponentContainer(component, compilerInformation)) {
            getChildren(component).forEach(this::iterate);
        }
    }

    private void processComponent(Component component) {
        if (component instanceof Image) {
            if (((Image) component).getSrc() != null && !((Image) component).getSrc().equals("")) {
                String src = ((Image) component).getSrc();
                ((Image) component).setSrc(new FileStreamResource(new File(getCompilerInformation().getProject().getFrontendFolder() + File.separator + src.replace("/", File.separator))));
                getCompilerInformation().getOrCreateCompilationMetaInformation(component).setPropertyReplacement("src", src);
            }
        }
    }

    public DesignCompilerInformation getCompilerInformation() {
        return compilerInformation;
    }
}
