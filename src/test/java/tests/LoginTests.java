package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

public class LoginTests extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setUp() {
        driver.get(webDriverConfig.getBaseUrl());
        loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
    }

    @AfterMethod
    public void tearDown() {
        try {
            dashboardPage = new DashboardPage(driver);
            if (dashboardPage.isDashboardDisplayed()) {
                dashboardPage.logout();
            }
        } catch (Exception e) {
        }
    }

    @Test(priority = 1)
    public void testValidLogin() {
        dashboardPage = loginPage.loginWithValidCredentials(
                webDriverConfig.getUsername(),
                webDriverConfig.getPassword()
        );
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard page should be displayed after successful login");
    }

    @Test(priority = 2)
    public void testInvalidLogin() {
        loginPage.enterCredentials("Admin", "wrongpassword");
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Invalid credentials"),
                "Expected 'Invalid credentials' but got: " + errorMessage);
    }

    @Test(priority = 3, description = "Login with invalid username")
    public void testInvalidUsername() {
        loginPage.enterCredentials("Nonexistent", "admin123");
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Invalid credentials"),
                "Expected invalid credentials message but got: " + errorMessage);
    }

    @Test(priority = 4, description = "Login with empty username")
    public void testEmptyUsername() {
        loginPage.enterPassword("admin123");
        loginPage.clickLogin();
        String errorMessage = loginPage.getFieldErrorMessage("username");
        Assert.assertEquals(errorMessage.trim(), "Required",
                "Expected 'Required' but got: " + errorMessage);
    }

    @Test(priority = 5, description = "Login with empty password")
    public void testEmptyPassword() {
        loginPage.enterUsername("Admin");
        loginPage.clickLogin();
        String errorMessage = loginPage.getFieldErrorMessage("password");
        Assert.assertEquals(errorMessage.trim(), "Required",
                "Expected 'Required' but got: " + errorMessage);
    }
}
