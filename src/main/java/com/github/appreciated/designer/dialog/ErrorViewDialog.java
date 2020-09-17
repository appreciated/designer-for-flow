package com.github.appreciated.designer.dialog;

import com.github.appreciated.designer.application.view.ErrorView;
import com.github.appreciated.designer.service.ExceptionService;
import com.vaadin.flow.component.dialog.Dialog;

public class ErrorViewDialog extends Dialog {
    public ErrorViewDialog(ExceptionService exceptionService) {
        add(new ErrorView(exceptionService));
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
    }
}
