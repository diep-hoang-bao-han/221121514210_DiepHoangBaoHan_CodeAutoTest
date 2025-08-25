package tests;

import configs.WebDriverConfig;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import java.io.IOException;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverConfig webDriverConfig;

    @BeforeTest
    public void setup() throws IOException {
        webDriverConfig = new WebDriverConfig();
        driver = webDriverConfig.initializeDriver();
        driver.get(webDriverConfig.getBaseUrl());
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}