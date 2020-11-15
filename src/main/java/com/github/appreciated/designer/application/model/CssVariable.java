package com.github.appreciated.designer.application.model;

import java.util.Arrays;

public class CssVariable {

    private final LumoVariables variable;
    private String value;

    public CssVariable(LumoVariables variable, String value) {
        this.variable = variable;
        this.value = value;
    }

    public static void main(String[] args) {
        String test = "--lumo-size-xs: 1.625rem;\n" +
                "--lumo-size-s: 1.875rem;\n" +
                "--lumo-size-m: 2.25rem;\n" +
                "--lumo-size-l: 2.75rem;\n" +
                "--lumo-size-xl: 3.5rem;\n" +
                "--lumo-icon-size-s: 1.25em;\n" +
                "--lumo-icon-size-m: 1.5em;\n" +
                "--lumo-icon-size-l: 2.25em;\n" +
                "--lumo-icon-size: var(--lumo-icon-size-m); " +
                "--lumo-font-family: -apple-system, BlinkMacSystemFont, \"Roboto\", \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\";\n" +
                "--lumo-font-size-xxs: .75rem;\n" +
                "--lumo-font-size-xs: .8125rem;\n" +
                "--lumo-font-size-s: .875rem;\n" +
                "--lumo-font-size-m: 1rem;\n" +
                "--lumo-font-size-l: 1.125rem;\n" +
                "--lumo-font-size-xl: 1.375rem;\n" +
                "--lumo-font-size-xxl: 1.75rem;\n" +
                "--lumo-font-size-xxxl: 2.5rem;\n" +
                "--lumo-line-height-xs: 1.25;\n" +
                "--lumo-line-height-s: 1.375;\n" +
                "--lumo-line-height-m: 1.625;\n" +
                "--lumo-space-xs: 0.25rem;\n" +
                "--lumo-space-s: 0.5rem;\n" +
                "--lumo-space-m: 1rem;\n" +
                "--lumo-space-l: 1.5rem;\n" +
                "--lumo-space-xl: 2.5rem;\n" +
                "--lumo-space-wide-xs: calc(var(--lumo-space-xs) / 2) var(--lumo-space-xs);\n" +
                "--lumo-space-wide-s: calc(var(--lumo-space-s) / 2) var(--lumo-space-s);\n" +
                "--lumo-space-wide-m: calc(var(--lumo-space-m) / 2) var(--lumo-space-m);\n" +
                "--lumo-space-wide-l: calc(var(--lumo-space-l) / 2) var(--lumo-space-l);\n" +
                "--lumo-space-wide-xl: calc(var(--lumo-space-xl) / 2) var(--lumo-space-xl);\n" +
                "--lumo-space-tall-xs: var(--lumo-space-xs) calc(var(--lumo-space-xs) / 2);\n" +
                "--lumo-space-tall-s: var(--lumo-space-s) calc(var(--lumo-space-s) / 2);\n" +
                "--lumo-space-tall-m: var(--lumo-space-m) calc(var(--lumo-space-m) / 2);\n" +
                "--lumo-space-tall-l: var(--lumo-space-l) calc(var(--lumo-space-l) / 2);\n" +
                "--lumo-space-tall-xl: var(--lumo-space-xl) calc(var(--lumo-space-xl) / 2);" +
                "--lumo-space-wide-xs: calc(var(--lumo-space-xs) / 2) var(--lumo-space-xs);\n" +
                "--lumo-space-wide-s: calc(var(--lumo-space-s) / 2) var(--lumo-space-s);\n" +
                "--lumo-space-wide-m: calc(var(--lumo-space-m) / 2) var(--lumo-space-m);\n" +
                "--lumo-space-wide-l: calc(var(--lumo-space-l) / 2) var(--lumo-space-l);\n" +
                "--lumo-space-wide-xl: calc(var(--lumo-space-xl) / 2) var(--lumo-space-xl);\n" +
                "--lumo-space-tall-xs: var(--lumo-space-xs) calc(var(--lumo-space-xs) / 2);\n" +
                "--lumo-space-tall-s: var(--lumo-space-s) calc(var(--lumo-space-s) / 2);\n" +
                "--lumo-space-tall-m: var(--lumo-space-m) calc(var(--lumo-space-m) / 2);\n" +
                "--lumo-space-tall-l: var(--lumo-space-l) calc(var(--lumo-space-l) / 2);\n" +
                "--lumo-space-tall-xl: var(--lumo-space-xl) calc(var(--lumo-space-xl) / 2);\n" +
                "--lumo-base-color: #FFF;\n" +
                "--lumo-tint-5pct: hsla(0, 0%, 100%, 0.3);\n" +
                "--lumo-tint-10pct: hsla(0, 0%, 100%, 0.37);\n" +
                "--lumo-tint-20pct: hsla(0, 0%, 100%, 0.44);\n" +
                "--lumo-tint-30pct: hsla(0, 0%, 100%, 0.5);\n" +
                "--lumo-tint-40pct: hsla(0, 0%, 100%, 0.57);\n" +
                "--lumo-tint-50pct: hsla(0, 0%, 100%, 0.64);\n" +
                "--lumo-tint-60pct: hsla(0, 0%, 100%, 0.7);\n" +
                "--lumo-tint-70pct: hsla(0, 0%, 100%, 0.77);\n" +
                "--lumo-tint-80pct: hsla(0, 0%, 100%, 0.84);\n" +
                "--lumo-tint-90pct: hsla(0, 0%, 100%, 0.9);\n" +
                "--lumo-tint: #FFF;\n" +
                "--lumo-shade-5pct: hsla(214, 61%, 25%, 0.05);\n" +
                "--lumo-shade-10pct: hsla(214, 57%, 24%, 0.1);\n" +
                "--lumo-shade-20pct: hsla(214, 53%, 23%, 0.16);\n" +
                "--lumo-shade-30pct: hsla(214, 50%, 22%, 0.26);\n" +
                "--lumo-shade-40pct: hsla(214, 47%, 21%, 0.38);\n" +
                "--lumo-shade-50pct: hsla(214, 45%, 20%, 0.5);\n" +
                "--lumo-shade-60pct: hsla(214, 43%, 19%, 0.61);\n" +
                "--lumo-shade-70pct: hsla(214, 42%, 18%, 0.72);\n" +
                "--lumo-shade-80pct: hsla(214, 41%, 17%, 0.83);\n" +
                "--lumo-shade-90pct: hsla(214, 40%, 16%, 0.94);\n" +
                "--lumo-shade: hsl(214, 35%, 15%);\n" +
                "--lumo-contrast-5pct: var(--lumo-shade-5pct);\n" +
                "--lumo-contrast-10pct: var(--lumo-shade-10pct);\n" +
                "--lumo-contrast-20pct: var(--lumo-shade-20pct);\n" +
                "--lumo-contrast-30pct: var(--lumo-shade-30pct);\n" +
                "--lumo-contrast-40pct: var(--lumo-shade-40pct);\n" +
                "--lumo-contrast-50pct: var(--lumo-shade-50pct);\n" +
                "--lumo-contrast-60pct: var(--lumo-shade-60pct);\n" +
                "--lumo-contrast-70pct: var(--lumo-shade-70pct);\n" +
                "--lumo-contrast-80pct: var(--lumo-shade-80pct);\n" +
                "--lumo-contrast-90pct: var(--lumo-shade-90pct);\n" +
                "--lumo-contrast: var(--lumo-shade);\n" +
                "--lumo-header-text-color: var(--lumo-contrast);\n" +
                "--lumo-body-text-color: var(--lumo-contrast-90pct);\n" +
                "--lumo-secondary-text-color: var(--lumo-contrast-70pct);\n" +
                "--lumo-tertiary-text-color: var(--lumo-contrast-50pct);\n" +
                "--lumo-disabled-text-color: var(--lumo-contrast-30pct);\n" +
                "--lumo-primary-color: hsl(214, 90%, 52%);\n" +
                "--lumo-primary-color-50pct: hsla(214, 90%, 52%, 0.5);\n" +
                "--lumo-primary-color-10pct: hsla(214, 90%, 52%, 0.1);\n" +
                "--lumo-primary-text-color: var(--lumo-primary-color);\n" +
                "--lumo-primary-contrast-color: #FFF;\n" +
                "--lumo-error-color: hsl(3, 100%, 61%);\n" +
                "--lumo-error-color-50pct: hsla(3, 100%, 60%, 0.5);\n" +
                "--lumo-error-color-10pct: hsla(3, 100%, 60%, 0.1);\n" +
                "--lumo-error-text-color: hsl(3, 92%, 53%);\n" +
                "--lumo-error-contrast-color: #FFF;\n" +
                "--lumo-success-color: hsl(145, 80%, 42%);\n" +
                "--lumo-success-color-50pct: hsla(145, 76%, 44%, 0.55);\n" +
                "--lumo-success-color-10pct: hsla(145, 76%, 44%, 0.12);\n" +
                "--lumo-success-text-color: hsl(145, 100%, 32%);\n" +
                "--lumo-success-contrast-color: #FFF;\n" +
                "--lumo-border-radius-s: 0.25em;\n" +
                "--lumo-border-radius-m: var(--lumo-border-radius, 0.25em);\n" +
                "--lumo-border-radius-l: 0.5em;\n" +
                "--lumo-border-radius: 0.25em;\n" +
                "--lumo-box-shadow-xs: 0 1px 4px -1px var(--lumo-shade-50pct);\n" +
                "--lumo-box-shadow-s: 0 2px 4px -1px var(--lumo-shade-20pct), 0 3px 12px -1px var(--lumo-shade-30pct);\n" +
                "--lumo-box-shadow-m: 0 2px 6px -1px var(--lumo-shade-20pct), 0 8px 24px -4px var(--lumo-shade-40pct);\n" +
                "--lumo-box-shadow-l: 0 3px 18px -2px var(--lumo-shade-20pct), 0 12px 48px -6px var(--lumo-shade-40pct);\n" +
                "--lumo-box-shadow-xl: 0 4px 24px -3px var(--lumo-shade-20pct), 0 18px 64px -8px var(--lumo-shade-40pct);\n" +
                "--lumo-clickable-cursor: default;\n";
        Arrays.stream(test.split("\n"))
                .map((s) -> s.split(":"))
                .map((s) -> s[0].toUpperCase().substring(7).replaceAll("-", "_") + "(\"" + s[0] + "\",\"" + s[1].replace(";", "") + "\"),")
                .forEach(System.out::println);
    }

    public LumoVariables getVariable() {
        return variable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
