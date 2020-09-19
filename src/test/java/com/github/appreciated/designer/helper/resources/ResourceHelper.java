package com.github.appreciated.designer.helper.resources;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ResourceHelper {
    public static String getPathForResource(String resource) {
        return ResourceHelper.class.getClassLoader().getResource(resource).getFile();
    }

    public static File getFileForResource(String resource) {
        return new File(getPathForResource(resource));
    }

    public static String getPlainTestProject() {
        return getFileForResource("plain-test-project").getPath();
    }

    public static String getSpringBootTestProject() {
        return getFileForResource("spring-boot-test-project").getPath();
    }

    @Test
    public void checkTestProjectsExistence() {
        Assertions.assertTrue(new File(getPlainTestProject()).exists());
        Assertions.assertTrue(new File(getSpringBootTestProject()).exists());
    }
}


