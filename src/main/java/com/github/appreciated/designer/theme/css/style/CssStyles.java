package com.github.appreciated.designer.theme.css.style;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssStyles {

    private static CssStyles instance;
    Map<String, CssStyle> styles = new HashMap<>();

    private CssStyles() {
        try {
            IOUtils.readLines(new ClassPathResource("msdn-css.ref").getInputStream(), Charset.defaultCharset())
                    .forEach(s -> styles.put(s.substring(46), new CssStyle(s.substring(46), s)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CssStyles getInstance() {
        if (instance == null) {
            instance = new CssStyles();
        }
        return instance;
    }

    public static void main(String[] args) {

        File f = new File("...\\CSS Referenz - CSS   MDN.html");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            ArrayList<String> urls = new ArrayList<>();
            Pattern p = Pattern.compile("(https:\\/\\/developer.mozilla.org\\/de\\/docs\\/Web\\/CSS\\/\\S*\")");
            lines.stream()
                    .filter(s -> p.matcher(s).find())
                    .forEach(s -> {
                        Matcher matcher = p.matcher(s);
                        while (matcher.find()) {
                            urls.add(matcher.group(1).substring(0, matcher.group(0).length() - 1));
                        }
                    });
            urls.stream()
                    .filter(s -> !s.contains("@"))
                    .filter(s -> !s.substring(46).contains(":"))
                    .filter(s -> !s.endsWith("an-plus-b"))
                    .filter(s -> !s.substring(46).contains("#"))
                    .filter(s -> !s.substring(46).contains("*"))
                    .filter(s -> !s.substring(46).contains("/"))
                    .filter(s -> !s.substring(46).contains("_"))
                    .filter(s -> s.substring(46).charAt(0) != s.substring(46).toUpperCase().charAt(0))
                    .forEach(System.out::println);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, CssStyle> getStyles() {
        return styles;
    }
}
