package com.github.appreciated.designer.theme.css;

import com.github.appreciated.designer.io.TempFile;
import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.model.LumoVariables;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ThemeCompilerTest {

    @Test
    public void readAndWriteThemeWithoutHTMLClass() throws IOException {
        File file = TempFile.get(new File(getClass().getClassLoader().getResource("styles-without-html.css").getFile()));
        Theme theme = new Theme(file);
        theme.init();

        HashMap<LumoVariables, CssVariable> styles = theme.getStyles();
        Assert.assertFalse(styles.containsKey(LumoVariables.BASE_COLOR));
        styles.put(LumoVariables.BASE_COLOR, new CssVariable(LumoVariables.BASE_COLOR, "#f9f9f9"));
        theme.save();

        theme = new Theme(file);
        theme.init();
        styles = theme.getStyles();
        Assert.assertEquals("#f9f9f9", styles.get(LumoVariables.BASE_COLOR).getValue());
    }

    @Test
    public void readAndWriteThemeClass() throws IOException {
        File file = TempFile.get(new File(getClass().getClassLoader().getResource("styles.css").getFile()));
        Theme theme = new Theme(file);
        theme.init();

        // Read
        HashMap<LumoVariables, CssVariable> styles = theme.getStyles();
        Assert.assertEquals("#fafafa", styles.get(LumoVariables.BASE_COLOR).getValue());
        //write
        styles.put(LumoVariables.BASE_COLOR, new CssVariable(LumoVariables.BASE_COLOR, "#ffffff"));
        theme.save();

        theme = new Theme(file);
        // Read
        styles = theme.getStyles();
        Assert.assertEquals("#ffffff", styles.get(LumoVariables.BASE_COLOR).getValue());
    }
}
