package com.github.appreciated.designer.integrationtest.project;

import com.github.appreciated.designer.helper.resources.ResourceHelper;
import com.github.appreciated.designer.helper.url.UrlEncoder;
import com.github.appreciated.designer.integrationtest.IntegrationTestBase;
import org.junit.Test;

public class SpringBootTestProjectIntegrationTest extends IntegrationTestBase {
    @Test
    public void testPlainProject() {
        openPath("project/" + UrlEncoder.encode(ResourceHelper.getSpringBootTestProject()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

