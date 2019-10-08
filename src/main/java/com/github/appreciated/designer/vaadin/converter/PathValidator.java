package com.github.appreciated.designer.vaadin.converter;

import com.github.appreciated.designer.service.ProjectService;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.io.File;

public class PathValidator implements Validator<String> {

    private ProjectService service;

    public PathValidator(ProjectService service) {
        this.service = service;
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        File actualFile = new File(service.getProject().getFrontendFolder() + File.separator + value);
        if (value == null || actualFile.exists()) {
            return ValidationResult.ok();
        } else {
            System.err.println(actualFile.getPath() + "does not exist");
            return ValidationResult.error("File does not exist");
        }
    }
}
