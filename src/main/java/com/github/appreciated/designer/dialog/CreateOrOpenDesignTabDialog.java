package com.github.appreciated.designer.dialog;

import com.github.appreciated.designer.file.JavaFile;
import com.github.appreciated.designer.model.project.Project;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

import static com.github.appreciated.designer.file.JavaFile.PACKAGE;
import static com.github.appreciated.designer.helper.DesignerFileHelper.isFileDesignerFile;

@Getter
public class CreateOrOpenDesignTabDialog extends FileChooserDialog {
    private static final long serialVersionUID = 211268896431029392L;

    // Layout
    protected final Button addButton;

    // Data
    protected final GridContextMenu<File> contextMenu;
    protected final GridMenuItem<File> contextMenuAdd;
    protected final GridMenuItem<File> contextMenuSelect;
    private Project project;

    public CreateOrOpenDesignTabDialog(Project project, final File parentFile, final Consumer<File> fileConsumer) {
        this(project, parentFile, fileConsumer, true);
    }

    public CreateOrOpenDesignTabDialog(Project project, final File parentFile, final Consumer<File> fileConsumer, final boolean init) {
        super(parentFile, fileConsumer, false, false);
        this.project = project;

        contextMenu = createContextMenu();

        contextMenuAdd = contextMenu.addItem(getTranslation("create.new.class"), e -> onCreateSelected());
        contextMenuSelect = contextMenu.addItem(getTranslation("open"), e -> onSelectSelected());

        addButton = new Button(getTranslation("add.new.design"), buttonClickEvent -> onCreateSelected());
        getButtons().addComponentAtIndex(0, addButton);
        getSelect().setEnabled(false);
        addButton.setEnabled(false);
        getHeader().setText(getTranslation("select.a.file.or.package"));

        if (init) { // init
            update();
        }
    }

    protected GridContextMenu<File> createContextMenu() {
        final GridContextMenu<File> contextMenu = grid.addContextMenu();

        contextMenu.addGridContextMenuOpenedListener(e -> {
            final File file = e.getItem().orElse(null);

            if (file != null) {
                if (file.isDirectory()) {
                    contextMenuAdd.setEnabled(true);
                    contextMenuSelect.setEnabled(false);
                } else {
                    contextMenuAdd.setEnabled(false);
                    contextMenuSelect.setEnabled(true);
                }
            } else {
                contextMenuAdd.setEnabled(false);
                contextMenuSelect.setEnabled(false);
            }
        });

        return contextMenu;
    }

    private void onCreateSelected() {
        final Optional<File> selectedItem = getGrid().getSelectedItems().stream().findFirst();

        if (selectedItem.isPresent()) {
            onCreate(selectedItem.get());
        } else {
            Notification.show(getTranslation("please.select.a.java.file"));
        }
    }

    private void onCreate(final File file) {
        if (file.isDirectory()) {
            JavaFile javaFile = new JavaFile();
            HashMap<String, Object> objectHashMap = new HashMap<>();
            String packageName = file.getPath().substring(parentFile.getPath().length()).replace(File.separator, ".");
            if (packageName.startsWith(".")) {
                packageName = packageName.substring(1);
            }
            objectHashMap.put(PACKAGE, packageName);
            objectHashMap.put(JavaFile.SOURCE_FILE, parentFile);
            javaFile.setPreconditions(objectHashMap);
            new PreconditionDialog(this.project, javaFile, o -> {
                getFileConsumer().accept(javaFile.create());
                close();
            }).open();
        } else {
            Notification.show(getTranslation("please.select.a.java.file"));
        }
    }

    @Override
    protected void processDoubleClickEvent(final ItemDoubleClickEvent<File> event) {
        final File selectedFile = event.getItem();

        if (selectedFile != null) {
            if (selectedFile.isDirectory()) {
                onCreate(selectedFile);
            } else {
                onSelect(selectedFile);
            }
        }
    }

    @Override
    protected void onSelect(final File file) {
        if (file != null) {
            if (FilenameUtils.getExtension(file.getName()).equals("java")) {
                getFileConsumer().accept(file);
                close();
            } else {
                Notification.show(getTranslation("please.select.a.java.file"));
            }
        } else {
            Notification.show(getTranslation("please.select.a.java.file"));
        }
    }

    protected Component getRowComponentForFile(File file) {
        HorizontalLayout layout = new HorizontalLayout();
        if (file.isDirectory()) {
            layout.add(VaadinIcon.FOLDER.create());
        } else {
            if (isFileDesignerFile(file)) {
                layout.add(VaadinIcon.VAADIN_H.create());
            } else {
                layout.add(VaadinIcon.FILE.create());
            }
        }
        layout.add(new Span(file.getName()));
        return layout;
    }

    @Override
    protected File[] getFiles(final File root) {
        if (root.listFiles() == null) {
            return null;
        } else {
            return Arrays.stream(root.listFiles())
                    .filter(file -> FilenameUtils.getExtension(file.getName()).equals("java") || file.isDirectory())
                    .toArray(File[]::new);
        }
    }

    @Override
    public void update(final File selectedFile) {
        if (selectedFile != null) {
            select.setEnabled(!selectedFile.isDirectory() && isFileDesignerFile(selectedFile));
            addButton.setEnabled(selectedFile.isDirectory());
        } else {
            select.setEnabled(false);
            addButton.setEnabled(false);
        }
    }
}