package com.github.appreciated.designer.theme.css;

import com.github.appreciated.designer.application.model.CssVariable;
import com.github.appreciated.designer.application.model.LumoVariables;
import com.github.appreciated.designer.helper.resources.ResourceHelper;
import com.github.appreciated.designer.io.TempFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ThemeCompilerTest {

    @Test
    public void readAndWriteThemeWithoutHTMLClass() throws IOException {
        File file = TempFile.get(ResourceHelper.getFileForResource("test-files/styles-without-html.css"));
        Theme theme = new Theme();
        theme.parseCss(file);

        HashMap<LumoVariables, CssVariable> styles = theme.getStyles();
        Assertions.assertFalse(styles.containsKey(LumoVariables.BASE_COLOR));
        styles.put(LumoVariables.BASE_COLOR, new CssVariable(LumoVariables.BASE_COLOR, "#f9f9f9"));
        theme.save(file);

        theme = new Theme();
        theme.parseCss(file);
        styles = theme.getStyles();
        Assertions.assertEquals("#f9f9f9", styles.get(LumoVariables.BASE_COLOR).getValue());
    }

    @Test
    public void readAndWriteThemeClass() throws IOException {
        File file = TempFile.get(ResourceHelper.getFileForResource("test-files/styles.css"));
        Theme theme = new Theme();
        theme.parseCss(file);

        // Read
        HashMap<LumoVariables, CssVariable> styles = theme.getStyles();
        Assertions.assertEquals("#fafafa", styles.get(LumoVariables.BASE_COLOR).getValue());
        //write
        styles.put(LumoVariables.BASE_COLOR, new CssVariable(LumoVariables.BASE_COLOR, "#ffffff"));
        theme.save(file);

        theme = new Theme();
        theme.parseCss(file);
        // Read
        styles = theme.getStyles();
        Assertions.assertEquals("#ffffff", styles.get(LumoVariables.BASE_COLOR).getValue());
    }
}
