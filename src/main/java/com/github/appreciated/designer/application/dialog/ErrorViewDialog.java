package com.github.appreciated.designer.application.dialog;

import com.github.appreciated.designer.application.service.ExceptionService;
import com.github.appreciated.designer.application.view.ErrorView;
import com.vaadin.flow.component.dialog.Dialog;

public class ErrorViewDialog extends Dialog {
    public ErrorViewDialog(ExceptionService exceptionService) {
        add(new ErrorView(exceptionService));
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("330px");
    }
}
