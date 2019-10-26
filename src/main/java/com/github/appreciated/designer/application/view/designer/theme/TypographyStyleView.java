package com.github.appreciated.designer.application.view.designer.theme;

import com.github.appreciated.designer.component.designer.TextFieldEditorDetails;
import com.github.appreciated.designer.model.LumoVariables;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.theme.css.Theme;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TypographyStyleView extends VerticalLayout {
    public TypographyStyleView(ProjectService projectService, EventService eventService) {
        Theme theme = projectService.getCurrentFile().getTheme();

        TextFieldEditorDetails fontSize = new TextFieldEditorDetails("Font Size", eventService, false);
        fontSize.withFormField(LumoVariables.FONT_SIZE_XXXL, theme)
                .withFormField(LumoVariables.FONT_SIZE_XXL, theme)
                .withFormField(LumoVariables.FONT_SIZE_XL, theme)
                .withFormField(LumoVariables.FONT_SIZE_L, theme)
                .withFormField(LumoVariables.FONT_SIZE_M, theme)
                .withFormField(LumoVariables.FONT_SIZE_S, theme)
                .withFormField(LumoVariables.FONT_SIZE_XS, theme)
                .withFormField(LumoVariables.FONT_SIZE_XXS, theme);

        TextFieldEditorDetails lineHeight = new TextFieldEditorDetails("Line Height", eventService, false);
        lineHeight.withFormField(LumoVariables.LINE_HEIGHT_M, theme)
                .withFormField(LumoVariables.LINE_HEIGHT_S, theme)
                .withFormField(LumoVariables.LINE_HEIGHT_XS, theme);

        add(fontSize, lineHeight);
        setMargin(false);
        setPadding(false);
    }
}
