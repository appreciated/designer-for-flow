package com.github.appreciated.designer.dialog;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;

import lombok.Getter;

@Getter
public class FileChooserDialog extends Dialog {
	private static final long serialVersionUID = -8905937388281013238L;

	// Layout
    protected final H2 header;
	protected final TreeGrid<File> grid;
	protected final HorizontalLayout buttons;
    protected final Button select;
    
    // Data
    protected final Consumer<File> fileConsumer;
    protected final File parentFile;

    public FileChooserDialog(final File parentFile, final Consumer<File> fileConsumer) {
    	this(parentFile, fileConsumer, true);
    }
    
    public FileChooserDialog(final File parentFile, final Consumer<File> fileConsumer, final boolean init) {
    	super();
    	
        this.fileConsumer = fileConsumer;
        this.parentFile = parentFile;
        
        grid = createGrid(parentFile);
        
        select = new Button("Select", event -> onSelectSelected());
        select.setThemeName(ButtonVariant.LUMO_PRIMARY.getVariantName());
        buttons = new HorizontalLayout(select);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setWidthFull();
        header = new H2("Select a File");
        VerticalLayout wrapper = new VerticalLayout(header, grid, buttons);
        wrapper.setPadding(false);
        wrapper.setMargin(false);
        wrapper.setSizeFull();
        setWidth("500px");
        add(wrapper);
        
        if (init) { // init
        	update();
        }
    }
    
    protected TreeGrid<File> createGrid(final File parent) {
    	TreeGrid<File> grid = new TreeGrid<File>();
        grid.setDataProvider(new AbstractBackEndHierarchicalDataProvider<File, Void>() {
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
        });
        grid.addItemDoubleClickListener(this::processDoubleClickEvent);
        grid.addSelectionListener(e -> update(e.getFirstSelectedItem().orElse(null)));
        grid.addExpandListener(event -> grid.expandRecursively(event.getItems(), singleDirectoriesRecursive(event.getItems(), -1)));
        grid.addHierarchyColumn(File::getName);
        grid.setWidthFull();
        
        return grid;
    }

    protected File[] getFiles(final File root) {
        return root.listFiles();
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
    
    protected void onSelectSelected() {
    	onSelect(getGrid().getSelectedItems().stream().findFirst().orElse(null));
    }

    protected void onSelect(final File file) {
    	if (file != null) {
    		fileConsumer.accept(file);
            close();
    	} else {
            Notification.show("Please select a file");
    	}
    }
    
    protected void update() {
    	update(getGrid().getSelectedItems().stream().findFirst().orElse(null));
    }
    
    protected void update(final File selectedFile) {
    	getSelect().setEnabled((selectedFile != null));
    }
}
