package com.github.appreciated.designer.helper;

import com.github.appreciated.designer.application.model.CompilationMetaInformation;
import com.github.appreciated.designer.application.model.project.Project;
import com.github.appreciated.designer.application.service.ComponentService;
import com.vaadin.flow.component.Component;

import javax.lang.model.SourceVersion;
import java.util.Map;
import java.util.stream.Stream;

public class FieldNameHelper {
    public static boolean isFieldNameValid(String value, Component component, Stream<String> components, Map<Component, CompilationMetaInformation> compilationMetaInformation) {
        if (!SourceVersion.isName(value)) {
            return false;
        }
        if (components.anyMatch(s -> value.toLowerCase().startsWith(s.toLowerCase()))) {
            return false;
        }
        return compilationMetaInformation.entrySet().stream()
                .filter(compilationMetaInformation1 -> compilationMetaInformation1.getKey() != component)
                .noneMatch(compilationMetaInformation1 -> value.equals(compilationMetaInformation1.getValue().getVariableName()));
    }

    public static Stream<String> getComponents(Project project) {
        return new ComponentService()
                .getAllComponents(project)
                .map(component1 -> component1.getClassNameAsString().toLowerCase());
    }
}
