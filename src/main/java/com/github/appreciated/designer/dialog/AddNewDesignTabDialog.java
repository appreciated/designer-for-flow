package com.github.appreciated.designer.dialog;


import com.github.appreciated.designer.file.JavaFile;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

import static com.github.appreciated.designer.file.JavaFile.PACKAGE;

public class AddNewDesignTabDialog extends FileChooserDialog {

    private final Button addButton;
    private File parent;

    public AddNewDesignTabDialog(File parent, Consumer<File> fileConsumer) {
        super(parent, fileConsumer);
        this.parent = parent;
        addButton = new Button("Add new Design", buttonClickEvent -> onCreate());
        getButtons().addComponentAtIndex(0, addButton);
        getGrid().addItemClickListener(fileItemClickEvent -> {
            if (fileItemClickEvent.getItem().isDirectory()) {
                getSelect().setEnabled(false);
                addButton.setEnabled(true);
            } else {
                getSelect().setEnabled(true);
                addButton.setEnabled(false);
            }
        });
        getSelect().setEnabled(false);
        addButton.setEnabled(false);
        getHeader().setText("Select a File or Directory");
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

    private void onCreate() {
        getGrid().getSelectedItems().stream().findFirst().ifPresent(file -> {
            if (file.isDirectory()) {
                JavaFile javaFile = new JavaFile();
                HashMap<String, Object> objectHashMap = new HashMap<>();
                objectHashMap.put(PACKAGE, file.getPath().substring(parent.getPath().length()).replace(File.separator, "."));
                objectHashMap.put(JavaFile.SOURCE_FILE, parent);
                javaFile.setPreconditions(objectHashMap);
                new PreconditionDialog(javaFile, o -> {
                    javaFile.create();
                    getFileConsumer().accept(file);
                    close();
                }).open();
            } else {
                Notification.show("Please select a Java file");
            }
        });
        if (getGrid().getSelectedItems().isEmpty()) {
            Notification.show("Please select a Java file");
        }
    }
}
