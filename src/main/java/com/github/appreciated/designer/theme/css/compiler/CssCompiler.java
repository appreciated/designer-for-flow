package com.github.appreciated.designer.theme.css.compiler;

import com.github.appreciated.designer.model.CssVariable;
import com.github.appreciated.designer.model.LumoVariables;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.*;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Optional;

public class CssCompiler {
    private final CascadingStyleSheet css;
    private File themeFile;
    private HashMap<LumoVariables, CssVariable> cssVariableMap;

    public CssCompiler(File themeFile, HashMap<LumoVariables, CssVariable> cssVariableMap) {
        this.themeFile = themeFile;
        this.cssVariableMap = cssVariableMap;
        css = CSSReader.readFromFile(themeFile, Charset.defaultCharset(), ECSSVersion.CSS30);
        CommonsArrayList<CSSStyleRule> rules = (CommonsArrayList) css.getAllRules();
        Optional<CSSStyleRule> htmlRules = rules.stream()
                .filter(cssStyleRule -> cssStyleRule.getAllSelectors()
                        .stream()
                        .anyMatch(cssSelector -> cssSelector.getAsCSSString().equals("html")))
                .findFirst();
        if (htmlRules.isPresent()) {
            addCssRules(htmlRules.get());
        } else {
            CSSStyleRule rule = new CSSStyleRule();
            addCssRules(rule);
            CSSSelector selector = new CSSSelector();
            selector.addMember(new CSSSelectorSimpleMember("html"));
            rule.addSelector(selector);
            rules.add(rule);
            css.addRule(rule);
        }

    }

    private void addCssRules(CSSStyleRule cssStyleRule) {
        cssStyleRule.removeAllDeclarations();
        cssVariableMap.forEach((variables, cssVariable) -> {
            if (cssVariable.getValue() != null && cssVariable.getValue().length() > 0) {
                CSSExpression expression = new CSSExpression();
                expression.addTermSimple(cssVariable.getValue());
                cssStyleRule.addDeclaration(new CSSDeclaration(variables.getVariableName(), expression));
            }
        });
    }

    public void save() {
        CSSWriter writer = new CSSWriter(ECSSVersion.CSS30);
        try {
            writer.writeCSS(css, new FileWriter(themeFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
