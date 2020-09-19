package com.github.appreciated.designer.dialog;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

public class OpenProjectDialog extends FileChooserDialog {

    public OpenProjectDialog(Consumer<File> fileConsumer) {
        super(Arrays.stream(File.listRoots()).findFirst().get(), fileConsumer, true, true);
        getGrid().setPageSize(100);
        getHeader().setText(getTranslation("select.a.project.folder"));
    }

    @Override
    protected File[] getFiles(File root) {
        if (root.listFiles() == null) {
            return null;
        } else {
            return Arrays.stream(root.listFiles())
                    .filter(File::isDirectory)
                    .toArray(File[]::new);
        }
    }

    protected Component getRowComponentForFile(File file) {
        HorizontalLayout layout = new HorizontalLayout();
        if (file.isDirectory() && file.list() != null && Arrays.asList(file.list()).contains("pom.xml")) {
            layout.add(VaadinIcon.VAADIN_H.create());
        } else if (file.isDirectory()) {
            layout.add(VaadinIcon.FOLDER.create());
        } else {
            layout.add(VaadinIcon.FILE.create());
        }
        layout.add(new Span(file.getName()));
        return layout;
    }

    protected void update(final File selectedFile) {
        getSelect().setEnabled(selectedFile != null && selectedFile.isDirectory() && selectedFile.list() != null && Arrays.asList(selectedFile.list()).contains("pom.xml"));
    }
}
