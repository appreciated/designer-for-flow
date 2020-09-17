package com.github.appreciated.designer.dialog;

import static com.github.appreciated.designer.file.JavaFile.PACKAGE;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.io.FilenameUtils;

import com.github.appreciated.designer.file.JavaFile;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.notification.Notification;

import lombok.Getter;

@Getter
public class AddNewDesignTabDialog extends FileChooserDialog {
	private static final long serialVersionUID = 211268896431029392L;

	// Layout
	protected final Button addButton;
	
    // Data
    protected final GridContextMenu<File> contextMenu;
    protected final GridMenuItem<File> contextMenuAdd;
    protected final GridMenuItem<File> contextMenuSelect;

    public AddNewDesignTabDialog(final File parentFile, final Consumer<File> fileConsumer) {
    	this(parentFile, fileConsumer, true);
    }
    
    public AddNewDesignTabDialog(final File parentFile, final Consumer<File> fileConsumer, final boolean init) {
        super(parentFile, fileConsumer, false);
        
        contextMenu = createContextMenu();
        
        contextMenuAdd = contextMenu.addItem("Create new class", e -> onCreateSelected());
        contextMenuSelect = contextMenu.addItem("Select", e -> onSelectSelected());
        
        addButton = new Button("Add new Design", buttonClickEvent -> onCreateSelected());
        getButtons().addComponentAtIndex(0, addButton);
        getSelect().setEnabled(false);
        addButton.setEnabled(false);
        getHeader().setText("Select a File or Package");
        
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
            Notification.show("Please select a Java file");
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
            new PreconditionDialog(javaFile, o -> {
                getFileConsumer().accept(javaFile.create());
                close();
            }).open();
        } else {
            Notification.show("Please select a Java file");
        }
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
    protected void onSelect(final File file) {
    	if (file != null) {
    		if (FilenameUtils.getExtension(file.getName()).equals("java")) {
    			getFileConsumer().accept(file);
    			close();
    		} else {
    			Notification.show("Please select a Java file");
    		}
    	} else {
            Notification.show("Please select a Java file");
    	}
    }
    
    @Override
    public void update(final File selectedFile) {
    	if (selectedFile != null) {
            select.setEnabled(!selectedFile.isDirectory());
            addButton.setEnabled(!getSelect().isEnabled());
    	} else {
    		select.setEnabled(false);
            addButton.setEnabled(false);
    	}
    }
}