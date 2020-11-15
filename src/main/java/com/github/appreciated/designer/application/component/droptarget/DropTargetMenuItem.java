package com.github.appreciated.designer.application.component.droptarget;

import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.function.SerializableRunnable;

public class DropTargetMenuItem extends MenuItem implements DropTargetComponent {
    public DropTargetMenuItem(ContextMenu contextMenu, SerializableRunnable contentReset) {
        super(contextMenu, contentReset);
    }
}
