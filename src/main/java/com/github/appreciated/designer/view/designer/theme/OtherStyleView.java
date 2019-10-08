package com.github.appreciated.designer.view.designer.theme;

import com.github.appreciated.designer.component.designer.TextFieldEditorDetails;
import com.github.appreciated.designer.model.LumoVariables;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.service.ProjectService;
import com.github.appreciated.designer.theme.css.Theme;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OtherStyleView extends VerticalLayout {
    public OtherStyleView(ProjectService projectService, EventService eventService) {
        Theme theme = projectService.getCurrentFile().getTheme();

        TextFieldEditorDetails boxShadow = new TextFieldEditorDetails("Shadow", eventService, false);
        boxShadow.withFormField(LumoVariables.BOX_SHADOW_XL, theme)
                .withFormField(LumoVariables.BOX_SHADOW_L, theme)
                .withFormField(LumoVariables.BOX_SHADOW_M, theme)
                .withFormField(LumoVariables.BOX_SHADOW_S, theme)
                .withFormField(LumoVariables.BOX_SHADOW_XS, theme);

        TextFieldEditorDetails borderRadius = new TextFieldEditorDetails("Border Radius", eventService, false);
        borderRadius.withFormField(LumoVariables.BORDER_RADIUS_L, theme)
                .withFormField(LumoVariables.BORDER_RADIUS_M, theme)
                .withFormField(LumoVariables.BORDER_RADIUS_S, theme);

        TextFieldEditorDetails iconSize = new TextFieldEditorDetails("Icon Size", eventService, false);
        iconSize.withFormField(LumoVariables.ICON_SIZE_L, theme)
                .withFormField(LumoVariables.ICON_SIZE_M, theme)
                .withFormField(LumoVariables.ICON_SIZE_S, theme);

        add(boxShadow, borderRadius, iconSize);
        setMargin(false);
        setPadding(false);
    }

}
