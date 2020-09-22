package com.github.appreciated.designer.helper;

import com.vaadin.flow.component.UI;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlHelper {

    public static void openUrl(String url) {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("win") >= 0) {
            Runtime rt = Runtime.getRuntime();
            try {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (os.indexOf("mac") >= 0) {
            Runtime rt = Runtime.getRuntime();
            try {
                rt.exec("open " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            UI.getCurrent().getPage().executeJs("window.open(\"" + url + "\")");
        }
    }
}
