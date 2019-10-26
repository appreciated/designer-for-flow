package com.github.appreciated.designer.vaadin.converter;

import com.github.appreciated.designer.application.model.file.ProjectFileModel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.io.File;

public class PathValidator implements Validator<String> {


    private ProjectFileModel projectFileModel;

    public PathValidator(ProjectFileModel projectFileModel) {
        this.projectFileModel = projectFileModel;
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        File actualFile = new File(projectFileModel.getInformation().getProject().getFrontendFolder() + File.separator + value);
        if (value == null || actualFile.exists()) {
            return ValidationResult.ok();
        } else {
            System.err.println(actualFile.getPath() + "does not exist");
            return ValidationResult.error("File does not exist");
        }
    }
}
