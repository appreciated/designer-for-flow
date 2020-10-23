package com.github.appreciated.designer.template.java.generator.components;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.appreciated.designer.template.java.generator.interfaces.ComponentJavaGenerator;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class HasTextJavaGenerator implements ComponentJavaGenerator<HasText> {

    private final DesignCompilerInformation designCompilerInformation;

    public HasTextJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof HasText;
    }

    @Override
    public boolean requiresGeneration(HasText propertyParent) {
        return propertyParent.getText() != null && !propertyParent.getText().equals("");
    }

    @Override
    public Stream<Expression> generate(CompilationUnit compilationUnit, HasText propertyParent, Expression nameExpr) {
        if (designCompilerInformation.hasCompilationMetaInformation((Component) propertyParent) && designCompilerInformation.getCompilationMetaInformation((Component) propertyParent).hasPropertyReplacement("text")) {
            List<Expression> expressionList = new ArrayList<>();
            if (propertyParent.getText() != null && designCompilerInformation.getCompilationMetaInformation((Component) propertyParent).hasPropertyReplacement("text")) {
                expressionList.add(
                        new MethodCallExpr(nameExpr,
                                "setText",
                                new NodeList<>(
                                        new MethodCallExpr("getTranslation",
                                                new StringLiteralExpr((String) designCompilerInformation.getCompilationMetaInformation((Component) propertyParent).getPropertyReplacement("text"))
                                        )
                                )
                        )
                );
            }
            return expressionList.stream();
        } else {
            return Stream.of(new MethodCallExpr(nameExpr, "setText", new NodeList<>(new StringLiteralExpr(propertyParent.getText()))));
        }
    }

    @Override
    public List<String> generatedProperties() {
        return Arrays.asList("text");
    }
}
