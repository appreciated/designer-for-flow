package com.github.appreciated.designer.service;


import com.github.appreciated.designer.component.DesignerComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.upload.Upload;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A Class which purpose is to return every Vaadin {@link Component}. This being done by scanning the
 * "com.vaadin.flow.component" package for Classes with a {@link com.vaadin.flow.component.Tag} Annotation
 * and by brute forcing their constructor. If a Class cannot be instantiated it will not be available.
 */
public class ComponentService {

    public Stream<DesignerComponent> getAllComponents() {
        List<Class<? extends Component>> list = Arrays.asList(
                Accordion.class,
                AccordionPanel.class,
                Anchor.class,
                Button.class,
                Checkbox.class,
                ComboBox.class,
                ContextMenu.class,
                DatePicker.class,
                DateTimePicker.class,
                Div.class,
                EmailField.class,
                FormLayout.class,
                Grid.class,
                H1.class,
                H2.class,
                H3.class,
                HorizontalLayout.class,
                Image.class,
                ListBox.class,
                LoginForm.class,
                MenuBar.class,
                NumberField.class,
                Paragraph.class,
                PasswordField.class,
                ProgressBar.class,
                RadioButtonGroup.class,
                Select.class,
                Span.class,
                SplitLayout.class,
                Tab.class,
                Tabs.class,
                TextArea.class,
                TextField.class,
                TimePicker.class,
                TreeGrid.class,
                Upload.class,
                VerticalLayout.class
        );

        return list.stream().map(aClass -> {
            try {
                if (aClass.isAnnotationPresent(Tag.class)) {
                    return new DesignerComponent(aClass.getAnnotation(Tag.class).value(), aClass);
                } else {
                    return new DesignerComponent(aClass.getSimpleName(), aClass);
                }
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                System.err.println(aClass.getSimpleName());
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull);
    }
}
