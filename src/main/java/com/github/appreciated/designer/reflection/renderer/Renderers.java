package com.github.appreciated.designer.reflection.renderer;

import com.github.appreciated.designer.reflection.renderer.property.component.ComponentRenderer;
import com.github.appreciated.designer.reflection.renderer.property.component.ImageRenderer;
import com.github.appreciated.designer.reflection.renderer.property.interfaces.FlexComponentInterfaceRenderer;
import com.github.appreciated.designer.reflection.renderer.property.interfaces.HasSizeInterfaceRenderer;
import com.github.appreciated.designer.reflection.renderer.property.interfaces.HasTextInterfaceRenderer;
import com.github.appreciated.designer.reflection.renderer.property.interfaces.HasThemeInterfaceRenderer;
import com.github.appreciated.designer.reflection.renderer.type.*;
import com.github.appreciated.designer.service.ProjectService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Renderers {

    private List<com.github.appreciated.designer.reflection.renderer.type.AbstractPropertyRenderer> typeRenderer = new ArrayList<>();
    private List<AbstractPropertyRenderer> propertyRenderer = new ArrayList<>();

    public Renderers(ProjectService service) {
        typeRenderer.addAll(Arrays.asList(
                new StringPropertyRenderer(),
                new BooleanPropertyRenderer(),
                new IntegerPropertyRenderer(),
                new DoublePropertyRenderer(),
                new IconPropertyRenderer()
        ));
        propertyRenderer.addAll(Arrays.asList(
                new HasThemeInterfaceRenderer(),
                new HasSizeInterfaceRenderer(),
                new HasTextInterfaceRenderer(),
                new FlexComponentInterfaceRenderer(),
                new ComponentRenderer(),
                new ImageRenderer()
        ));
        typeRenderer.forEach(abstractPropertyRenderer -> abstractPropertyRenderer.setProjectService(service));
        propertyRenderer.forEach(abstractPropertyRenderer -> abstractPropertyRenderer.setProjectService(service));
    }

    public List<com.github.appreciated.designer.reflection.renderer.type.AbstractPropertyRenderer> getTypeRenderers() {
        return typeRenderer;
    }

    public List<AbstractPropertyRenderer> getPropertyRenderers() {
        return propertyRenderer;
    }

}
