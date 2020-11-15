package com.github.appreciated.designer.application.dialog.file;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

public class MavenProjectChooserView extends FileChooserView {
    public MavenProjectChooserView(final File parentFile, final Consumer<File> fileConsumer, boolean init, boolean allowSwitchingVolumes, Consumer onClose) {
        super(parentFile, fileConsumer, init, allowSwitchingVolumes, onClose);
        getGrid().setPageSize(100);

        setGetFiles(root -> {
            if (root.listFiles() == null) {
                return null;
            } else {
                return Arrays.stream(root.listFiles()).filter(File::isDirectory);
            }
        });
        setUpdate(selectedFile -> {
            getSelectFileButton().setEnabled(selectedFile != null && selectedFile.isDirectory() && selectedFile.list() != null && Arrays.asList(selectedFile.list()).contains("pom.xml"));
        });
        setGetRowComponentForFile(file -> {
            HorizontalLayout layout = new HorizontalLayout();
            layout.getStyle().set("user-select", "none");
            if (isVaadinProject(file)) {
                layout.add(VaadinIcon.VAADIN_H.create());
            } else if (file.isDirectory()) {
                layout.add(VaadinIcon.FOLDER.create());
            } else {
                layout.add(VaadinIcon.FILE.create());
            }
            layout.add(new Span(file.getName()));
            return layout;
        });
        setProcessDoubleClickEvent(fileItemDoubleClickEvent -> {
            File file = fileItemDoubleClickEvent.getItem();
            if (isVaadinProject(file)) {
                getOnSelect().accept(file);
            } else if (file.isDirectory()) {
                getGrid().expand(file);
            }
        });
    }

    boolean isVaadinProject(File file) {
        return file.isDirectory() && file.list() != null && Arrays.asList(file.list()).contains("pom.xml");
    }
}
