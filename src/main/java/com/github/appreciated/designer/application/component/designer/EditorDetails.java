package com.github.appreciated.designer.application.component.designer;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;

public class EditorDetails extends Details implements HasSize {
    public EditorDetails(String summary) {
        setSummaryText(summary);
        addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
    }
}
