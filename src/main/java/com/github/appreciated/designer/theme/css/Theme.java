package com.github.appreciated.designer.theme.css;

import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.model.LumoVariables;
import com.github.appreciated.designer.model.project.Project;
import com.github.appreciated.designer.theme.css.compiler.CssCompiler;
import com.github.appreciated.designer.theme.css.parser.CssParser;

import java.io.File;
import java.util.HashMap;

public class Theme {
    HashMap<LumoVariables, CssVariable> styles = new HashMap<>();
    private DesignCompilerInformation compilerInformation;
    private Project project;
    private File themeFile;

    public Theme(Project project) {
        this.project = project;
        if (project.hasThemeFile()) {
            init(this.project.getThemeFile());
        }
    }

    public void init(File file) {
        themeFile = file;
        CssParser parser = new CssParser(file);
        styles = parser.getLumoVariables();
    }

    public Theme() {
    }

    public void save() {
        CssCompiler cssCompiler = new CssCompiler(themeFile, styles);
        cssCompiler.save();
    }

    public HashMap<LumoVariables, CssVariable> getStyles() {
        return styles;
    }
}
