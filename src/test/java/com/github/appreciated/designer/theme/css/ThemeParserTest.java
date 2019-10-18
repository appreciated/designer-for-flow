package com.github.appreciated.designer.theme.css;

import com.github.appreciated.designer.helper.resources.ResourceHelper;
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
        File file = TempFile.get(ResourceHelper.getFileForResource("test-files/styles.css"));
        Theme theme = new Theme();
        theme.parseCss(file);

        HashMap<LumoVariables, CssVariable> styles = theme.getStyles();
        Assert.assertEquals("#fafafa", styles.get(LumoVariables.BASE_COLOR).getValue());
    }

    @Test
    public void readThemeWithoutHTMLClass() throws IOException {
        File file = TempFile.get(ResourceHelper.getFileForResource("test-files/styles-without-html.css"));
        Theme theme = new Theme();
        theme.parseCss(file);

        HashMap<LumoVariables, CssVariable> styles = theme.getStyles();
        Assert.assertFalse(styles.containsKey(LumoVariables.BASE_COLOR));
    }
}