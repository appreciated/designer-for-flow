package com.github.appreciated.designer.application.view;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.Command;

/**
 * sdfgsdfg
 */
public class BaseView extends VerticalLayout {
	private static final long serialVersionUID = 2698584072054013941L;

	public BaseView() {
    	this(null);
    }
	
    public BaseView(String i18nName) {
        super();

        if (i18nName != null) {
            add(new H3(getTranslation(i18nName)));
        }

        setSizeFull();
        setMargin(false);
        setPadding(true);
    }
    
    public void uiAccess(final Command command) {
    	getUI().ifPresent(ui -> ui.access(command));
    }
}