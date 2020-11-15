package com.github.appreciated.designer.template.java.generator.components;

import com.github.appreciated.designer.application.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TextFieldJavaGenerator implements ComponentJavaGenerator<TextField> {

    private final DesignCompilerInformation designCompilerInformation;

    public TextFieldJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof TextField;
    }

    @Override
    public boolean requiresGeneration(TextField propertyParent) {
        return designCompilerInformation.hasCompilationMetaInformation(propertyParent) &&
                (designCompilerInformation.getCompilationMetaInformation(propertyParent).hasPropertyReplacement("label") || designCompilerInformation.getCompilationMetaInformation(propertyParent).hasPropertyReplacement("caption"));
    }

    @Override
    public Stream<Expression> generate(CompilationUnit compilationUnit, TextField propertyParent, Expression nameExpr) {
        List<Expression> expressionList = new ArrayList<>();
        if (designCompilerInformation.hasCompilationMetaInformation(propertyParent) && designCompilerInformation.getCompilationMetaInformation(propertyParent).hasPropertyReplacement("label")) {
            if (propertyParent.getLabel() != null && designCompilerInformation.getCompilationMetaInformation(propertyParent).hasPropertyReplacement("label")) {
                expressionList.add(
                        new MethodCallExpr(nameExpr,
                                "setLabel",
                                new NodeList<>(
                                        new MethodCallExpr("getTranslation",
                                                new StringLiteralExpr((String) designCompilerInformation.getCompilationMetaInformation(propertyParent).getPropertyReplacement("label"))
                                        )
                                )
                        )
                );
            }
        } else {
            expressionList.add(new MethodCallExpr(nameExpr, "setLabel", new NodeList<>(new StringLiteralExpr(propertyParent.getLabel()))));
        }
        if (designCompilerInformation.hasCompilationMetaInformation(propertyParent) && designCompilerInformation.getCompilationMetaInformation(propertyParent).hasPropertyReplacement("placeholder")) {
            if (propertyParent.getLabel() != null && designCompilerInformation.getCompilationMetaInformation(propertyParent).hasPropertyReplacement("placeholder")) {
                expressionList.add(
                        new MethodCallExpr(nameExpr,
                                "setPlaceholder",
                                new NodeList<>(
                                        new MethodCallExpr("getTranslation",
                                                new StringLiteralExpr((String) designCompilerInformation.getCompilationMetaInformation(propertyParent).getPropertyReplacement("placeholder"))
                                        )
                                )
                        )
                );
            }
        } else {
            expressionList.add(new MethodCallExpr(nameExpr, "setPlaceholder", new NodeList<>(new StringLiteralExpr(propertyParent.getPlaceholder()))));
        }
        return expressionList.stream();
    }

    @Override
    public List<String> generatedProperties() {
        return Arrays.asList("label", "placeholder");
    }

}
