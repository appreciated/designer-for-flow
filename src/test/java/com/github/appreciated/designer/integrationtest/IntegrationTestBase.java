package com.github.appreciated.designer.integrationtest;

import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.open;

public class IntegrationTestBase {

    public static WebDriver driver;
    public static int SLEEP = 400;

    @BeforeClass
    public static void init() {
        List<String> versions = WebDriverManager.chromedriver().getVersions().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (String version : versions) {
            try {
                // TODO Save the working version somewhere persistent to use it in other ui tests
                System.out.println("Trying Webdriver with version " + version);
                WebDriverManager.chromedriver().version(version).setup();
                driver = new ChromeDriver();
                WebDriverRunner.setWebDriver(driver);
                System.out.println("Using Webdriver with version " + version);
                break;
            } catch (Exception exception) {
                System.out.println("Webdriver with version " + version + " not suitable for installed browser");
            }
        }
    }

    @AfterClass
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static WebDriver getDriver() {
        return driver;
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
