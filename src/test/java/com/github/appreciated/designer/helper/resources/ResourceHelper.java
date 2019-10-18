package com.github.appreciated.designer.helper.resources;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ResourceHelper {
    public static String getPathForResource(String resource) {
        return ResourceHelper.class.getClassLoader().getResource(resource).getFile();
    }

    public static File getFileForResource(String resource) {
        return new File(getPathForResource(resource));
    }

    public static String getPlainTestProject() {
        return getPathForResource("plain-test-project");
    }

    public static String getSpringBootTestProject() {
        return getPathForResource("spring-boot-test-project");
    }

    @Test
    public void checkTestProjectsExistence() {
        Assert.assertTrue(new File(getPlainTestProject()).exists());
        Assert.assertTrue(new File(getSpringBootTestProject()).exists());
    }
}


