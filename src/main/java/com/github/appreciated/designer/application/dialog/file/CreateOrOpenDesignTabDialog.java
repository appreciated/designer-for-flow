package com.github.appreciated.designer.application.dialog.file;

import com.github.appreciated.designer.application.dialog.PreconditionDialog;
import com.github.appreciated.designer.application.dialog.file.preconditions.JavaFile;
import com.github.appreciated.designer.application.model.project.Project;
import com.vaadin.flow.component.button.Button;
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
import java.util.stream.Stream;

import static com.github.appreciated.designer.application.dialog.file.preconditions.JavaFile.PACKAGE;
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
    private final Project project;

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
            getView().update();
        }

        getView().setGetFiles(root -> {
            if (root.listFiles() == null) {
                return Stream.empty();
            } else {
                return Arrays.stream(root.listFiles())
                        .filter(file -> FilenameUtils.getExtension(file.getName()).equals("java") || file.isDirectory());
            }
        });
        getView().setUpdate(selectedFile -> {
            if (selectedFile != null) {
                getSelect().setEnabled(!selectedFile.isDirectory() && isFileDesignerFile(selectedFile));
                addButton.setEnabled(selectedFile.isDirectory());
            } else {
                getSelect().setEnabled(false);
                addButton.setEnabled(false);
            }
        });
        getView().setOnSelect(file -> {
            if (file != null) {
                if (FilenameUtils.getExtension(file.getName()).equals("java")) {
                    if (isFileDesignerFile(file)) {
                        getView().getFileConsumer().accept(file);
                        close();
                    } else {
                        Notification.show(getTranslation("only.java.files.created.with.the.designer.can.be.opened"));
                    }
                } else {
                    Notification.show(getTranslation("please.select.a.java.file"));
                }
            } else {
                Notification.show(getTranslation("please.select.a.java.file"));
            }
        });
        getView().setProcessDoubleClickEvent(event -> {
            final File selectedFile = event.getItem();

            if (selectedFile != null) {
                if (selectedFile.isDirectory()) {
                    onCreate(selectedFile);
                } else {
                    getView().getOnSelect().accept(selectedFile);
                }
            }
        });
        getView().setGetRowComponentForFile(file -> {
            HorizontalLayout layout = new HorizontalLayout();
            layout.getStyle().set("user-select", "none");
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
        });
    }

    private GridContextMenu<File> createContextMenu() {
        final GridContextMenu<File> contextMenu = getGrid().addContextMenu();

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
            String packageName = file.getPath().substring(getParentFile().getPath().length()).replace(File.separator, ".");
            if (packageName.startsWith(".")) {
                packageName = packageName.substring(1);
            }
            objectHashMap.put(PACKAGE, packageName);
            objectHashMap.put(JavaFile.SOURCE_FILE, getParentFile());
            javaFile.setPreconditions(objectHashMap);
            new PreconditionDialog(this.project, javaFile, o -> {
                getView().getFileConsumer().accept(javaFile.create());
                close();
            }).open();
        } else {
            Notification.show(getTranslation("please.select.a.java.file"));
        }
    }
}