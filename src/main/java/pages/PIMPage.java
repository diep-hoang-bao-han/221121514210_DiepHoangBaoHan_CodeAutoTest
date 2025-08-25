package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PIMPage extends BasePage {
    @FindBy(linkText = "Add Employee")
    private WebElement addEmployeeLink;

    @FindBy(linkText = "Employee List")
    private WebElement employeeListLink;

    public PIMPage(WebDriver driver) {
        super(driver);
    }

    public AddEmployeePage navigateToAddEmployeePage() {
        addEmployeeLink.click();
        return new AddEmployeePage(driver);
    }

    public EmployeeListPage navigateToEmployeeList() {
        employeeListLink.click();
        return new EmployeeListPage(driver);
    }
}