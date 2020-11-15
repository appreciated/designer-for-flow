package com.github.appreciated.designer.helper;

import com.vaadin.flow.component.formlayout.FormLayout;
import elemental.json.impl.JreJsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FormLayoutHelper {
    @SuppressWarnings("ConstantConditions")
    public static List<FormLayout.ResponsiveStep> getActualResponsiveSteps(FormLayout layout) {
        List<FormLayout.ResponsiveStep> steps = layout.getResponsiveSteps();
        List<JreJsonObject> actualSteps = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            actualSteps.add((JreJsonObject) (Object) steps.get(i));
        }
        return actualSteps.stream()
                .map(jreJsonObject ->
                        new FormLayout.ResponsiveStep(
                                jreJsonObject.getString("minWidth"),
                                (int) jreJsonObject.getNumber("columns")
                        )
                ).collect(Collectors.toList());
    }
}
