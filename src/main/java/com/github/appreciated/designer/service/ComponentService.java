package com.github.appreciated.designer.service;


import com.github.appreciated.designer.component.DesignerComponent;
import com.vaadin.flow.component.Component;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * A Class which purpose is to return every Vaadin {@link Component}. This being done by scanning the
 * "com.vaadin.flow.component" package for Classes with a {@link com.vaadin.flow.component.Tag} Annotation
 * and by brute forcing their constructor. If a Class cannot be instantiated it will not be available.
 */
public class ComponentService {

    public Stream<DesignerComponent> getAllComponents() {
        String pkg = "com.vaadin.flow.component";
        String tagAnnotation = pkg + ".Tag";
        ScanResult scanResult = new ClassGraph().enableAllInfo()             // Scan classes, methods, fields, annotations
                .acceptPackages(pkg)      // Scan com.xyz and subpackages (omit to scan all packages)
                .scan();                  // Start the scan

        ClassInfoList classesFound = scanResult.getClassesWithAnnotation(tagAnnotation);
        List<DesignerComponent> componentList = new ArrayList<>();

        classesFound.forEach(classInfo -> {
            try {
                try {
                    componentList.add(
                            new DesignerComponent(classInfo.getAnnotationInfo("com.vaadin.flow.component.Tag")
                                    .getParameterValues()
                                    .getValue("value")
                                    .toString(),
                                    (Class<? extends Component>) Class.forName(classInfo.getName()))
                    );
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        componentList.sort(Comparator.comparing(DesignerComponent::getTagName));
        return componentList.stream();
    }
}
