package com.github.appreciated.designer.view;

import com.github.appreciated.designer.service.ExceptionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Route("bugreport")
public class ErrorPage extends VerticalLayout {

    public ErrorPage(@Autowired ExceptionService exceptionService) {
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
                pw.println("```");
                exceptionService.getError().printStackTrace(pw);
                pw.println("```");
                try {
                    String stackTrace = URLEncoder.encode(sw.toString(), "UTF-8");
                    String title = URLEncoder.encode(exceptionService.getError().getClass().getSimpleName(), "UTF-8");
                    UI.getCurrent().getPage().executeJs("window.open(\"https://github.com/appreciated/designer-for-flow/issues/new?title=" + title + "&body=" + stackTrace + "\")");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                Notification.show("Nothing here to report");
            }
        });
        VerticalLayout wrapper = new VerticalLayout(bug, message, send);
        wrapper.getElement().setAttribute("theme", wrapper.getElement().getAttribute("theme").replace("spacing", "spacing-xl"));
        wrapper.setAlignItems(Alignment.CENTER);
        wrapper.setJustifyContentMode(JustifyContentMode.CENTER);
        wrapper.setSizeUndefined();
        wrapper.getStyle()
                .set("background", "var(--lumo-error-color-10pct)")
                .set("border-radius", "var(--lumo-border-radius)")
                .set("box-shadow", "var(--lumo-box-shadow-m)");
        wrapper.setWidth("300px");
        wrapper.setHeight("360px");
        add(wrapper);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        getElement().getStyle()
                .set("background", "var(--lumo-error-color-10pct)");
    }

}
