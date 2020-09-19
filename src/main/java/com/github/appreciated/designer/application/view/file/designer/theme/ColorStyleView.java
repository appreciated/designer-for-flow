package com.github.appreciated.designer.application.view.file.designer.theme;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.github.appreciated.designer.component.designer.ColorPickerEditorDetails;
import com.github.appreciated.designer.model.LumoVariables;
import com.github.appreciated.designer.service.EventService;
import com.github.appreciated.designer.theme.css.Theme;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ColorStyleView extends VerticalLayout {
    public ColorStyleView(ProjectFileModel projectFileModel, EventService eventService) {
        Theme theme = projectFileModel.getInformation().getTheme();

        ColorPickerEditorDetails primary = new ColorPickerEditorDetails(getTranslation("primary"), eventService, false);
        primary.withFormField(LumoVariables.PRIMARY_COLOR, theme)
                .withFormField(LumoVariables.PRIMARY_CONTRAST_COLOR, theme)
                .withFormField(LumoVariables.PRIMARY_TEXT_COLOR, theme);

        ColorPickerEditorDetails basic = new ColorPickerEditorDetails(getTranslation("basic"), eventService, false);
        basic.withFormField(LumoVariables.BASE_COLOR, theme)
                .withFormField(LumoVariables.SHADE, theme)
                .withFormField(LumoVariables.TINT, theme);

        ColorPickerEditorDetails error = new ColorPickerEditorDetails(getTranslation("error"), eventService, false);
        error.withFormField(LumoVariables.ERROR_COLOR, theme)
                .withFormField(LumoVariables.ERROR_CONTRAST_COLOR, theme)
                .withFormField(LumoVariables.ERROR_TEXT_COLOR, theme);

        ColorPickerEditorDetails success = new ColorPickerEditorDetails(getTranslation("success"), eventService, false);
        success.withFormField(LumoVariables.SUCCESS_COLOR, theme)
                .withFormField(LumoVariables.SUCCESS_CONTRAST_COLOR, theme)
                .withFormField(LumoVariables.SUCCESS_TEXT_COLOR, theme);

        ColorPickerEditorDetails text = new ColorPickerEditorDetails(getTranslation("text"), eventService, false);
        text.withFormField(LumoVariables.HEADER_TEXT_COLOR, theme)
                .withFormField(LumoVariables.BODY_TEXT_COLOR, theme)
                .withFormField(LumoVariables.SECONDARY_TEXT_COLOR, theme)
                .withFormField(LumoVariables.TERTIARY_TEXT_COLOR, theme)
                .withFormField(LumoVariables.DISABLED_TEXT_COLOR, theme);

        ColorPickerEditorDetails shade = new ColorPickerEditorDetails(getTranslation("shade"), eventService, false);
        shade.withFormField(LumoVariables.SHADE_90PCT, theme)
                .withFormField(LumoVariables.SHADE_80PCT, theme)
                .withFormField(LumoVariables.SHADE_70PCT, theme)
                .withFormField(LumoVariables.SHADE_60PCT, theme)
                .withFormField(LumoVariables.SHADE_50PCT, theme)
                .withFormField(LumoVariables.SHADE_40PCT, theme)
                .withFormField(LumoVariables.SHADE_30PCT, theme)
                .withFormField(LumoVariables.SHADE_20PCT, theme)
                .withFormField(LumoVariables.SHADE_10PCT, theme)
                .withFormField(LumoVariables.SHADE_5PCT, theme);

        ColorPickerEditorDetails tint = new ColorPickerEditorDetails(getTranslation("tint"), eventService, false);
        tint.withFormField(LumoVariables.TINT_90PCT, theme)
                .withFormField(LumoVariables.TINT_80PCT, theme)
                .withFormField(LumoVariables.TINT_70PCT, theme)
                .withFormField(LumoVariables.TINT_60PCT, theme)
                .withFormField(LumoVariables.TINT_50PCT, theme)
                .withFormField(LumoVariables.TINT_40PCT, theme)
                .withFormField(LumoVariables.TINT_30PCT, theme)
                .withFormField(LumoVariables.TINT_20PCT, theme)
                .withFormField(LumoVariables.TINT_10PCT, theme)
                .withFormField(LumoVariables.TINT_5PCT, theme);

        add(primary, basic, error, success, text, shade, tint);
        setMargin(false);
        setPadding(false);
    }
}
