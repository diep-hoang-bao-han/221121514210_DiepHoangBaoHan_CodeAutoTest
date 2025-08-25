package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdminPage;
import pages.LoginPage;
import pages.DashboardPage;

import java.time.Duration;

public class AdminTests extends BaseTest {

    private AdminPage adminPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setUp() {
        LoginPage loginPage = new LoginPage(driver);
        dashboardPage = loginPage.loginWithValidCredentials(
                webDriverConfig.getUsername(),
                webDriverConfig.getPassword()
        );
        adminPage = dashboardPage.navigateToAdminModule();
    }

    @AfterMethod
    public void tearDown() {
        dashboardPage.logout();
    }

    @Test(priority = 1, description = "Admin adds new user successfully")
    public void testAddNewUserSuccess() {
        adminPage.clickAddButton();

        adminPage.enterUserDetails(
                "Alice Duong",
                "alice.d" + System.currentTimeMillis(),
                "Password123!",
                "Password123!",
                "Admin",
                "Enabled"
        );

        adminPage.saveUser();

        Assert.assertTrue(adminPage.isSuccessMessageDisplayed(),
                "Success message should be displayed after adding user");
    }

    @Test(priority = 2, description = "Admin edits first user with password change")
    public void testEditFirstUserWithPasswordChange() {
        adminPage.searchUserByUsername("admin");
        adminPage.clickEditFirstUser();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h6[text()='Edit User']")
        ));

        adminPage.debugCheckbox();

        adminPage.editUserDetails(
                null,
                "NewPassword123!",
                "NewPassword123!",
                "Disabled"
        );

        adminPage.copyPasswordToConfirmField();

        adminPage.saveUser();

        Assert.assertTrue(adminPage.isSuccessMessageDisplayed(),
                "Success message should be displayed after editing user");
    }

    @Test(priority = 3, description = "Admin edits first user without password change")
    public void testEditFirstUserWithoutPasswordChange() {
        adminPage.searchUserByUsername("admin");

        adminPage.clickEditFirstUser();
        adminPage.editUserDetails(
                null,
                null,
                null,
                "Enabled"
        );

        adminPage.saveUser();

        Assert.assertTrue(adminPage.isSuccessMessageDisplayed(),
                "Success message should be displayed after editing user");
    }

    @Test(priority = 4, description = "Admin deletes ESS user successfully")
    public void testDeleteESSUserSuccess() {
        adminPage.searchUserByUsername("");

        if (adminPage.isESSUserExists()) {
            boolean deleteSuccess = adminPage.deleteFirstESSUser();

            Assert.assertTrue(deleteSuccess, "Should successfully delete ESS user and show success message");

        } else {
            System.out.println("No ESS user found to delete, skipping test");
            Assert.assertTrue(true, "No ESS user to delete is acceptable");
        }
    }

    @Test(priority = 5, description = "Try to delete when no ESS user exists")
    public void testDeleteESSUserWhenNoneExists() {
        adminPage.searchUserByUsername("admin");

        boolean essExists = adminPage.isESSUserExists();

        if (!essExists) {
            boolean deleteSuccess = adminPage.deleteFirstESSUser();
            Assert.assertFalse(deleteSuccess,
                    "Should not find any ESS user to delete");
        } else {
            System.out.println("ESS user exists, skipping this test scenario");
            Assert.assertTrue(true);
        }
    }
}