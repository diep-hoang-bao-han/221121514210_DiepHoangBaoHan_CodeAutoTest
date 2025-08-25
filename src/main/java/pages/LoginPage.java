package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@name='username' or @id='username' or contains(@placeholder,'Username')]")
    private WebElement usernameField;

    @FindBy(xpath = "//input[@type='password' or @name='password' or contains(@placeholder,'Password')]")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' or contains(text(),'Login')]")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class,'error')]//p | //div[contains(@class,'alert')]//p | //span[contains(@class,'error')]")
    private WebElement errorMessage;

    @FindBy(xpath = "//span[contains(@class,'error')][contains(@class,'input-field')]")
    private List<WebElement> fieldErrorMessages;

    @FindBy(xpath = "//div[contains(@class,'forgot-password')]")
    private WebElement forgotPasswordLink;

    // Specific error message locators for empty fields
    @FindBy(xpath = "//input[@name='username']/following::span[contains(@class,'error')][1]")
    private WebElement usernameError;

    @FindBy(xpath = "//input[@name='password']/following::span[contains(@class,'error')][1]")
    private WebElement passwordError;

    public LoginPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    // ======= Actions =======

    public void enterUsername(String username) {
        waitForElementToBeVisible(usernameField, 10);
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        waitForElementToBeVisible(passwordField, 10);
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        waitForElementToBeClickable(loginButton, 10);
        loginButton.click();
    }

    public void enterCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public DashboardPage loginWithValidCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new DashboardPage(driver);
    }

    public String getErrorMessage() {
        try {
            waitForElementToBeVisible(errorMessage, 5);
            return errorMessage.getText().trim();
        } catch (Exception e) {
            return "No error message found";
        }
    }


    public String getFieldErrorMessage(String fieldName) {
        try {
            WebElement errorElement;
            switch (fieldName.toLowerCase()) {
                case "username":
                    waitForElementToBeVisible(usernameError, 5);
                    errorElement = usernameError;
                    break;
                case "password":
                    waitForElementToBeVisible(passwordError, 5);
                    errorElement = passwordError;
                    break;
                default:
                    return "Invalid field name";
            }
            return errorElement.getText().trim();
        } catch (Exception e) {
            return "Error message not found";
        }
    }

    public boolean isLoginPageDisplayed() {
        try {
            waitForElementToBeVisible(usernameField, 5);
            waitForElementToBeVisible(passwordField, 5);
            waitForElementToBeVisible(loginButton, 5);
            return usernameField.isDisplayed() &&
                    passwordField.isDisplayed() &&
                    loginButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public void waitForPageToLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete")
        );
    }

    private void waitForElementToBeVisible(WebElement element, int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    private void waitForElementToBeClickable(WebElement element, int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.elementToBeClickable(element));
    }
}