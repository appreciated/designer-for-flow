package com.github.appreciated.designer.application.dialog.file.preconditions;

import java.util.Map;

public interface HasPreconditions {
    Map<String, Class> getPreconditionNames();

    Map<String, Object> getPreconditions();

    void setPreconditions(Map<String, Object> preconditionNames);
}
