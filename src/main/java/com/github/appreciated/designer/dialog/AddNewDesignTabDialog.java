package com.github.appreciated.designer.dialog;


import com.vaadin.flow.component.notification.Notification;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

public class AddNewDesignTabDialog extends FileChooserDialog {

    public AddNewDesignTabDialog(File parent, Consumer<File> fileConsumer) {
        super(parent, fileConsumer);
    }

    protected File[] getFiles(File root) {
        if (root.listFiles() == null) {
            return null;
        } else {
            return Arrays.stream(root.listFiles())
                    .filter(file -> FilenameUtils.getExtension(file.getName()).equals("java") || file.isDirectory())
                    .toArray(File[]::new);
        }
    }

    protected void onSelect() {
        getGrid().getSelectedItems().stream().findFirst().ifPresent(file -> {
            if (FilenameUtils.getExtension(file.getName()).equals("java")) {
                getFileConsumer().accept(file);
                close();
            } else {
                Notification.show("Please select a Java file");
            }
        });
        if (getGrid().getSelectedItems().isEmpty()) {
            Notification.show("Please select a Java file");
        }
    }

}
