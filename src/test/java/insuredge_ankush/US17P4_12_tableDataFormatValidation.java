package insuredge_ankush;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class US17P4_12_tableDataFormatValidation extends testBase{

	  @BeforeSuite
	    public void beforeSuite() {
	        driver = new ChromeDriver();
	        wait = getWait();
	    }

	    @AfterSuite(alwaysRun = true)
	    public void afterSuite() {
	        if (driver != null) driver.quit();
	    }

	    @BeforeClass
	    public void start() {
	        launchAndNavigation();
	        login();
	        navigation_pendingPH();
	        Assert.assertTrue(correctPageSanity_pph1(), "Not on Pending Policy Holders page.");
	    }
	    
    @Test(priority = 1)
    public void TC01_ValidateCustomerNameDisplayFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: No data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            List<WebElement> cells = wait.until(
                    ExpectedConditions.visibilityOfAllElements(
                            row.findElements(By.tagName("td"))
                    ));

            String customerName = cells.get(0).getText().trim();

            Assert.assertFalse(customerName.isEmpty(),
                    "Row " + (i + 1) + " : Customer Name is empty!");

            Assert.assertTrue(customerName.matches(".*[A-Za-z0-9].*"),
                    "Row " + (i + 1) + " : Invalid Customer Name: " + customerName);
        }
    }


    @Test(priority = 2)
    public void TC02_ValidateMobileNumbersFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: No data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);
            String mobile = row.findElement(By.xpath("./td[2]")).getText().trim();

            Assert.assertTrue(mobile.matches("\\d{10}"),
                    "FAIL: Row " + (i + 1) + " invalid mobile: " + mobile);
        }
    }


    @Test(priority = 3)
    public void TC03_ValidateEmailFormat() {

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: no data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            String email = row.findElement(By.xpath("./td[3]")).getText().trim();

            Assert.assertFalse(email.isEmpty(),
                    "FAIL: Row " + (i + 1) + " email empty!");

            Assert.assertFalse(email.contains(" "),
                    "FAIL: Row " + (i + 1) + " email contains whitespace.");

            Assert.assertTrue(email.matches(emailRegex),
                    "FAIL: Row " + (i + 1) + " invalid email: " + email);
        }
    }


    @Test(priority = 4)
    public void TC04_ValidatePolicyNameDisplayFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: no data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            String policyName = row.findElement(By.xpath("./td[4]")).getText().trim();

            boolean readable = !policyName.isEmpty() &&
                    policyName.matches(".*[A-Za-z0-9].*");

            Assert.assertTrue(readable,
                    "FAIL: Row " + (i + 1) + " invalid policy name: " + policyName);
        }
    }


    @Test(priority = 5)
    public void TC05_ValidateMainCategoryDisplayFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: no data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            String mainCat = row.findElement(By.xpath("./td[5]")).getText().trim();

            Assert.assertFalse(mainCat.isEmpty(),
                    "FAIL: Row " + (i + 1) + " main category empty!");

            Assert.assertTrue(mainCat.matches(".*[A-Za-z0-9].*"),
                    "FAIL: Row " + (i + 1) + " invalid main category: " + mainCat);
        }
    }


    @Test(priority = 6)
    public void TC06_ValidateSubCategoryFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: No data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            String subCat = row.findElement(By.xpath("./td[6]")).getText().trim();

            Assert.assertFalse(subCat.isEmpty(),
                    "FAIL: Row " + (i + 1) + " sub category empty!");

            Assert.assertTrue(subCat.matches(".*[A-Za-z0-9].*"),
                    "FAIL: Row " + (i + 1) + " invalid sub category: " + subCat);
        }
    }


    @Test(priority = 7)
    public void TC07_ValidateSumAssuredFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: No data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            String sumAssured = row.findElement(By.xpath("./td[7]")).getText().trim();

            Assert.assertTrue(sumAssured.matches("\\d+\\.\\d{2}"),
                    "FAIL: Row " + (i + 1) + " invalid Sum Assured: " + sumAssured);
        }
    }


    @Test(priority = 8)
    public void TC08_ValidatePremiumFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: No data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);
//            stableScrollIntoView(row);

            String premium = row.findElement(By.xpath("./td[8]")).getText().trim();

            Assert.assertTrue(premium.matches("\\d+\\.\\d{2}"),
                    "FAIL: Row " + (i + 1) + " invalid Premium: " + premium);
        }
    }


    @Test(priority = 9)
    public void TC09_ValidateTenureDisplayFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: No data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            String tenure = row.findElement(By.xpath("./td[9]")).getText().trim();

            Assert.assertTrue(tenure.matches("\\d+"),
                    "FAIL: Row " + (i + 1) + " invalid Tenure: " + tenure);
        }
    }


    @Test(priority = 10)
    public void TC10_ValidateAppliedOnDateFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: No data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            String date = row.findElement(By.xpath("./td[10]")).getText().trim();

            Assert.assertTrue(date.matches("\\d{4}-\\d{2}-\\d{2}"),
                    "FAIL: Row " + (i + 1) + " invalid Date: " + date);
        }
    }


    @Test(priority = 11)
    public void TC11_ValidateActionsButtonsDisplayFormat() {

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(
                By.xpath(".//tbody/tr[td and not(td[@colspan])]"));

        Assert.assertTrue(rows.size() > 0, "FAIL: No data rows found.");

        int limit = Math.min(10, rows.size());

        for (int i = 0; i < limit; i++) {

            WebElement row = rows.get(i);

            WebElement actionsCell = row.findElement(By.xpath("./td[11]"));

            List<WebElement> approveBtns =
                    actionsCell.findElements(By.xpath(".//input[@value='Approve']"));

            List<WebElement> rejectBtns =
                    actionsCell.findElements(By.xpath(".//input[@value='Reject']"));

            Assert.assertTrue(!approveBtns.isEmpty() && approveBtns.get(0).isDisplayed(),
                    "FAIL: Row " + (i + 1) + " Approve button missing!");

            Assert.assertTrue(!rejectBtns.isEmpty() && rejectBtns.get(0).isDisplayed(),
                    "FAIL: Row " + (i + 1) + " Reject button missing!");
        }
    }
}