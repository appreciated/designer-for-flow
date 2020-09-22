package com.github.appreciated.designer.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import lombok.Getter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Getter
public class FileChooserDialog extends Dialog {
    private static final long serialVersionUID = -8905937388281013238L;

    // Layout
    protected final H2 header;
    protected final HorizontalLayout buttons;
    protected final Button select;
    // Data
    protected final Consumer<File> fileConsumer;
    protected TreeGrid<File> grid;
    protected File parentFile;
    private AbstractBackEndHierarchicalDataProvider<File, Void> dataProvider;

    public FileChooserDialog(final File parentFile, final Consumer<File> fileConsumer) {
        this(parentFile, fileConsumer, true, false);
    }

    public FileChooserDialog(final File parentFile, final Consumer<File> fileConsumer, final boolean init, boolean allowSwitchingVolumes) {
        super();
        setMinWidth("50%");
        setMinHeight("50%");
        this.fileConsumer = fileConsumer;
        this.parentFile = parentFile;

        initGrid(parentFile);

        select = new Button(getTranslation("select"), event -> onSelectSelected());
        select.setThemeName(ButtonVariant.LUMO_PRIMARY.getVariantName());
        buttons = new HorizontalLayout(select);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setWidthFull();
        header = new H2(getTranslation("select.a.file"));
        VerticalLayout wrapper = new VerticalLayout(header);
        HorizontalLayout gridHeader = new HorizontalLayout();
        gridHeader.setWidthFull();
        if (allowSwitchingVolumes) {
            File[] roots = File.listRoots();
            ComboBox<File> volumes = new ComboBox<>();
            volumes.setWidthFull();
            volumes.setAllowCustomValue(true);
            volumes.setItems(roots);
            volumes.setValue(Paths.get(parentFile.getPath()).getRoot().toFile());
            volumes.setItemLabelGenerator(File::getPath);
            volumes.addValueChangeListener(event -> {
                this.parentFile = event.getValue();
                refresh(this.parentFile);
            });
            gridHeader.add(volumes);
            gridHeader.add(new Button(VaadinIcon.HOME.create(), event -> {
                this.parentFile = new File(System.getProperty("user.home"));
                volumes.setValue(this.parentFile);
                refresh(this.parentFile);
            }));
            gridHeader.add(new Button(VaadinIcon.DESKTOP.create(), event -> {
                this.parentFile = new File(System.getProperty("user.home") + "/Desktop");
                volumes.setValue(this.parentFile);
                refresh(this.parentFile);
            }));
        }
        gridHeader.add(new Button(VaadinIcon.REFRESH.create(), event -> dataProvider.refreshAll()));
        wrapper.add(gridHeader, grid, buttons);
        wrapper.setPadding(false);
        wrapper.setMargin(false);
        wrapper.setSizeFull();
        setWidth("500px");
        add(wrapper);

        if (init) { // init
            update();
        }
    }

    protected void onSelectSelected() {
        onSelect(getGrid().getSelectedItems().stream().findFirst().orElse(null));
    }

    private void refresh(final File parent) {
        dataProvider = new AbstractBackEndHierarchicalDataProvider<File, Void>() {
            private static final long serialVersionUID = -1759673492347200329L;

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
        };
        grid.setDataProvider(dataProvider);
    }

    protected void update() {
        update(getGrid().getSelectedItems().stream().findFirst().orElse(null));
    }

    protected void onSelect(final File file) {
        if (file != null) {
            fileConsumer.accept(file);
            close();
        } else {
            Notification.show(getTranslation("please.select.a.file"));
        }
    }

    protected File[] getFiles(final File root) {
        return root.listFiles();
    }

    protected void update(final File selectedFile) {
        getSelect().setEnabled(selectedFile != null && selectedFile.isDirectory() && selectedFile.list() != null && Arrays.asList(selectedFile.list()).contains("pom.xml"));
    }

    protected void initGrid(final File parent) {
        this.grid = new TreeGrid<>();
        grid.setVerticalScrollingEnabled(true);
        refresh(parent);
        grid.addItemDoubleClickListener(this::processDoubleClickEvent);
        grid.addSelectionListener(e -> update(e.getFirstSelectedItem().orElse(null)));
        grid.addExpandListener(event -> grid.expandRecursively(event.getItems(), singleDirectoriesRecursive(event.getItems(), -1)));
        grid.addComponentHierarchyColumn(this::getRowComponentForFile);
        grid.setWidthFull();
    }

    protected Component getRowComponentForFile(File file) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(new Span(file.getName()));
        return layout;
    }

    private int singleDirectoriesRecursive(final Collection<File> files, int expand) {
        if ((long) files.size() == 1 && files.stream().anyMatch(File::isDirectory)) {
            return singleDirectoriesRecursive(Arrays.asList(files.stream().findFirst().get().listFiles()), ++expand);
        }
        return Math.max(expand, 0);
    }

    protected void processDoubleClickEvent(final ItemDoubleClickEvent<File> event) {
        final File selectedFile = event.getItem();

        if (selectedFile != null && !selectedFile.isDirectory()) {
            onSelect(selectedFile);
        }
    }
}
