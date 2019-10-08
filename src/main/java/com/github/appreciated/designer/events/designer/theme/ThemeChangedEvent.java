package com.github.appreciated.designer.events.designer.theme;

import com.github.appreciated.designer.model.CssVariable;
import org.springframework.context.ApplicationEvent;

public class ThemeChangedEvent extends ApplicationEvent {

    private CssVariable cssVariable;

    public ThemeChangedEvent(Object source, CssVariable cssVariable) {
        super(source);
        this.cssVariable = cssVariable;
    }

    public CssVariable getCssVariable() {
        return cssVariable;
    }
}
