package com.github.appreciated.designer.component.droptarget;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.function.SerializableRunnable;

public class DropTargetSubMenu extends SubMenu {
    public DropTargetSubMenu(MenuItem parentMenuItem, SerializableRunnable contentReset) {
        super(parentMenuItem, contentReset);
    }
}
