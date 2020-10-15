package com.github.appreciated.designer.theme.css;

import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.model.LumoVariables;
import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.theme.css.compiler.CssCompiler;
import com.github.appreciated.designer.theme.css.parser.CssParser;

import java.io.File;
import java.util.HashMap;

public class Theme {
    HashMap<LumoVariables, CssVariable> styles = new HashMap<>();
    private Project project;
    private File themeFile;

    public Theme(Project project) {
        this.project = project;
        init();
    }

    public void init() {
        if (project.hasThemeFile()) {
            themeFile = project.getThemeFile();
            parseCss(themeFile);
        }
    }

    public void parseCss(File file) {
        CssParser parser = new CssParser(file);
        styles = parser.getLumoVariables();
    }

    public Theme() {

    }

    public void save() {
        save(themeFile);
    }

    public void save(File file) {
        CssCompiler cssCompiler = new CssCompiler(file, styles);
        cssCompiler.save();
    }

    public HashMap<LumoVariables, CssVariable> getStyles() {
        return styles;
    }
}
