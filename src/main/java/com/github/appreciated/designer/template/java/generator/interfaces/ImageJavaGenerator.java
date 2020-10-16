package com.github.appreciated.designer.template.java.generator.interfaces;

import com.github.appreciated.designer.model.DesignCompilerInformation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ImageJavaGenerator implements VaadinComponentJavaGenerator<Image> {

    private final DesignCompilerInformation designCompilerInformation;

    public ImageJavaGenerator(DesignCompilerInformation designCompilerInformation) {
        this.designCompilerInformation = designCompilerInformation;
    }

    @Override
    public boolean canParse(Component propertyParent) {
        return propertyParent instanceof Image;
    }

    @Override
    public boolean requiresParsing(Image propertyParent) {
        return propertyParent.getSrc() != null;
    }

    @Override
    public Stream<Expression> parse(CompilationUnit compilationUnit, Image propertyParent, Expression nameExpr) {
        List<Expression> expressionList = new ArrayList<>();
        if (propertyParent.getSrc() != null &&
                designCompilerInformation.hasComponentMetainfo(propertyParent) &&
                designCompilerInformation.getComponentMetainfo(propertyParent).hasPropertyReplacement("src")) {
            expressionList.add(new MethodCallExpr(nameExpr, "setSrc", new NodeList<>(new StringLiteralExpr((String) designCompilerInformation.getComponentMetainfo(propertyParent).getPropertyReplacement("src")))));
        }
        return expressionList.stream();
    }


}
