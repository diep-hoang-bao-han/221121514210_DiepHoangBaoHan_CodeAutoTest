package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddEmployeePage extends BasePage {
    @FindBy(xpath = "//input[@placeholder='First Name']")
    private WebElement firstNameField;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    private WebElement lastNameField;

    @FindBy(xpath = "//label[text()='Employee Id']/following::input[1]")
    private WebElement employeeIdField;

    @FindBy(xpath = "//button[normalize-space()='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//h6[normalize-space()='Personal Details']")
    private WebElement personalDetailsHeader;

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public void enterFirstName(String firstName) {
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.sendKeys(lastName);
    }

    public void enterEmployeeId(String employeeId) {
        employeeIdField.clear();
        employeeIdField.sendKeys(employeeId);
    }

    public void clickSave() {
        saveButton.click();
    }

    public String getPersonalDetailsHeaderText() {
        return personalDetailsHeader.getText();
    }

    public void addNewEmployee(String firstName, String lastName) {
        enterFirstName(firstName);
        enterLastName(lastName);

        String timestampPart = String.valueOf(System.currentTimeMillis()).substring(8); // Lấy 5-6 ký tự cuối
        String uniqueId = "E" + timestampPart;

        enterEmployeeId(uniqueId);
        clickSave();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(personalDetailsHeader)
        );
    }
}