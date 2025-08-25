package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class MyInfoPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "middleName")
    private WebElement middleNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(xpath = "//h6[text()='Personal Details']")
    private WebElement personalDetailsHeader;

    @FindBy(xpath = "//button[@type='submit' and contains(., 'Save')]")
    private WebElement saveButton;

    @FindBy(xpath = "//button[contains(@class, 'oxd-button--text') and .//i[contains(@class, 'bi-plus')]]")
    private WebElement addMembershipButton;

    @FindBy(xpath = "(//div[contains(@class, 'oxd-select-wrapper')])[1]")
    private WebElement membershipDropdown;

    @FindBy(xpath = "(//div[contains(@class, 'oxd-select-wrapper')])[2]")
    private WebElement subscriptionPaidByDropdown;

    @FindBy(xpath = "//label[contains(text(),'Subscription Amount')]/following::input[1]")
    private WebElement subscriptionAmountInput;

    @FindBy(xpath = "(//div[contains(@class, 'oxd-select-wrapper')])[3]")
    private WebElement currencyDropdown;

    @FindBy(xpath = "(//input[@placeholder='yyyy-dd-mm'])[1]")
    private WebElement subscriptionCommenceDateInput;

    @FindBy(xpath = "(//input[@placeholder='yyyy-dd-mm'])[2]")
    private WebElement subscriptionRenewalDateInput;

    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'oxd-button--secondary')]")
    private WebElement membershipSaveButton;

    @FindBy(xpath = "//div[contains(@class, 'oxd-toast--error')]")
    private WebElement errorMessage;

    @FindBy(xpath = "//div[contains(@class, 'oxd-toast--success')]")
    private WebElement successMessage;


    public MyInfoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void navigateToPersonalDetails() {
        WebElement myInfoMenu = wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//span[text()='My Info']/ancestor::a")));
        myInfoMenu.click();
        wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader));
    }

    private void clearFieldCompletely(WebElement element) {
        try {

            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);

            element.sendKeys(Keys.CONTROL + "a");
            element.sendKeys(Keys.DELETE);
            element.clear();

            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);

            Thread.sleep(500);

            String currentValue = element.getAttribute("value");
            if (currentValue != null && !currentValue.isEmpty()) {
                System.out.println("[WARN] Field still not empty after clearing, value: '" + currentValue + "'");

                ((JavascriptExecutor) driver).executeScript("arguments[0].select(); arguments[0].value = '';", element);
                Thread.sleep(300);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error clearing field completely: " + e.getMessage());
        }
    }

    public void editFullName(String firstName, String middleName, String lastName) {
        navigateToPersonalDetails();

        waitForLoaderToDisappear();

        clearFieldCompletely(firstNameInput);
        wait.until(ExpectedConditions.attributeToBe(firstNameInput, "value", ""));
        firstNameInput.sendKeys(firstName);
        System.out.println("First name set to: " + firstNameInput.getAttribute("value"));

        clearFieldCompletely(middleNameInput);
        wait.until(ExpectedConditions.attributeToBe(middleNameInput, "value", ""));
        middleNameInput.sendKeys(middleName);

        clearFieldCompletely(lastNameInput);
        wait.until(ExpectedConditions.attributeToBe(lastNameInput, "value", ""));
        lastNameInput.sendKeys(lastName);

        scrollToElement(saveButton);
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();

        waitForSaveCompletion();
    }

    private void waitForLoaderToDisappear() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'oxd-form-loader')]")));
        } catch (Exception e) {
            System.out.println("[INFO] Loader not present or already disappeared");
        }
    }


    private void waitForSaveCompletion() {
        try {
            waitForLoaderToDisappear();

            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("[INFO] Wait for save completion interrupted");
        }
    }

    private void clearInputField(WebElement element) {
        element.click();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.clear();

        String value = element.getAttribute("value");
        if (value != null && !value.isEmpty()) {
            System.out.println("[WARN] Input field not empty after clear, value: " + value);
            element.sendKeys(Keys.CONTROL + "a");
            element.sendKeys(Keys.DELETE);
        }
    }


    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public boolean isFullNameUpdated(String expectedFirstName, String expectedMiddleName, String expectedLastName) {
        driver.navigate().refresh();

        waitForLoaderToDisappear();

        try {
            wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader));

            Thread.sleep(1500);

            String jsFirstName = (String) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].value;", firstNameInput);
            String seleniumFirstName = firstNameInput.getAttribute("value");

            String jsMiddleName = (String) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].value;", middleNameInput);
            String seleniumMiddleName = middleNameInput.getAttribute("value");

            String jsLastName = (String) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].value;", lastNameInput);
            String seleniumLastName = lastNameInput.getAttribute("value");

            System.out.println("[DEBUG] ========== VERIFICATION RESULTS ==========");
            System.out.println("[DEBUG] Expected: '" + expectedFirstName + "' | '" + expectedMiddleName + "' | '" + expectedLastName + "'");
            System.out.println("[DEBUG] JS Values: '" + jsFirstName + "' | '" + jsMiddleName + "' | '" + jsLastName + "'");
            System.out.println("[DEBUG] Selenium Values: '" + seleniumFirstName + "' | '" + seleniumMiddleName + "' | '" + seleniumLastName + "'");
            System.out.println("[DEBUG] First name match: " + expectedFirstName.equals(jsFirstName) + " (JS) / " + expectedFirstName.equals(seleniumFirstName) + " (Selenium)");
            System.out.println("[DEBUG] Middle name match: " + expectedMiddleName.equals(jsMiddleName) + " (JS) / " + expectedMiddleName.equals(seleniumMiddleName) + " (Selenium)");
            System.out.println("[DEBUG] Last name match: " + expectedLastName.equals(jsLastName) + " (JS) / " + expectedLastName.equals(seleniumLastName) + " (Selenium)");
            System.out.println("[DEBUG] ==========================================");

            return expectedFirstName.equals(jsFirstName) &&
                    expectedMiddleName.equals(jsMiddleName) &&
                    expectedLastName.equals(jsLastName);
        } catch (Exception e) {
            System.out.println("[ERROR] Error in verification: " + e.getMessage());
            return false;
        }
    }

    public void addMembership(String membership, String commenceDate, String renewalDate, String amount, String paidBy, String currency) {
        navigateToMemberships();
        wait.until(ExpectedConditions.elementToBeClickable(addMembershipButton)).click();

        wait.until(ExpectedConditions.visibilityOf(membershipDropdown));

        membershipDropdown.click();
        selectDropdownOption(membership);

        subscriptionPaidByDropdown.click();
        selectDropdownOption(paidBy);

        subscriptionAmountInput.clear();
        subscriptionAmountInput.sendKeys(amount);

        currencyDropdown.click();
        selectDropdownOption(currency);

        setDateInput(subscriptionCommenceDateInput, commenceDate);
        setDateInput(subscriptionRenewalDateInput, renewalDate);

        scrollToElement(membershipSaveButton);
        wait.until(ExpectedConditions.elementToBeClickable(membershipSaveButton)).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOf(successMessage),
                ExpectedConditions.visibilityOf(errorMessage)
        ));
    }

    private void selectDropdownOption(String optionText) {
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='option']//span[contains(text(), '" + optionText + "')]")));
        option.click();
    }

    private void setDateInput(WebElement dateInput, String date) {
        dateInput.clear();
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.TAB);
    }

    public boolean isMembershipAdded(String membership, String commenceDate, String amount, String currency, String renewalDate) {
        navigateToMemberships();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-table-row')]")));

        try {
            WebElement row = driver.findElement(By.xpath(
                    "//div[contains(@class, 'oxd-table-cell') and contains(.,'" + membership + "')]/ancestor::div[contains(@class, 'oxd-table-row')]"));

            return row.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void navigateToMemberships() {
        WebElement tab = wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//a[contains(text(), 'Memberships')]")));
        tab.click();
        wait.until(ExpectedConditions.visibilityOf(addMembershipButton));
    }


    public boolean isPersonalDetailsDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}