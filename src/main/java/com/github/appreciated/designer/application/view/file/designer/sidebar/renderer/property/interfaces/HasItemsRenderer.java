package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces;

import com.github.appreciated.designer.application.component.designer.StringItemsEditor;
import com.github.appreciated.designer.application.model.CompilationMetaInformation;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.HasItems;

import java.util.List;
import java.util.stream.Stream;

public class HasItemsRenderer extends AbstractComponentPropertyRenderer<HasItems> {

    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof HasItems;
    }

    @Override
    public Stream<RenderPair> render(HasItems propertyParent) {

        boolean hasMetaInformation = getProjectFileModel().getInformation().hasCompilationMetaInformation((Component) propertyParent);
        CompilationMetaInformation metaInformation = getProjectFileModel().getInformation().getCompilationMetaInformation((Component) propertyParent);

        return Stream.of(
                new RenderPair("items",
                        new StringItemsEditor("items",
                                (hasMetaInformation ? ((List<String>) metaInformation.getPropertyReplacement("items")).stream() : Stream.empty()),
                                collection -> {
                                    propertyParent.setItems(collection);
                                    getProjectFileModel().getInformation().getOrCreateCompilationMetaInformation((Component) propertyParent).setPropertyReplacement("items", collection);
                                }
                        )
                )
        );
    }


}
