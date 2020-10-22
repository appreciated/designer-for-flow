package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component.ComponentRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component.DateTimePickerRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component.ImageRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component.TimePickerRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.interfaces.*;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.type.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Renderers {

    private final List<AbstractPropertyRenderer> typeRenderer = new ArrayList<>();
    private final List<AbstractComponentPropertyRenderer> propertyRenderer = new ArrayList<>();

    public Renderers(ProjectFileModel projectFileModel) {
        typeRenderer.addAll(Arrays.asList(
                new StringPropertyRenderer(),
                new BooleanPropertyRenderer(),
                new IntegerPropertyRenderer(),
                new DoublePropertyRenderer(),
                new IconPropertyRenderer(),
                new LocalDateTimePropertyRenderer(),
                new DurationPropertyRenderer()
        ));
        propertyRenderer.addAll(Arrays.asList(
                new FlexComponentRenderer(),
                new DateTimePickerRenderer(),
                new TimePickerRenderer(),
                new FlexComponentRenderer(),
                new HasThemeRenderer(),
                new HasItemsRenderer(),
                new HasSizeRenderer(),
                new HasTextRenderer(),
                new ThemeableLayoutRenderer()
        ));
        propertyRenderer.addAll(Arrays.asList(
                new ComponentRenderer(),
                new ImageRenderer()
        ));
        typeRenderer.forEach(abstractPropertyRenderer -> abstractPropertyRenderer.setProjectFileModel(projectFileModel));
        propertyRenderer.forEach(abstractPropertyRenderer -> abstractPropertyRenderer.setProjectFileModel(projectFileModel));
    }

    public List<AbstractPropertyRenderer> getTypeRenderers() {
        return typeRenderer;
    }

    public List<AbstractComponentPropertyRenderer> getPropertyRenderers() {
        return propertyRenderer;
    }
}
