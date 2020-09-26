package com.github.appreciated.designer.i18n;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

@Component
public class TranslationProvider implements I18NProvider {

    public static final String BUNDLE_PREFIX = "i18n";

    private final Locale LOCALE_DE = new Locale("de");
    private final Locale LOCALE_EN = new Locale("en");

    private final List<Locale> locales = Collections.unmodifiableList(Arrays.asList(LOCALE_DE, LOCALE_EN));

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            LoggerFactory.getLogger(TranslationProvider.class.getName())
                    .warn("Got lang request for key with null value!");
            return "";
        }

        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            LoggerFactory.getLogger(TranslationProvider.class.getName()).warn("Missing resource", e);
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }
}

