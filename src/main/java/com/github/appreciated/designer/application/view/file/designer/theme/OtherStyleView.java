package com.github.appreciated.designer.application.view.file.designer.theme;

import com.github.appreciated.designer.application.component.designer.TextFieldEditorDetails;
import com.github.appreciated.designer.application.model.LumoVariables;
import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.application.service.EventService;
import com.github.appreciated.designer.theme.css.Theme;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OtherStyleView extends VerticalLayout {
    public OtherStyleView(ProjectFileModel projectFileModel, EventService eventService) {
        Theme theme = projectFileModel.getInformation().getTheme();

        TextFieldEditorDetails boxShadow = new TextFieldEditorDetails(getTranslation("shadow"), eventService, false);
        boxShadow.withFormField(LumoVariables.BOX_SHADOW_XL, theme)
                .withFormField(LumoVariables.BOX_SHADOW_L, theme)
                .withFormField(LumoVariables.BOX_SHADOW_M, theme)
                .withFormField(LumoVariables.BOX_SHADOW_S, theme)
                .withFormField(LumoVariables.BOX_SHADOW_XS, theme);

        TextFieldEditorDetails borderRadius = new TextFieldEditorDetails(getTranslation("border.radius"), eventService, false);
        borderRadius.withFormField(LumoVariables.BORDER_RADIUS_L, theme)
                .withFormField(LumoVariables.BORDER_RADIUS_M, theme)
                .withFormField(LumoVariables.BORDER_RADIUS_S, theme);

        TextFieldEditorDetails iconSize = new TextFieldEditorDetails(getTranslation("icon.size"), eventService, false);
        iconSize.withFormField(LumoVariables.ICON_SIZE_L, theme)
                .withFormField(LumoVariables.ICON_SIZE_M, theme)
                .withFormField(LumoVariables.ICON_SIZE_S, theme);

        add(boxShadow, borderRadius, iconSize);
        setMargin(false);
        setPadding(false);
    }

}
