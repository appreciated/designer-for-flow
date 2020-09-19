package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TextFieldJavaGenerator implements VaadinComponentJavaGenerator<TextField> {

    private final DesignCompilerInformation designCompilerInformation;

    public TextFieldJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof TextField;
    }

    @Override
    public boolean requiresParsing(TextField propertyParent) {
        return designCompilerInformation.hasComponentMetainfo(propertyParent) &&
                (designCompilerInformation.getComponentMetainfo(propertyParent).hasPropertyReplacement("label") || designCompilerInformation.getComponentMetainfo(propertyParent).hasPropertyReplacement("caption"));
    }

    @Override
    public Stream<Expression> parse(CompilationUnit compilationUnit, TextField propertyParent, Expression nameExpr) {
        List<Expression> expressionList = new ArrayList<>();
        if (designCompilerInformation.hasComponentMetainfo(propertyParent) && designCompilerInformation.getComponentMetainfo(propertyParent).hasPropertyReplacement("label")) {
            if (propertyParent.getLabel() != null && designCompilerInformation.getComponentMetainfo(propertyParent).hasPropertyReplacement("label")) {
                expressionList.add(
                        new MethodCallExpr(nameExpr,
                                "setLabel",
                                new NodeList<>(
                                        new MethodCallExpr("getTranslation",
                                                new StringLiteralExpr((String) designCompilerInformation.getComponentMetainfo(propertyParent).getPropertyReplacement("label"))
                                        )
                                )
                        )
                );
            }
        } else {
            expressionList.add(new MethodCallExpr(nameExpr, "setLabel", new NodeList<>(new StringLiteralExpr(propertyParent.getLabel()))));
        }
        if (designCompilerInformation.hasComponentMetainfo(propertyParent) && designCompilerInformation.getComponentMetainfo(propertyParent).hasPropertyReplacement("placeholder")) {
            if (propertyParent.getLabel() != null && designCompilerInformation.getComponentMetainfo(propertyParent).hasPropertyReplacement("placeholder")) {
                expressionList.add(
                        new MethodCallExpr(nameExpr,
                                "setPlaceholder",
                                new NodeList<>(
                                        new MethodCallExpr("getTranslation",
                                                new StringLiteralExpr((String) designCompilerInformation.getComponentMetainfo(propertyParent).getPropertyReplacement("placeholder"))
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
}
