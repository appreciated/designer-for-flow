package com.github.appreciated.designer.template.java.generator.components;

import com.github.appreciated.designer.application.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.appreciated.designer.helper.FormLayoutHelper.getActualResponsiveSteps;

public class FormLayoutJavaGenerator implements ComponentJavaGenerator<FormLayout> {

    private final DesignCompilerInformation designCompilerInformation;

    public FormLayoutJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof FormLayout;
    }

    @Override
    public boolean requiresGeneration(FormLayout propertyParent) {
        return propertyParent.getResponsiveSteps() != null;
    }

    @Override
    public Stream<Expression> generate(CompilationUnit compilationUnit, FormLayout propertyParent, Expression nameExpr) {
        if (propertyParent.getResponsiveSteps() != null && !propertyParent.getResponsiveSteps().isEmpty()) {
            List<Expression> expressionList = getActualResponsiveSteps(propertyParent).stream().map(responsiveStep -> {
                try {
                    return new ObjectCreationExpr()
                            .setType("com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep")
                            .setArguments(
                                    NodeList.nodeList(
                                            new StringLiteralExpr((String) FieldUtils.getField(FormLayout.ResponsiveStep.class, "minWidth", true).get(responsiveStep)),
                                            new IntegerLiteralExpr(String.valueOf(FieldUtils.getField(FormLayout.ResponsiveStep.class, "columns", true).getInt(responsiveStep)))
                                    ));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            return Stream.of(new MethodCallExpr(nameExpr, "setResponsiveSteps", new NodeList<>(expressionList)));
        }
        return Stream.empty();
    }

    @Override
    public List<String> generatedProperties() {
        return Arrays.asList("src");
    }
}
