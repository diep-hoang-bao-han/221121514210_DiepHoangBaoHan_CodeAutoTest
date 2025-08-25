package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.MyInfoPage;

import java.io.File;

public class MyInfoTests extends BaseTest {

    private DashboardPage dashboardPage;
    private MyInfoPage myInfoPage;

    @BeforeMethod
    public void setUp() {
        LoginPage loginPage = new LoginPage(driver);
        dashboardPage = loginPage.loginWithValidCredentials(
                webDriverConfig.getUsername(),
                webDriverConfig.getPassword()
        );
        myInfoPage = dashboardPage.navigateToMyInfoModule();
    }

    @AfterMethod
    public void tearDown() {
        dashboardPage.logout();
    }

    @Test(priority = 1, description = "Test view personal details")
    public void testViewPersonalDetails() {
        myInfoPage.navigateToPersonalDetails();
        Assert.assertTrue(myInfoPage.isPersonalDetailsDisplayed(), "Personal details should be visible");
    }

    @Test(priority = 2, description = "Test edit personal details (Employee Full Name)")
    public void testEditFullName() throws InterruptedException {
        String newFirstName = "TestF";
        String newMiddleName = "TestM";
        String newLastName = "TestL";

        System.out.println("[INFO] Attempting to change name to: " + newFirstName + " " + newMiddleName + " " + newLastName);
        myInfoPage.editFullName(newFirstName, newMiddleName, newLastName);

        boolean isUpdated = false;
        int retryCount = 0;
        while (!isUpdated && retryCount < 5) {
            isUpdated = myInfoPage.isFullNameUpdated(newFirstName, newMiddleName, newLastName);
            if (!isUpdated) {
                retryCount++;
                System.out.println("[WARN] Verification failed, retrying (" + retryCount + "/5)");
                Thread.sleep(3000);
            }
        }

        Assert.assertTrue(isUpdated, "Full name should be updated after 5 retries.");
    }

    @Test(priority = 3, description = "Test add membership in My Info")
    public void testAddMembership() throws InterruptedException {
        myInfoPage.addMembership("ACCA", "2025-08-19", "2026-08-19", "50.00", "Company", "Afghanistan Afghani");

        boolean isAdded = false;
        int retryCount = 0;
        while (!isAdded && retryCount < 3) {
            isAdded = myInfoPage.isMembershipAdded("ACCA", "2025-08-19", "50.00", "Afghanistan Afghani", "2026-08-19");
            if (!isAdded) {
                retryCount++;
                System.out.println("[WARN] Membership not found, retrying (" + retryCount + "/3)");
                Thread.sleep(2000);
            }
        }

        Assert.assertTrue(isAdded, "Membership should be added with all details after 3 retries");
    }
}
