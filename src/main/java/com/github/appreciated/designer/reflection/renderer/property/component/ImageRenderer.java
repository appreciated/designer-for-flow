package com.github.appreciated.designer.reflection.renderer.property.component;

import com.github.appreciated.designer.component.IconButton;
import com.github.appreciated.designer.dialog.FileChooserDialog;
import com.github.appreciated.designer.reflection.renderer.AbstractPropertyRenderer;
import com.github.appreciated.designer.reflection.renderer.RenderPair;
import com.github.appreciated.designer.vaadin.converter.FileStreamResource;
import com.github.appreciated.designer.vaadin.converter.PathValidator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class ImageRenderer extends AbstractPropertyRenderer<Image> {
    @Override
    public boolean canRender(Component propertyParent) {
        return propertyParent instanceof Image;
    }

    @Override
    public Stream<RenderPair> render(Image component) {
        TextField src = new TextField();
        src.setSuffixComponent(new IconButton(VaadinIcon.FOLDER.create(), buttonClickEvent -> {
            new FileChooserDialog(getService().getProject().getFrontendFolder(),
                    file -> src.setValue("." + file.getPath().substring(getService().getProject().getFrontendFolder().getPath().length()).replace("\\", "/")))
                    .open();
        }));
        src.setErrorMessage("This source does not exist");
        Binder<ImageSource> sourceBinder = new Binder<>();
        ImageSource is = new ImageSource();
        if (component.getSrc() != null && !component.getSrc().startsWith("VAADIN/dynamic")) {
            is.setSrc(component.getSrc());
            src.setValue(component.getSrc());
            setSrc(component, src);
        } else if (getService().getCurrentFile().getComponentMetainfo(component).hasPropertyReplacement("src")) {
            is.setSrc((String) getService().getCurrentFile().getComponentMetainfo(component).getPropertyReplacement("src"));
        }
        sourceBinder.setBean(is);
        src.addBlurListener(textFieldBlurEvent -> sourceBinder.validate());
        sourceBinder.forField(src)
                .withValidator(new PathValidator(getService()))
                .bind(ImageSource::getSrc, ImageSource::setSrc);

        sourceBinder.addStatusChangeListener(event -> {
            if (!event.hasValidationErrors()) {
                setSrc(component, src);
            }
        });

        ComboBox<String> objectFitComponent = new ComboBox<>();
        objectFitComponent.setItems(Arrays.asList("fill", "contain", "cover", "none", "scale-down"));
        setValueButNull(objectFitComponent, "fill");
        objectFitComponent.addValueChangeListener(event -> component.getStyle().set("object-fit", event.getValue()));

        return Stream.of(new RenderPair("src", src), new RenderPair("object-fit", objectFitComponent));
    }

    public void setSrc(Image component, TextField src) {
        if (src.getValue() != null) {
            component.setSrc(new FileStreamResource(new File(getService().getProject().getFrontendFolder() + File.separator + src.getValue().replace("/", File.separator))));
            getService().getCurrentFile().getComponentMetainfo(component).setPropertyReplacement("src", src.getValue());
        } else {
            component.getElement().setAttribute("src", "");
            getService().getCurrentFile().getComponentMetainfo(component).setPropertyReplacement("src", null);
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

    @Override
    public void applyValue(Image propertyParent) {

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