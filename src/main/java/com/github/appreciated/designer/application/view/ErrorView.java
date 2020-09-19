package com.github.appreciated.designer.application.view;

import com.github.appreciated.designer.service.ExceptionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

@StyleSheet("./styles/theme.css")
public class ErrorView extends VerticalLayout {

    public ErrorView(ExceptionService exceptionService) {
        Icon bug = VaadinIcon.BUG.create();
        bug.setSize("75px");

        Label message = new Label(getTranslation("unfortunately.an.error.occurred.please.help.fixing.this.issue.by.reporting.it"));
        message.getStyle().set("text-align", "center");
        Button send = new Button(getTranslation("create.bugreport"));
        send.addThemeVariants(ButtonVariant.LUMO_ERROR);
        send.addClickListener(event -> {
            if (exceptionService.getError() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println("### Designer bug report Template");
                pw.println("If you think the issue could be related to your template files please consider providing it to make it easier to reproduce your issue. ");
                pw.println("### Stacktrace");
                pw.println("```");
                exceptionService.getError().printStackTrace(pw);
                try {
                    String issueTemplate = URLEncoder.encode(sw.toString(), "UTF-8");
                    String title = URLEncoder.encode(exceptionService.getError().getClass().getSimpleName() + ": " + exceptionService.getError().getMessage(), "UTF-8");
                    String body = (issueTemplate.length() < 2500 ? issueTemplate : issueTemplate.substring(0, 2500)) + URLEncoder.encode("...\n", "UTF-8") + URLEncoder.encode("```", "UTF-8");
                    String url = "https://github.com/appreciated/designer-for-flow/issues/new?labels=bug&title=" + title + "&body=" + body;

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
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                Notification.show(getTranslation("nothing.here.to.report"));
            }
        });
        add(bug, message, send);
        getElement().setAttribute("theme", getElement().getAttribute("theme").replace("spacing", "spacing-xl"));
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeUndefined();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }

}
