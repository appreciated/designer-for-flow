package com.github.appreciated.designer.regex;

public class CssRegex {
    public static String getLengthRegex() {
        return "^-?\\d+(em|ex|%|px|cm|mm|in|pt|pc|ch|rem|vh|vw|vmin|vmax)$|unset|^$";
    }

}
