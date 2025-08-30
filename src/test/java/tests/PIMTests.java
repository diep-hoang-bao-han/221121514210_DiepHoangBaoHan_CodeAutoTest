package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.WaitUtils;

public class PIMTests extends BaseTest {

    private DashboardPage dashboardPage;
    private PIMPage pimPage;

    @BeforeMethod
    public void setUp() {
        LoginPage loginPage = new LoginPage(driver);
        dashboardPage = loginPage.loginWithValidCredentials(
                webDriverConfig.getUsername(),
                webDriverConfig.getPassword()
        );
        pimPage = dashboardPage.navigateToPIMModule();
    }

    @AfterMethod
    public void tearDown() {
        dashboardPage.logout();
    }

    @Test(priority = 1, description = "Add new employee with auto-generated ID")
    public void testAddNewEmployee() {
        AddEmployeePage addEmployeePage = pimPage.navigateToAddEmployeePage();
        String firstName = "John";
        String lastName = "Doe" + System.currentTimeMillis();
        addEmployeePage.addNewEmployee(firstName, lastName);
        Assert.assertEquals(addEmployeePage.getPersonalDetailsHeaderText(), "Personal Details");
    }

    @Test(priority = 2, description = "Search employee by Employee ID")
    public void testSearchEmployeeById() {
        EmployeeListPage employeeListPage = pimPage.navigateToEmployeeList();
        String employeeId = employeeListPage.getFirstEmployeeId();
        employeeListPage.searchById(employeeId);
        Assert.assertTrue(employeeListPage.isRecordsFound());
        Assert.assertEquals(employeeListPage.getFirstEmployeeId(), employeeId);
    }

    @Test(priority = 3, description = "Delete employee and verify removal from system")
    public void testDeleteEmployeeSuccessfully() {
        AddEmployeePage addEmployeePage = pimPage.navigateToAddEmployeePage();
        String firstName = "John";
        String lastName = "ToDelete" + System.currentTimeMillis();
        String fullName = firstName + " " + lastName;

        addEmployeePage.addNewEmployee(firstName, lastName);
        Assert.assertEquals(addEmployeePage.getPersonalDetailsHeaderText(), "Personal Details");

        EmployeeListPage employeeListPage = pimPage.navigateToEmployeeList();
        employeeListPage.searchByName(fullName);
        employeeListPage.deleteFirstEmployee();
        employeeListPage.searchByName(fullName); // Search lại với fullName
        Assert.assertTrue(employeeListPage.isNoRecordsFound(), "Vẫn tìm thấy bản ghi sau khi xóa: " + fullName);
    }
}
