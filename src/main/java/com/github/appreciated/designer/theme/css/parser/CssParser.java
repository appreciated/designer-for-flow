package com.github.appreciated.designer.theme.css.parser;

import com.github.appreciated.designer.application.model.CssVariable;
import com.github.appreciated.designer.application.model.LumoVariables;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;

public class CssParser {

    private final HashMap<String, String> styles = new HashMap<>();

    public CssParser(File themeFile) {
        final CascadingStyleSheet aCSS = CSSReader.readFromFile(themeFile, Charset.defaultCharset(), ECSSVersion.CSS30);
        CommonsArrayList<CSSStyleRule> rules = (CommonsArrayList) aCSS.getAllRules();
        rules.stream().filter(cssStyleRule -> cssStyleRule.getAllSelectors()
                .stream()
                .anyMatch(cssSelector -> cssSelector.getAsCSSString().equals("html")))
                .forEach(rule -> rule.getAllDeclarations()
                        .forEach(cssDeclaration ->
                                styles.put(cssDeclaration.getProperty(), cssDeclaration.getExpression().getAsCSSString().replaceAll("\"", ""))
                        )
                );
        System.out.println();
    }

    public HashMap<LumoVariables, CssVariable> getLumoVariables() {
        HashMap<LumoVariables, CssVariable> variableHashMap = new HashMap<>();
        styles.forEach((s, s2) ->
                LumoVariables.getValueForVariableName(s).ifPresent(variables ->
                        variableHashMap.put(variables, new CssVariable(variables, s2))
                )
        );
        return variableHashMap;
    }
}
