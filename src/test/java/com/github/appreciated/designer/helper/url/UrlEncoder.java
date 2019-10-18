package com.github.appreciated.designer.helper.url;

import org.springframework.web.util.UriUtils;

public class UrlEncoder {
    public static String encode(String path) {
        return UriUtils.encodePath(path, "UTF-8");
    }
}
