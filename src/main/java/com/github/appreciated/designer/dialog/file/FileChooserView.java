package com.github.appreciated.designer.dialog.file;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
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
import lombok.Setter;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Getter
@Setter
public class FileChooserView extends VerticalLayout {
    private static final long serialVersionUID = -8905937388281013238L;

    // Layout
    protected final HorizontalLayout buttons;
    protected final Button select;
    // Data
    protected Consumer<File> fileConsumer;
    protected TreeGrid<File> grid;

    protected File parentFile;
    protected Function<File, Component> getRowComponentForFile = file -> {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(new Span(file.getName()));
        return layout;
    };
    private AbstractBackEndHierarchicalDataProvider<File, Void> dataProvider;
    // Events
    private Function<File, Stream<File>> getFiles = file -> Arrays.stream(file.listFiles().clone());
    private Consumer onClose;
    private Consumer<File> onSelect = file -> {
        if (file != null && fileConsumer != null) {
            fileConsumer.accept(file);
            onClose.accept(null);
        } else {
            Notification.show(getTranslation("please.select.a.file"));
        }
    };
    private Consumer<ItemDoubleClickEvent<File>> processDoubleClickEvent = event -> {
        final File selectedFile = event.getItem();
        if (selectedFile != null && !selectedFile.isDirectory()) {
            onSelect.accept(selectedFile);
        }
    };
    private Consumer<File> update = selectedFile -> getSelect().setEnabled(selectedFile != null && !selectedFile.isDirectory());


    public FileChooserView(final File parentFile, final Consumer<File> fileConsumer, Consumer onClose) {
        this(parentFile, fileConsumer, true, false, onClose);
    }


    public FileChooserView(final File parentFile, final Consumer<File> fileConsumer, final boolean init, boolean allowSwitchingVolumes, Consumer onClose) {
        this.onClose = onClose;
        this.fileConsumer = fileConsumer;
        this.parentFile = parentFile;

        initGrid(parentFile);

        select = new Button(getTranslation("open"), event -> onSelectSelected());
        select.setThemeName(ButtonVariant.LUMO_PRIMARY.getVariantName());
        buttons = new HorizontalLayout(select);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setWidthFull();
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
        add(gridHeader, grid, buttons);
        setPadding(false);
        setMargin(false);
        setSizeFull();

        if (init) { // init
            update();
        }
    }

    protected void onSelectSelected() {
        onSelect.accept(getGrid().getSelectedItems().stream().findFirst().orElse(null));
    }

    private void refresh(final File parent) {
        dataProvider = new AbstractBackEndHierarchicalDataProvider<File, Void>() {
            private static final long serialVersionUID = -1759673492347200329L;

            @Override
            public int getChildCount(HierarchicalQuery<File, Void> hierarchicalQuery) {
                return (int) (hierarchicalQuery.getParent() == null ? getFiles.apply(parent).count() : getFiles.apply(hierarchicalQuery.getParent()).count());
            }

            @Override
            public boolean hasChildren(File file) {
                if (file == null) {
                    return parent.isDirectory() && getFiles.apply(parent).count() > 0;
                } else {
                    return file.isDirectory() && getFiles.apply(file) != null && getFiles.apply(file).count() > 0;
                }
            }

            @Override
            protected Stream<File> fetchChildrenFromBackEnd(HierarchicalQuery<File, Void> hierarchicalQuery) {
                return hierarchicalQuery.getParent() == null ? getFiles.apply(parent) : getFiles.apply(hierarchicalQuery.getParent());
            }
        };
        grid.setDataProvider(dataProvider);
        if (parent.isDirectory()) {
            String[] list = parent.list();
            if (list.length > 0) {
                File firstChild = new File(parent.getPath() + File.separator + list[0]);
                if (firstChild.isDirectory()) {
                    String[] firstChildList = firstChild.list();
                    if (list.length == 1 && firstChildList.length == 1) {
                        grid.expandRecursively(Stream.of(firstChild), singleDirectoriesRecursive(Arrays.asList(firstChild), -1));
                    }
                }
            }
        }
    }

    protected void update() {
        update.accept(getGrid().getSelectedItems().stream().findFirst().orElse(null));
    }

    protected void initGrid(final File parent) {
        this.grid = new TreeGrid<>();
        grid.setVerticalScrollingEnabled(true);
        refresh(parent);
        grid.addItemDoubleClickListener(event -> processDoubleClickEvent.accept(event));
        grid.addSelectionListener(e -> update.accept(e.getFirstSelectedItem().orElse(null)));
        grid.addExpandListener(event -> grid.expandRecursively(event.getItems(), singleDirectoriesRecursive(event.getItems(), -1)));
        grid.addComponentHierarchyColumn(file -> getRowComponentForFile.apply(file));
        grid.setWidthFull();
    }


    private int singleDirectoriesRecursive(final Collection<File> files, int expand) {
        if ((long) files.size() == 1 && files.stream().anyMatch(File::isDirectory)) {
            return singleDirectoriesRecursive(Arrays.asList(files.stream().findFirst().get().listFiles()), ++expand);
        }
        return Math.max(expand, 0);
    }
}
