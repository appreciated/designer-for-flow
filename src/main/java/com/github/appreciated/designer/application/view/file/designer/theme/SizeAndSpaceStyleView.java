package com.github.appreciated.designer.application.view.file.designer.theme;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.component.designer.TextFieldEditorDetails;
import com.github.appreciated.designer.model.LumoVariables;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.theme.css.Theme;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SizeAndSpaceStyleView extends VerticalLayout {
    public SizeAndSpaceStyleView(ProjectFileModel projectFileModel, EventService eventService) {
        Theme theme = projectFileModel.getInformation().getTheme();

        TextFieldEditorDetails fontSize = new TextFieldEditorDetails("Unit Size", eventService, false);
        fontSize.withFormField(LumoVariables.SIZE_XL, theme)
                .withFormField(LumoVariables.SIZE_L, theme)
                .withFormField(LumoVariables.SIZE_M, theme)
                .withFormField(LumoVariables.SIZE_S, theme)
                .withFormField(LumoVariables.SIZE_XS, theme);

        TextFieldEditorDetails lineHeight = new TextFieldEditorDetails("Space", eventService, false);
        lineHeight.withFormField(LumoVariables.SPACE_XL, theme)
                .withFormField(LumoVariables.SPACE_L, theme)
                .withFormField(LumoVariables.SPACE_M, theme)
                .withFormField(LumoVariables.SPACE_S, theme)
                .withFormField(LumoVariables.SPACE_XS, theme);

        add(fontSize, lineHeight);
        setMargin(false);
        setPadding(false);
    }

}
