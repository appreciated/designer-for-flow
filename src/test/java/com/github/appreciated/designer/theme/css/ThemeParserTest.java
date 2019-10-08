package com.github.appreciated.designer.theme.css;

import com.github.appreciated.designer.io.TempFile;
import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.model.LumoVariables;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ThemeParserTest {

    @Test
    public void readThemeClass() throws IOException {
        Theme theme = new Theme();
        File file = TempFile.get(new File(getClass().getClassLoader().getResource("styles.css").getFile()));
        theme.init(file);

        HashMap<LumoVariables, CssVariable> styles = theme.getStyles();
        Assert.assertEquals("#fafafa", styles.get(LumoVariables.BASE_COLOR).getValue());
    }

    @Test
    public void readThemeWithoutHTMLClass() throws IOException {
        Theme theme = new Theme();
        File file = TempFile.get(new File(getClass().getClassLoader().getResource("styles-without-html.css").getFile()));
        theme.init(file);

        HashMap<LumoVariables, CssVariable> styles = theme.getStyles();
        Assert.assertFalse(styles.containsKey(LumoVariables.BASE_COLOR));
    }
}