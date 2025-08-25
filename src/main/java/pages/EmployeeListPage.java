package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
public class EmployeeListPage extends BasePage {

    @FindBy(xpath = "//input[@placeholder='Type for hints...']")
    private WebElement nameSearchField;

    @FindBy(xpath = "//label[text()='Employee Id']/following::input[1]")
    private WebElement idSearchField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[@class='oxd-table-card']//div[not(contains(@class, 'oxd-table-header'))]")
    private List<WebElement> employeeRecords;

    @FindBy(xpath = "//div[contains(@class, 'oxd-table')]")
    private WebElement resultsTable;

    @FindBy(xpath = "//span[text()='No Records Found']")
    private WebElement noRecordsFoundMessage;

    @FindBy(xpath = "//div[@class='oxd-table-card'][1]//div[2]/div")
    private WebElement firstEmployeeIdElement;

    private WebDriver driver;
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    public EmployeeListPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void searchByName(String name) {
        nameSearchField.clear();
        nameSearchField.sendKeys(name);
        searchButton.click();
        wait.until(ExpectedConditions.visibilityOf(resultsTable));
        employeeRecords = driver.findElements(By.xpath("//div[@class='oxd-table-card']"));
        System.out.println("DEBUG: Số bản ghi sau khi search '" + name + "': " + employeeRecords.size());
    }

    public void searchById(String employeeId) {
        try {
            System.out.println("DEBUG: Đang tìm kiếm với ID: " + employeeId);

            wait.until(ExpectedConditions.visibilityOf(idSearchField));
            idSearchField.clear();

            idSearchField.sendKeys(employeeId);
            System.out.println("DEBUG: Đã nhập ID vào ô tìm kiếm.");

            wait.until(ExpectedConditions.elementToBeClickable(searchButton));
            searchButton.click();
            System.out.println("DEBUG: Đã click nút Search.");

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.oxd-table-card")),
                    ExpectedConditions.visibilityOf(noRecordsFoundMessage)
            ));
            System.out.println("DEBUG: Kết quả tìm kiếm đã load.");

            employeeRecords = driver.findElements(By.cssSelector("div.oxd-table-card"));
            System.out.println("DEBUG: Số bản ghi tìm thấy: " + employeeRecords.size());

        } catch (Exception e) {
            System.out.println("LỖI NGHIÊM TRỌNG trong quá trình searchById: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isRecordsFound() {
        boolean noRecordsVisible = false;
        try {
            noRecordsVisible = noRecordsFoundMessage.isDisplayed();
        } catch (Exception e) {
        }
        return !employeeRecords.isEmpty() && !noRecordsVisible;
    }

    public boolean isNoRecordsFound() {
        try {
            return noRecordsFoundMessage.isDisplayed() || employeeRecords.isEmpty();
        } catch (Exception e) {
            return employeeRecords.isEmpty();
        }
    }

    public String getFirstEmployeeId() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.oxd-table-card")));

            if (!employeeRecords.isEmpty()) {
                String id = firstEmployeeIdElement.getText();
                System.out.println("DEBUG: Lấy được ID đầu tiên: " + id);
                return id;
            } else {
                System.out.println("DEBUG: Không có bản ghi nào để lấy ID.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy ID đầu tiên: " + e.getMessage());
            return null;
        }
    }

    public void deleteFirstEmployee() {
        if (isRecordsFound()) {
            try {

                WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//i[@class='oxd-icon bi-trash'])[1]")));
                deleteBtn.click();
                System.out.println("DEBUG: Đã click nút Delete.");

                WebElement confirmDeleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class, 'oxd-button--label-danger')]")));
                confirmDeleteBtn.click();
                System.out.println("DEBUG: Đã click nút xác nhận xóa.");

                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'oxd-toast--success')]")));
                System.out.println("DEBUG: Thông báo xóa thành công xuất hiện.");

                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'oxd-toast--success')]")));
                System.out.println("DEBUG: Thông báo xóa thành công đã biến mất.");

                wait.until(ExpectedConditions.refreshed(
                        ExpectedConditions.visibilityOfAllElementsLocatedBy(
                                By.cssSelector("div.oxd-table-card"))));

                employeeRecords = driver.findElements(By.cssSelector("div.oxd-table-card"));
                System.out.println("DEBUG [" + LocalDateTime.now() + "]: Số bản ghi sau khi xóa: " + employeeRecords.size());

            } catch (Exception e) {
                System.out.println("Lỗi trong quá trình xóa: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
