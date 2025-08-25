package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class AdminPage extends BasePage {

    @FindBy(xpath = "//button[contains(., 'Add')]")
    private WebElement addButton;

    @FindBy(xpath = "//h6[text()='Add User']")
    private WebElement addUserHeader;

    @FindBy(xpath = "//label[text()='Employee Name']/following::input[1]")
    private WebElement employeeNameField;

    @FindBy(xpath = "//label[text()='Username']/following::input[1]")
    private WebElement usernameField;

    @FindBy(xpath = "(//input[@type='password'])[1]")
    private WebElement passwordField;

    @FindBy(xpath = "(//input[@type='password'])[2]")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "(//div[@class='oxd-select-text-input'])[1]")
    private WebElement userRoleDropdown;

    @FindBy(xpath = "(//div[@class='oxd-select-text-input'])[2]")
    private WebElement statusDropdown;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement saveButton;

    @FindBy(xpath = "//div[contains(@class,'oxd-toast')]")
    private WebElement toastMessage;

    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group')]//input[@placeholder='Type for hints...']")
    private WebElement searchUsernameField;

    @FindBy(xpath = "//button[contains(., 'Search')]")
    private WebElement searchButton;

    @FindBy(xpath = "//h6[text()='Edit User']")
    private WebElement editUserHeader;

    @FindBy(xpath = "//input[@type='password' and contains(@class, 'oxd-input')][1]")
    private WebElement editPasswordField;

    @FindBy(xpath = "//button[@class='oxd-icon-button oxd-table-cell-action-space']")
    private List<WebElement> deleteButtons;

    @FindBy(xpath = "//div[contains(@class, 'oxd-table-cell')][3]")
    private List<WebElement> userRoleCells;

    @FindBy(xpath = "//div[contains(@class, 'oxd-checkbox-wrapper')]//input[@type='checkbox']")
    private WebElement changePasswordCheckbox;

    @FindBy(xpath = "//div[contains(@class, 'oxd-checkbox-wrapper')]")
    private WebElement changePasswordCheckboxWrapper;

    @FindBy(xpath = "//div[contains(@class, 'oxd-checkbox-wrapper')]//label[contains(., 'Yes')]")
    private WebElement changePasswordYesLabel;

    @FindBy(xpath = "//input[@type='password' and contains(@class, 'oxd-input')][2]")
    private WebElement editConfirmPasswordField;

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public void clickAddButton() {
        System.out.println("Clicking Add button...");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", addButton);
        wait.until(ExpectedConditions.visibilityOf(addUserHeader));
    }

    public void enterUserDetails(String employeeName, String username, String password,
                                 String confirmPassword, String role, String status) {
        employeeNameField.sendKeys(employeeName.substring(0, 1));

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        longWait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(text(),'Searching...')]")
        ));

        longWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@role='listbox']//div[@role='option']")
        ));

        employeeNameField.sendKeys(Keys.ARROW_DOWN);
        employeeNameField.sendKeys(Keys.ENTER);

        longWait.until(ExpectedConditions.attributeToBeNotEmpty(employeeNameField, "value"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        confirmPasswordField.sendKeys(confirmPassword);

        selectDropdownWithoutWaitingListbox(userRoleDropdown, 1);
        selectDropdownWithoutWaitingListbox(statusDropdown, 1);
    }

    private void selectDropdownWithoutWaitingListbox(WebElement dropdown, int optionIndex) {
        System.out.println("Selecting dropdown option index: " + optionIndex);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", dropdown);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> options = driver.findElements(
                By.xpath("//div[contains(@class, 'oxd-select-option')]")
        );

        System.out.println("Found " + options.size() + " options in dropdown");

        if (options.size() >= optionIndex) {
            WebElement option = options.get(optionIndex - 1);
            System.out.println("Selecting option: " + option.getText());
            js.executeScript("arguments[0].click();", option);
        } else if (!options.isEmpty()) {
            System.out.println("Not enough options, selecting first option: " + options.get(0).getText());
            js.executeScript("arguments[0].click();", options.get(0));
        } else {
            System.out.println("No options found, using keyboard navigation");
            dropdown.sendKeys(Keys.ARROW_DOWN);
            dropdown.sendKeys(Keys.ENTER);
        }

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        try {
            shortWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(
                    dropdown, "-- Select --"
            )));
            System.out.println("Dropdown successfully selected: " + dropdown.getText());
        } catch (Exception e) {
            System.out.println("Dropdown may still show '-- Select --', but continuing...");
        }
    }

    public void saveUser() {
        System.out.println("=== BEFORE SAVE ===");
        System.out.println("User Role: " + userRoleDropdown.getText());
        System.out.println("Status: " + statusDropdown.getText());
        System.out.println("Employee Name: " + employeeNameField.getAttribute("value"));
        System.out.println("Username: " + usernameField.getAttribute("value"));

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        longWait.until(ExpectedConditions.elementToBeClickable(saveButton));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", saveButton);

        wait.until(ExpectedConditions.visibilityOf(toastMessage));
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(toastMessage));
            String messageText = toastMessage.getText();
            System.out.println("Toast message: " + messageText);

            return toastMessage.isDisplayed() &&
                    (messageText.contains("Successfully Updated") ||
                            messageText.contains("Successfully Saved"));
        } catch (Exception e) {
            System.out.println("Error checking success message: " + e.getMessage());
            return false;
        }
    }

    public void searchUserByUsername(String username) {
        searchUsernameField.clear();
        searchUsernameField.sendKeys(username);
        searchButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-table-card')]")
        ));
    }

    public void clickEditFirstUser() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-table-card')]")
        ));

        List<WebElement> userRecords = driver.findElements(
                By.xpath("//div[contains(@class, 'oxd-table-card')]")
        );

        if (!userRecords.isEmpty()) {
            WebElement firstRecord = userRecords.get(0);
            WebElement editBtn = firstRecord.findElement(
                    By.xpath(".//button[@type='button']//i[contains(@class, 'bi-pencil-fill')]")
            );
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", editBtn);
            wait.until(ExpectedConditions.visibilityOf(editUserHeader));
        } else {
            throw new RuntimeException("No users found in the list");
        }
    }


    public void editUserDetails(String newUsername, String newPassword,
                                String confirmNewPassword, String newStatus) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        if (newUsername != null && !newUsername.isEmpty() && !newUsername.equals("null")) {
            wait.until(ExpectedConditions.elementToBeClickable(usernameField));
            usernameField.clear();
            usernameField.sendKeys(newUsername);
        }

        if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("null")) {
            ensureChangePasswordChecked();

            wait.until(ExpectedConditions.visibilityOf(editPasswordField));
            wait.until(ExpectedConditions.elementToBeClickable(editPasswordField));
            editPasswordField.clear();
            editPasswordField.sendKeys(newPassword);

            handleConfirmPasswordField(newPassword);
        }

        if (newStatus != null && !newStatus.isEmpty() && !newStatus.equals("null")) {
            selectDropdownWithoutWaitingListbox(statusDropdown,
                    newStatus.equals("Enabled") ? 1 : 2);
        }

        System.out.println("User details updated");
    }

    private void handleConfirmPasswordField(String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {

            // Strategy 1: Direct interaction
            wait.until(ExpectedConditions.visibilityOf(editConfirmPasswordField));
            wait.until(ExpectedConditions.elementToBeClickable(editConfirmPasswordField));
            editConfirmPasswordField.clear();
            editConfirmPasswordField.sendKeys(password);
            System.out.println("Confirm password field handled directly");

        } catch (Exception e) {
            System.out.println("Direct approach failed, trying alternative methods: " + e.getMessage());

            // Strategy 2: JavaScript execution
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value = arguments[1];", editConfirmPasswordField, password);
                System.out.println("Confirm password set via JavaScript");
            } catch (Exception jsException) {
                System.out.println("JavaScript approach failed: " + jsException.getMessage());

                // Strategy 3: Find the element by different locator
                try {
                    WebElement confirmField = driver.findElement(
                            By.xpath("(//input[@type='password' and contains(@class, 'oxd-input')])[2]")
                    );
                    confirmField.clear();
                    confirmField.sendKeys(password);
                    System.out.println("Confirm password found via alternative locator");
                } catch (Exception altException) {
                    System.out.println("Alternative locator failed: " + altException.getMessage());

                    // Strategy 4: Copy-paste approach
                    try {
                        editPasswordField.sendKeys(Keys.CONTROL + "a");
                        editPasswordField.sendKeys(Keys.CONTROL + "c");

                        WebElement confirmField = driver.findElement(
                                By.xpath("//label[contains(text(),'Confirm Password')]/following::input[@type='password']")
                        );
                        confirmField.sendKeys(Keys.CONTROL + "v");
                        System.out.println("Password copied to confirm field");
                    } catch (Exception copyException) {
                        System.out.println("Copy-paste approach failed: " + copyException.getMessage());
                    }
                }
            }
        }
    }


    private void waitForPasswordFields() {
        System.out.println("Waiting for password fields...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {

            wait.until(ExpectedConditions.visibilityOf(editPasswordField));
            System.out.println("Password field is visible");

            try {
                wait.until(ExpectedConditions.visibilityOf(editConfirmPasswordField));
                System.out.println("Confirm password field is visible");
            } catch (Exception e) {
                System.out.println("Confirm password field not immediately visible, will handle dynamically");
            }

        } catch (Exception e) {
            System.out.println("Password fields not visible: " + e.getMessage());
        }
    }

    public void copyPasswordToConfirmField() {
        try {
            String password = editPasswordField.getAttribute("value");

            if (password != null && !password.isEmpty()) {
                try {
                    editConfirmPasswordField.clear();
                    editConfirmPasswordField.sendKeys(password);
                } catch (Exception e) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].value = arguments[1];", editConfirmPasswordField, password);
                }
                System.out.println("Password copied to confirm field: " + password);
            }
        } catch (Exception e) {
            System.out.println("Failed to copy password to confirm field: " + e.getMessage());
        }
    }


    public boolean deleteFirstESSUser() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'oxd-table-card')]")
            ));

            List<WebElement> tableRows = driver.findElements(
                    By.xpath("//div[contains(@class, 'oxd-table-row')]")
            );

            for (int i = 0; i < tableRows.size(); i++) {
                WebElement row = tableRows.get(i);

                List<WebElement> roleCells = row.findElements(
                        By.xpath(".//div[contains(@class, 'oxd-table-cell')][3]")
                );

                if (!roleCells.isEmpty()) {
                    String roleText = roleCells.get(0).getText().trim();

                    if ("ESS".equalsIgnoreCase(roleText)) {
                        System.out.println("Found ESS user at row: " + i);

                        List<WebElement> deleteButtonsInRow = row.findElements(
                                By.xpath(".//button[@type='button' and contains(@class, 'oxd-icon-button')]//i[contains(@class, 'bi-trash')]")
                        );

                        if (!deleteButtonsInRow.isEmpty()) {
                            WebElement deleteButton = deleteButtonsInRow.get(0);
                            System.out.println("Clicking delete button for ESS user...");

                            JavascriptExecutor js = (JavascriptExecutor) driver;
                            js.executeScript("arguments[0].click();", deleteButton);

                            wait.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.xpath("//button[contains(@class, 'oxd-button--label-danger') and contains(., 'Yes, Delete')]")
                            ));

                            WebElement confirmDeleteBtn = driver.findElement(
                                    By.xpath("//button[contains(@class, 'oxd-button--label-danger') and contains(., 'Yes, Delete')]")
                            );
                            js.executeScript("arguments[0].click();", confirmDeleteBtn);

                            wait.until(ExpectedConditions.visibilityOf(toastMessage));
                            String messageText = toastMessage.getText();
                            System.out.println("Toast message: " + messageText);

                            return messageText.contains("Success");
                        }
                    }
                }
            }

            System.out.println("No ESS user found to delete");
            return false;

        } catch (Exception e) {
            System.out.println("Error deleting ESS user: " + e.getMessage());
            return false;
        }
    }



    public boolean isESSUserExists() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-table-card')]")
        ));

        for (WebElement roleCell : userRoleCells) {
            if ("ESS".equalsIgnoreCase(roleCell.getText().trim())) {
                return true;
            }
        }
        return false;
    }


    public void clickChangePasswordCheckbox() {
        System.out.println("Clicking Change Password checkbox...");

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", changePasswordCheckbox);
            System.out.println("Checkbox clicked successfully");

        } catch (Exception e) {
            System.out.println("Strategy 1 failed, trying Strategy 2...");

            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", changePasswordCheckboxWrapper);
                System.out.println("Checkbox wrapper clicked");

            } catch (Exception e2) {
                System.out.println("Strategy 2 failed, trying Strategy 3...");

                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", changePasswordYesLabel);
                    System.out.println("Yes label clicked");

                } catch (Exception e3) {
                    System.out.println("All strategies failed, using direct XPath...");

                    WebElement checkbox = driver.findElement(
                            By.xpath("//div[contains(@class, 'oxd-checkbox-wrapper')]//input[@type='checkbox']")
                    );
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", checkbox);
                    System.out.println("Checkbox found and clicked via direct XPath");
                }
            }
        }

        waitForPasswordFields();
    }

    public boolean isChangePasswordChecked() {
        try {
            return changePasswordCheckbox.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    public void ensureChangePasswordChecked() {
        if (!isChangePasswordChecked()) {
            clickChangePasswordCheckbox();
        }
    }
    public void debugCheckbox() {
        System.out.println("=== DEBUG CHECKBOX ===");
        try {
            boolean isChecked = changePasswordCheckbox.isSelected();
            System.out.println("Change Password Checkbox is selected: " + isChecked);
            System.out.println("Checkbox is displayed: " + changePasswordCheckbox.isDisplayed());
            System.out.println("Checkbox is enabled: " + changePasswordCheckbox.isEnabled());
        } catch (Exception e) {
            System.out.println("Error debugging checkbox: " + e.getMessage());
        }
    }

}