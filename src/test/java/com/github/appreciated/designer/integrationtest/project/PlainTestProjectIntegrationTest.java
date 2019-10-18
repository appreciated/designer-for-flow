package com.github.appreciated.designer.integrationtest.project;

import com.github.appreciated.designer.helper.resources.ResourceHelper;
import com.github.appreciated.designer.helper.url.UrlEncoder;
import com.github.appreciated.designer.integrationtest.IntegrationTestBase;
import org.junit.Test;

public class PlainTestProjectIntegrationTest extends IntegrationTestBase {
    @Test
    public void testPlainProject() {
        openPath("project?path=" + UrlEncoder.encode(ResourceHelper.getPlainTestProject()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
