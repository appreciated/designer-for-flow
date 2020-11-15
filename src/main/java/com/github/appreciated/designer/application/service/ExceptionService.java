package com.github.appreciated.designer.application.service;

import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class ExceptionService {
    private Throwable throwable;

    public Throwable getError() {
        return throwable;
    }

    public void setError(Throwable throwable) {
        this.throwable = throwable;
    }
}
