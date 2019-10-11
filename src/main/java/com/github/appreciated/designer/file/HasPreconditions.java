package com.github.appreciated.designer.file;

import java.util.Map;

public interface HasPreconditions {
    Map<String, Class> getPreconditionNames();

    Map<String, Object> getPreconditions();

    void setPreconditions(Map<String, Object> preconditionNames);
}
