package com.github.appreciated.designer.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileChooserDialog extends Dialog {

    private final TreeGrid<File> grid;
    private final HorizontalLayout buttons;
    private final H2 header;
    private final Button select;
    private Consumer<File> fileConsumer;

    public FileChooserDialog(File parent, Consumer<File> fileConsumer) {
        this.fileConsumer = fileConsumer;
        grid = new TreeGrid<>();

        grid.setDataProvider(new AbstractBackEndHierarchicalDataProvider<File, Void>() {
            @Override
            public int getChildCount(HierarchicalQuery<File, Void> hierarchicalQuery) {
                return hierarchicalQuery.getParent() == null ? getFiles(parent).length : getFiles(hierarchicalQuery.getParent()).length;
            }

            @Override
            public boolean hasChildren(File file) {
                if (file == null) {
                    return parent.isDirectory() && getFiles(parent).length > 0;
                } else {
                    return file.isDirectory() && getFiles(file) != null && getFiles(file).length > 0;
                }
            }

            @Override
            protected Stream<File> fetchChildrenFromBackEnd(HierarchicalQuery<File, Void> hierarchicalQuery) {
                return Arrays.stream(hierarchicalQuery.getParent() == null ? getFiles(parent) : getFiles(hierarchicalQuery.getParent()));
            }
        });

        grid.addExpandListener(event -> grid.expandRecursively(event.getItems(), recursiveDirectory(event.getItems(), 1)));

        grid.addHierarchyColumn(File::getName).setHeader("Name");
        grid.setWidthFull();
        select = new Button("Select", event -> onSelect());
        select.setThemeName(ButtonVariant.LUMO_PRIMARY.getVariantName());
        buttons = new HorizontalLayout(select);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setWidthFull();
        header = new H2("Select a File");
        VerticalLayout wrapper = new VerticalLayout(header, grid, buttons);
        wrapper.setMargin(false);
        wrapper.setPadding(false);
        wrapper.setSizeFull();
        setWidth("500px");
        add(wrapper);
    }

    protected File[] getFiles(File root) {
        if (root.listFiles() == null) {
            return null;
        } else {
            return Arrays.stream(root.listFiles())
                    .toArray(File[]::new);
        }
    }

    int recursiveDirectory(Collection<File> files, int expand) {
        if (files.stream().count() == 1 && files.stream().anyMatch(File::isDirectory)) {
            return recursiveDirectory(Arrays.asList(files.stream().findFirst().get().listFiles()), ++expand);
        }
        return expand;
    }

    protected void onSelect() {
        grid.getSelectedItems().stream().findFirst().ifPresent(file -> {
            fileConsumer.accept(file);
            close();
        });
        if (grid.getSelectedItems().isEmpty()) {
            Notification.show("Please select a file");
        }
    }

    public Consumer<File> getFileConsumer() {
        return fileConsumer;
    }

    public TreeGrid<File> getGrid() {
        return grid;
    }

    public HorizontalLayout getButtons() {
        return buttons;
    }

    public Button getSelect() {
        return select;
    }

    public H2 getHeader() {
        return header;
    }
}
