package com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.property.component;

import com.github.appreciated.designer.application.component.IconButton;
import com.github.appreciated.designer.application.component.properties.PropertyComboBox;
import com.github.appreciated.designer.application.component.properties.PropertyTextField;
import com.github.appreciated.designer.application.dialog.file.FileChooserDialog;
import com.github.appreciated.designer.application.util.FileStreamResource;
import com.github.appreciated.designer.application.util.PathValidator;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.AbstractComponentPropertyRenderer;
import com.github.appreciated.designer.application.view.file.designer.sidebar.renderer.RenderPair;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.Binder;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class ImageRenderer extends AbstractComponentPropertyRenderer<Image> {
    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof Image;
    }

    @Override
    public Stream<RenderPair> render(Image component) {
        PropertyTextField src = new PropertyTextField();
        src.setSuffixComponent(new IconButton(VaadinIcon.FOLDER.create(), buttonClickEvent -> {
            new FileChooserDialog(getProjectFileModel().getInformation().getProject().getFrontendFolder(),
                    file -> src.setValue("." + file.getPath().substring(getProjectFileModel().getInformation().getProject().getFrontendFolder().getPath().length()).replace("\\", "/")))
                    .open();
        }));
        src.setErrorMessage("This source does not exist");
        Binder<ImageSource> sourceBinder = new Binder<>();
        ImageSource is = new ImageSource();
        if (component.getSrc() != null && !component.getSrc().startsWith("VAADIN/dynamic")) {
            is.setSrc(component.getSrc());
            src.setValue(component.getSrc());
            setSrc(component, src);
        } else if (
                getProjectFileModel().getInformation().hasCompilationMetaInformation(component) &&
                        getProjectFileModel().getInformation().getCompilationMetaInformation(component).hasPropertyReplacement("src")) {
            is.setSrc((String) getProjectFileModel().getInformation().getCompilationMetaInformation(component).getPropertyReplacement("src"));
        }
        sourceBinder.setBean(is);
        src.addBlurListener(textFieldBlurEvent -> sourceBinder.validate());
        sourceBinder.forField(src)
                .withValidator(new PathValidator(getProjectFileModel()))
                .bind(ImageSource::getSrc, ImageSource::setSrc);

        sourceBinder.addStatusChangeListener(event -> {
            if (!event.hasValidationErrors()) {
                setSrc(component, src);
            }
        });

        PropertyComboBox<String> objectFitComponent = new PropertyComboBox<>();
        objectFitComponent.setItems(Arrays.asList("fill", "contain", "cover", "none", "scale-down"));
        setValueButNull(objectFitComponent, "fill");
        objectFitComponent.addValueChangeListener(event -> component.getStyle().set("object-fit", event.getValue()));
        return Stream.of(new RenderPair("src", src), new RenderPair("object-fit", objectFitComponent));
    }

    public void setSrc(Image component, PropertyTextField src) {
        if (component != null) {
            if (src.getValue() != null && (src.getValue() != null && !src.getValue().equals(""))) {
                component.setSrc(new FileStreamResource(new File(getProjectFileModel().getInformation().getProject().getFrontendFolder() + File.separator + src.getValue().replace("/", File.separator))));
                getProjectFileModel().getInformation().getOrCreateCompilationMetaInformation(component).setPropertyReplacement("src", src.getValue());
            } else {
                component.getElement().setAttribute("src", "");
                getProjectFileModel().getInformation().getOrCreateCompilationMetaInformation(component).removePropertyReplacement("src");
            }
        }
    }

    @Override
    public Stream<String> rendersCssStyle() {
        return Stream.of("object-fit");
    }

    @Override
    public Stream<String> rendersProperty() {
        return Stream.of("src");
    }


    class ImageSource {
        String src;

        public ImageSource(String src) {
            this.src = src;
        }

        public ImageSource() {
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }
}