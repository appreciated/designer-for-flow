package com.github.appreciated.designer.service;


import com.github.appreciated.designer.component.DesignerComponent;
import com.github.appreciated.designer.helper.DesignerFileHelper;
import com.github.appreciated.designer.model.project.Project;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A Class which purpose is to return the available Vaadin {@link Component}'s.
 */
public class ComponentService {

    public Stream<DesignerComponent> getAllComponents(Project model) {
        return Stream.concat(Stream.concat(getHtmlComponents(), getVaadinComponents()), getProjectComponents(model));
    }

    public Stream<DesignerComponent> getAllButProjectComponents() {
        return Stream.concat(getHtmlComponents(), getVaadinComponents());
    }

    public Stream<DesignerComponent> getHtmlComponents() {
        List<Class<? extends Component>> list = Arrays.asList(
                Anchor.class,
                Article.class,
                Aside.class,
                DescriptionList.class,
                Div.class,
                Emphasis.class,
                Footer.class,
                H1.class,
                H2.class,
                H3.class,
                H4.class,
                H5.class,
                H6.class,
                Header.class,
                Hr.class,
                IFrame.class,
                Image.class,
                Label.class,
                ListItem.class,
                Main.class,
                NativeButton.class,
                Nav.class,
                OrderedList.class,
                Paragraph.class,
                Pre.class,
                Section.class,
                Span.class,
                UnorderedList.class
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

    public Stream<DesignerComponent> getVaadinComponents() {
        List<Class<? extends Component>> list = Arrays.asList(
                Accordion.class,
                AccordionPanel.class,
                Button.class,
                Checkbox.class,
                CheckboxGroup.class,
                ComboBox.class,
                ContextMenu.class,
                DatePicker.class,
                DateTimePicker.class,
                EmailField.class,
                FormLayout.class,
                Grid.class,
                HorizontalLayout.class,
                Image.class,
                ListBox.class,
                LoginForm.class,
                MenuBar.class,
                NumberField.class,
                PasswordField.class,
                ProgressBar.class,
                RadioButtonGroup.class,
                Select.class,
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

    public Stream<DesignerComponent> getProjectComponents(Project model) {
        return findDesignerClassesInProject(model);
    }

    private Stream<DesignerComponent> findDesignerClassesInProject(Project model) {
        File folder = model.getSourceFolder();
        return findDesignerClassesInPackage(folder);
    }

    private Stream<DesignerComponent> findDesignerClassesInPackage(File packageOrClassFile) {
        return findDesignerClassFilesInPackage(packageOrClassFile).map(DesignerComponent::new);
    }

    public Stream<File> findDesignerClassFilesInPackage(File packageOrClassFile) {
        if (packageOrClassFile.isDirectory()) {
            return Arrays.stream(packageOrClassFile.listFiles()).map(this::findDesignerClassFilesInPackage).reduce(Stream::concat).orElse(Stream.empty());
        } else if (DesignerFileHelper.isFileDesignerFile(packageOrClassFile)) {
            return Stream.of(packageOrClassFile);
        }
        return Stream.empty();
    }


}
