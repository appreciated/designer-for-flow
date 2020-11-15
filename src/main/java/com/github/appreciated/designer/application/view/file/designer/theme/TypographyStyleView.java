package com.github.appreciated.designer.application.view.file.designer.theme;

import com.github.appreciated.designer.application.component.designer.TextFieldEditorDetails;
import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.service.EventService;
import com.github.appreciated.designer.model.LumoVariables;
import com.github.appreciated.designer.theme.css.Theme;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TypographyStyleView extends VerticalLayout {
    public TypographyStyleView(ProjectFileModel projectFileModel, EventService eventService) {
        Theme theme = projectFileModel.getInformation().getTheme();

        TextFieldEditorDetails fontSize = new TextFieldEditorDetails(getTranslation("font.size"), eventService, false);
        fontSize.withFormField(LumoVariables.FONT_SIZE_XXXL, theme)
                .withFormField(LumoVariables.FONT_SIZE_XXL, theme)
                .withFormField(LumoVariables.FONT_SIZE_XL, theme)
                .withFormField(LumoVariables.FONT_SIZE_L, theme)
                .withFormField(LumoVariables.FONT_SIZE_M, theme)
                .withFormField(LumoVariables.FONT_SIZE_S, theme)
                .withFormField(LumoVariables.FONT_SIZE_XS, theme)
                .withFormField(LumoVariables.FONT_SIZE_XXS, theme);

        TextFieldEditorDetails lineHeight = new TextFieldEditorDetails(getTranslation("line.height"), eventService, false);
        lineHeight.withFormField(LumoVariables.LINE_HEIGHT_M, theme)
                .withFormField(LumoVariables.LINE_HEIGHT_S, theme)
                .withFormField(LumoVariables.LINE_HEIGHT_XS, theme);

        add(fontSize, lineHeight);
        setMargin(false);
        setPadding(false);
    }
}
