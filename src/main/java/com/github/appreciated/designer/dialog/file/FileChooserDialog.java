package com.github.appreciated.designer.dialog.file;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import lombok.Getter;

import java.io.File;
import java.util.function.Consumer;

@Getter
public class FileChooserDialog extends Dialog {
    private static final long serialVersionUID = -8905937388281013238L;
    private final FileChooserView view;
    private final H2 header;


    public FileChooserDialog(final File parentFile, final Consumer<File> fileConsumer) {
        this(parentFile, fileConsumer, true, false);
    }

    public FileChooserDialog(final File parentFile, final Consumer<File> fileConsumer, final boolean init, boolean allowSwitchingVolumes) {
        super();
        setMinWidth("50%");
        setMinHeight("50%");
        setWidth("500px");
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setMargin(false);
        wrapper.setPadding(false);
        view = new FileChooserView(parentFile, fileConsumer, init, allowSwitchingVolumes, o -> close());
        header = new H2(getTranslation("select.a.file"));
        wrapper.add(header, view);
        add(view);
    }

    protected void onSelectSelected() {
        view.onSelectSelected();
    }

    protected HorizontalLayout getButtons() {
        return view.getButtons();
    }

    protected Button getSelect() {
        return view.getSelect();
    }

    protected TreeGrid<File> getGrid() {
        return view.getGrid();
    }

    protected File getParentFile() {
        return view.getParentFile();
    }

}
