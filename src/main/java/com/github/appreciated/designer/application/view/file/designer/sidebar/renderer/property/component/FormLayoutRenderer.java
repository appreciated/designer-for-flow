package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component;

import com.github.appreciated.designer.application.component.designer.ResponsiveStepsItemsEditor;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

import java.util.stream.Stream;

public class FormLayoutRenderer extends AbstractComponentPropertyRenderer<FormLayout> {
    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof FormLayout;
    }

    @Override
    public Stream<RenderPair> render(FormLayout formLayout) {
        return Stream.of(new RenderPair("items",
                new ResponsiveStepsItemsEditor("responsiveSteps",
                        formLayout.getResponsiveSteps().stream(),
                        collection -> {
                            formLayout.setResponsiveSteps(collection);
                            getProjectFileModel()
                                    .getInformation()
                                    .getOrCreateCompilationMetaInformation(formLayout)
                                    .setPropertyReplacement("responsiveSteps", collection);
                        }
                )
        ));
    }

    @Override
    public Stream<String> rendersProperty() {
        return Stream.of("src");
    }

}

