package com.github.appreciated.designer.dialog;


import com.vaadin.flow.component.notification.Notification;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

public class OpenProjectDialog extends FileChooserDialog {

    public OpenProjectDialog(Consumer<File> fileConsumer) {
        super(new File(System.getProperty("user.home")), fileConsumer);
        getGrid().setPageSize(100);
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

    protected void onSelect() {
        getGrid().getSelectedItems().stream().findFirst().ifPresent(file -> {
            if (file.isDirectory()) {
                close();
                getFileConsumer().accept(file);
            } else {
                Notification.show("Please select a Folder");
            }
        });
        if (getGrid().getSelectedItems().isEmpty()) {
            Notification.show("Please select a Folder");
        }
    }
}
