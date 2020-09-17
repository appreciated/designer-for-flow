package com.github.appreciated.designer.application.view;

import com.github.appreciated.designer.service.ExceptionService;
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

        Label message = new Label("Unfortunately an error occurred. Please help fixing this issue by reporting it!");
        message.getStyle().set("text-align", "center");
        Button send = new Button("Create bugreport");
        send.addThemeVariants(ButtonVariant.LUMO_ERROR);
        send.addClickListener(event -> {
            if (exceptionService.getError() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println("### Designer Template");
                pw.println("If you think the issue could be related to your template files please consider providing it to make it easier to reproduce your issue. ");
                pw.println("### Stacktrace");
                pw.println("```");
                exceptionService.getError().printStackTrace(pw);
                pw.println("```");
                try {
                    String stackTrace = URLEncoder.encode(sw.toString(), "UTF-8");
                    String title = URLEncoder.encode(exceptionService.getError().getClass().getSimpleName(), "UTF-8");

                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://github.com/appreciated/designer-for-flow/issues/new?labels=bug&title=" + title + "&body=" + (stackTrace.length() < 400 ? stackTrace : stackTrace.substring(0, 400)) + "\\"));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                Notification.show("Nothing here to report");
            }
        });
        add(bug, message, send);
        getElement().setAttribute("theme", getElement().getAttribute("theme").replace("spacing", "spacing-xl"));
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeUndefined();
        setWidth("300px");
        setHeight("360px");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        getElement().getStyle()
                .set("background", "var(--lumo-error-color-10pct)");
    }

}
