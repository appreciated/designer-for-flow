package com.github.appreciated.designer.integrationtest;

import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.codeborne.selenide.Selenide.open;

public class IntegrationTestBase {

    private static ChromeDriver driver;

    @BeforeClass
    public static void init() {
        WebDriverManager.chromedriver().version("76.0.3809.68").setup();
        driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
    }

    @AfterClass
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void openPath(String path) {
        open(getWebsiteUrl(path));
    }

    public String getWebsiteUrl(String path) {
        return "http://localhost:" + getServerPort() + "/" + path;
    }

    public int getServerPort() {
        return 8080;
    }
}
