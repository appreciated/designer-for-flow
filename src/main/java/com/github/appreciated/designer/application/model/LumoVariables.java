package com.github.appreciated.designer.application.model;

import java.util.Arrays;
import java.util.Optional;

public enum LumoVariables {
    BASE_COLOR("--lumo-base-color", " #FFF"),
    BODY_TEXT_COLOR("--lumo-body-text-color", " var(--lumo-contrast-90pct)"),
    BORDER_RADIUS("--lumo-border-radius", " 0.25em"),
    BORDER_RADIUS_L("--lumo-border-radius-l", " 0.5em"),
    BORDER_RADIUS_M("--lumo-border-radius-m", " var(--lumo-border-radius, 0.25em)"),
    BORDER_RADIUS_S("--lumo-border-radius-s", " 0.25em"),
    BOX_SHADOW_L("--lumo-box-shadow-l", " 0 3px 18px -2px var(--lumo-shade-20pct), 0 12px 48px -6px var(--lumo-shade-40pct)"),
    BOX_SHADOW_M("--lumo-box-shadow-m", " 0 2px 6px -1px var(--lumo-shade-20pct), 0 8px 24px -4px var(--lumo-shade-40pct)"),
    BOX_SHADOW_S("--lumo-box-shadow-s", " 0 2px 4px -1px var(--lumo-shade-20pct), 0 3px 12px -1px var(--lumo-shade-30pct)"),
    BOX_SHADOW_XL("--lumo-box-shadow-xl", " 0 4px 24px -3px var(--lumo-shade-20pct), 0 18px 64px -8px var(--lumo-shade-40pct)"),
    BOX_SHADOW_XS("--lumo-box-shadow-xs", " 0 1px 4px -1px var(--lumo-shade-50pct)"),
    CLICKABLE_CURSOR("--lumo-clickable-cursor", " default--lumo-size-xs"),
    CONTRAST("--lumo-contrast", " var(--lumo-shade)"),
    CONTRAST_10PCT("--lumo-contrast-10pct", " var(--lumo-shade-10pct)"),
    CONTRAST_20PCT("--lumo-contrast-20pct", " var(--lumo-shade-20pct)"),
    CONTRAST_30PCT("--lumo-contrast-30pct", " var(--lumo-shade-30pct)"),
    CONTRAST_40PCT("--lumo-contrast-40pct", " var(--lumo-shade-40pct)"),
    CONTRAST_50PCT("--lumo-contrast-50pct", " var(--lumo-shade-50pct)"),
    CONTRAST_5PCT("--lumo-contrast-5pct", " var(--lumo-shade-5pct)"),
    CONTRAST_60PCT("--lumo-contrast-60pct", " var(--lumo-shade-60pct)"),
    CONTRAST_70PCT("--lumo-contrast-70pct", " var(--lumo-shade-70pct)"),
    CONTRAST_80PCT("--lumo-contrast-80pct", " var(--lumo-shade-80pct)"),
    CONTRAST_90PCT("--lumo-contrast-90pct", " var(--lumo-shade-90pct)"),
    DISABLED_TEXT_COLOR("--lumo-disabled-text-color", " var(--lumo-contrast-30pct)"),
    ERROR_COLOR("--lumo-error-color", " hsl(3, 100%, 61%)"),
    ERROR_COLOR_10PCT("--lumo-error-color-10pct", " hsla(3, 100%, 60%, 0.1)"),
    ERROR_COLOR_50PCT("--lumo-error-color-50pct", " hsla(3, 100%, 60%, 0.5)"),
    ERROR_CONTRAST_COLOR("--lumo-error-contrast-color", " #FFF"),
    ERROR_TEXT_COLOR("--lumo-error-text-color", " hsl(3, 92%, 53%)"),
    FONT_SIZE_L("--lumo-font-size-l", " 1.125rem"),
    FONT_SIZE_M("--lumo-font-size-m", " 1rem"),
    FONT_SIZE_S("--lumo-font-size-s", " .875rem"),
    FONT_SIZE_XL("--lumo-font-size-xl", " 1.375rem"),
    FONT_SIZE_XS("--lumo-font-size-xs", " .8125rem"),
    FONT_SIZE_XXL("--lumo-font-size-xxl", " 1.75rem"),
    FONT_SIZE_XXS("--lumo-font-size-xxs", " .75rem"),
    FONT_SIZE_XXXL("--lumo-font-size-xxxl", " 2.5rem"),
    HEADER_TEXT_COLOR("--lumo-header-text-color", " var(--lumo-contrast)"),
    ICON_SIZE("--lumo-icon-size", " var(--lumo-icon-size-m) --lumo-font-family"),
    ICON_SIZE_L("--lumo-icon-size-l", " 2.25em"),
    ICON_SIZE_M("--lumo-icon-size-m", " 1.5em"),
    ICON_SIZE_S("--lumo-icon-size-s", " 1.25em"),
    LINE_HEIGHT_M("--lumo-line-height-m", " 1.625"),
    LINE_HEIGHT_S("--lumo-line-height-s", " 1.375"),
    LINE_HEIGHT_XS("--lumo-line-height-xs", " 1.25"),
    PRIMARY_COLOR("--lumo-primary-color", " hsl(214, 90%, 52%)"),
    PRIMARY_COLOR_10PCT("--lumo-primary-color-10pct", " hsla(214, 90%, 52%, 0.1)"),
    PRIMARY_COLOR_50PCT("--lumo-primary-color-50pct", " hsla(214, 90%, 52%, 0.5)"),
    PRIMARY_CONTRAST_COLOR("--lumo-primary-contrast-color", " #FFF"),
    PRIMARY_TEXT_COLOR("--lumo-primary-text-color", " var(--lumo-primary-color)"),
    SECONDARY_TEXT_COLOR("--lumo-secondary-text-color", " var(--lumo-contrast-70pct)"),
    SHADE("--lumo-shade", " hsl(214, 35%, 15%)"),
    SHADE_10PCT("--lumo-shade-10pct", " hsla(214, 57%, 24%, 0.1)"),
    SHADE_20PCT("--lumo-shade-20pct", " hsla(214, 53%, 23%, 0.16)"),
    SHADE_30PCT("--lumo-shade-30pct", " hsla(214, 50%, 22%, 0.26)"),
    SHADE_40PCT("--lumo-shade-40pct", " hsla(214, 47%, 21%, 0.38)"),
    SHADE_50PCT("--lumo-shade-50pct", " hsla(214, 45%, 20%, 0.5)"),
    SHADE_5PCT("--lumo-shade-5pct", " hsla(214, 61%, 25%, 0.05)"),
    SHADE_60PCT("--lumo-shade-60pct", " hsla(214, 43%, 19%, 0.61)"),
    SHADE_70PCT("--lumo-shade-70pct", " hsla(214, 42%, 18%, 0.72)"),
    SHADE_80PCT("--lumo-shade-80pct", " hsla(214, 41%, 17%, 0.83)"),
    SHADE_90PCT("--lumo-shade-90pct", " hsla(214, 40%, 16%, 0.94)"),
    SIZE_L("--lumo-size-l", " 2.75rem"),
    SIZE_M("--lumo-size-m", " 2.25rem"),
    SIZE_S("--lumo-size-s", " 1.875rem"),
    SIZE_XL("--lumo-size-xl", " 3.5rem"),
    SIZE_XS("--lumo-size-xs", " 1.625rem"),
    SPACE_L("--lumo-space-l", " 1.5rem"),
    SPACE_M("--lumo-space-m", " 1rem"),
    SPACE_S("--lumo-space-s", " 0.5rem"),
    SPACE_TALL_L("--lumo-space-tall-l", " var(--lumo-space-l) calc(var(--lumo-space-l) / 2)"),
    SPACE_TALL_M("--lumo-space-tall-m", " var(--lumo-space-m) calc(var(--lumo-space-m) / 2)"),
    SPACE_TALL_S("--lumo-space-tall-s", " var(--lumo-space-s) calc(var(--lumo-space-s) / 2)"),
    SPACE_TALL_XL("--lumo-space-tall-xl", " var(--lumo-space-xl) calc(var(--lumo-space-xl) / 2)--lumo-space-wide-xs"),
    SPACE_TALL_XS("--lumo-space-tall-xs", " var(--lumo-space-xs) calc(var(--lumo-space-xs) / 2)"),
    SPACE_WIDE_L("--lumo-space-wide-l", " calc(var(--lumo-space-l) / 2) var(--lumo-space-l)"),
    SPACE_WIDE_M("--lumo-space-wide-m", " calc(var(--lumo-space-m) / 2) var(--lumo-space-m)"),
    SPACE_WIDE_S("--lumo-space-wide-s", " calc(var(--lumo-space-s) / 2) var(--lumo-space-s)"),
    SPACE_WIDE_XL("--lumo-space-wide-xl", " calc(var(--lumo-space-xl) / 2) var(--lumo-space-xl)"),
    SPACE_WIDE_XS("--lumo-space-wide-xs", " calc(var(--lumo-space-xs) / 2) var(--lumo-space-xs)"),
    SPACE_XL("--lumo-space-xl", " 2.5rem"),
    SPACE_XS("--lumo-space-xs", " 0.25rem"),
    SUCCESS_COLOR("--lumo-success-color", " hsl(145, 80%, 42%)"),
    SUCCESS_COLOR_10PCT("--lumo-success-color-10pct", " hsla(145, 76%, 44%, 0.12)"),
    SUCCESS_COLOR_50PCT("--lumo-success-color-50pct", " hsla(145, 76%, 44%, 0.55)"),
    SUCCESS_CONTRAST_COLOR("--lumo-success-contrast-color", " #FFF"),
    SUCCESS_TEXT_COLOR("--lumo-success-text-color", " hsl(145, 100%, 32%)"),
    TERTIARY_TEXT_COLOR("--lumo-tertiary-text-color", " var(--lumo-contrast-50pct)"),
    TINT("--lumo-tint", " #FFF"),
    TINT_10PCT("--lumo-tint-10pct", " hsla(0, 0%, 100%, 0.37)"),
    TINT_20PCT("--lumo-tint-20pct", " hsla(0, 0%, 100%, 0.44)"),
    TINT_30PCT("--lumo-tint-30pct", " hsla(0, 0%, 100%, 0.5)"),
    TINT_40PCT("--lumo-tint-40pct", " hsla(0, 0%, 100%, 0.57)"),
    TINT_50PCT("--lumo-tint-50pct", " hsla(0, 0%, 100%, 0.64)"),
    TINT_5PCT("--lumo-tint-5pct", " hsla(0, 0%, 100%, 0.3)"),
    TINT_60PCT("--lumo-tint-60pct", " hsla(0, 0%, 100%, 0.7)"),
    TINT_70PCT("--lumo-tint-70pct", " hsla(0, 0%, 100%, 0.77)"),
    TINT_80PCT("--lumo-tint-80pct", " hsla(0, 0%, 100%, 0.84)"),
    TINT_90PCT("--lumo-tint-90pct", " hsla(0, 0%, 100%, 0.9)");

    private final String variableName;
    private final String defaultValue;

    LumoVariables(String variableName, String defaultValue) {
        this.variableName = variableName;
        this.defaultValue = defaultValue;
    }

    public static Optional<LumoVariables> getValueForVariableName(String variableName) {
        return Arrays.stream(values()).filter(variables -> variables.variableName.equals(variableName)).findFirst();
    }

    public String getVariableName() {
        return variableName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
