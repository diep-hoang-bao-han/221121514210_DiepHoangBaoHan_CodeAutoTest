package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage extends BasePage {

    @FindBy(xpath = "//span[text()='PIM']")
    private WebElement pimMenu;

    @FindBy(xpath = "//span[text()='My Info']")
    private WebElement myInfoMenu;

    @FindBy(xpath = "//span[text()='Admin']")
    private WebElement adminMenu;

    @FindBy(xpath = "//p[@class='oxd-userdropdown-name']")
    private WebElement userDropdown;

    @FindBy(xpath = "//a[text()='Logout']")
    private WebElement logoutButton;

    // WebElement để xác nhận trang Dashboard
    @FindBy(xpath = "//h6[text()='Dashboard']")
    private WebElement dashboardTitle;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardDisplayed() {
        return dashboardTitle.isDisplayed();
    }

    public PIMPage navigateToPIMModule() {
        pimMenu.click();
        return new PIMPage(driver);
    }

    public MyInfoPage navigateToMyInfoModule() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(myInfoMenu)).click();
        return new MyInfoPage(driver);
    }

    public AdminPage navigateToAdminModule() {
        adminMenu.click();
        return new AdminPage(driver);
    }

    public void logout() {
        userDropdown.click();
        logoutButton.click();
    }
}